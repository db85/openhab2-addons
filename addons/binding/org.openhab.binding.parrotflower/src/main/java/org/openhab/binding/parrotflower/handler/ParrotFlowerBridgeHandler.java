package org.openhab.binding.parrotflower.handler;

import static org.openhab.binding.parrotflower.ParrotFlowerBindingConstants.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.parrotflower.internal.ParrotFlowerBridgeConfiguration;
import org.openhab.binding.parrotflower.internal.ParrotFlowerHandlerFactory;
import org.openhab.binding.parrotflower.internal.api.GardenConfiguration;
import org.openhab.binding.parrotflower.internal.api.GardenConfigurationResponse;
import org.openhab.binding.parrotflower.internal.api.GardenLocation;
import org.openhab.binding.parrotflower.internal.api.GardenLocationStatusResponse;
import org.openhab.binding.parrotflower.internal.api.ParrotFlowerPowerApi;
import org.openhab.binding.parrotflower.internal.api.ProfileRepsonse;
import org.openhab.binding.parrotflower.internal.api.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParrotFlowerBridgeHandler extends BaseBridgeHandler {

    public interface DiscoveryListener {
        void onDeviceDiscovered(ThingTypeUID thingTypeUID, String identifier, String label);
    }

    class GardenResponseZipModel {
        private GardenConfigurationResponse gardenConfigurationResponse;
        private GardenLocationStatusResponse gardenLocationStatusResponse;

        public GardenResponseZipModel(GardenConfigurationResponse gardenConfigurationResponse,
                GardenLocationStatusResponse gardenLocationStatusResponse) {
            this.gardenConfigurationResponse = gardenConfigurationResponse;
            this.gardenLocationStatusResponse = gardenLocationStatusResponse;
        }

        public GardenConfigurationResponse getGardenConfigurationResponse() {
            return gardenConfigurationResponse;
        }

        public GardenLocationStatusResponse getGardenLocationStatusResponse() {
            return gardenLocationStatusResponse;
        }

        public void setGardenConfigurationResponse(GardenConfigurationResponse gardenConfigurationResponse) {
            this.gardenConfigurationResponse = gardenConfigurationResponse;
        }

        public void setGardenLocationStatusResponse(GardenLocationStatusResponse gardenLocationStatusResponse) {
            this.gardenLocationStatusResponse = gardenLocationStatusResponse;
        }

    }

    private String accessToken;
    private DiscoveryListener discoveryListener;
    private ParrotFlowerHandlerFactory handlerFactory;
    private final Logger logger = LoggerFactory.getLogger(ParrotFlowerBridgeHandler.class);
    private ParrotFlowerPowerApi parrotFlowerPowerApi;
    private Disposable refreshDisposable;

    public ParrotFlowerBridgeHandler(ParrotFlowerHandlerFactory handlerFactory, Bridge bridge) {
        super(bridge);
        this.handlerFactory = handlerFactory;
    }

    private Retrofit createRetrofit() {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder newBuilder = chain.request().newBuilder();
                newBuilder.addHeader("Authorization", String.format("Bearer %s", accessToken));
                return chain.proceed(newBuilder.build());
            }
        }).build();
        return new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(httpClient).build();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (refreshDisposable != null) {
            refreshDisposable.dispose();
            refreshDisposable = null;
        }
    }

    public DiscoveryListener getDiscoveryListener() {
        return discoveryListener;
    }

    @Override
    public void handleCommand(@NonNull ChannelUID channelUID, @NonNull Command command) {
        switch (channelUID.getId()) {
            case CHANNEL_REFRESH:
                if (command instanceof OnOffType) {
                    if (((OnOffType) command).equals(OnOffType.ON)) {
                        initRefresh();
                    }
                }
                break;
            default:
                // not supported
                break;
        }
    }

    @Override
    public void handleConfigurationUpdate(Map<String, Object> configurationParameters) {
        super.handleConfigurationUpdate(configurationParameters);
        accessToken = null;
        initRefresh();
    }

    private void handleGardenLocationStatusResponse(GardenResponseZipModel gardenResponseZipModel) {

        List<GardenConfiguration> gardenConfiguration = gardenResponseZipModel.getGardenConfigurationResponse()
                .getLocations();
        if (gardenConfiguration == null) {
            return;
        }

        gardenConfiguration.forEach(config -> {
            String identifier = config.getLocationIdentifier();
            List<SensorDeviceHandler> devices = handlerFactory.getSensorDeviceHandlerList().stream()
                    .filter(profileHandler -> identifier
                            .equals(profileHandler.getThing().getConfiguration().getProperties().get("identifier")))
                    .collect(Collectors.toList());
            if (devices.size() > 0) {
                Optional<GardenLocation> gardenLocation = gardenResponseZipModel.getGardenLocationStatusResponse()
                        .getLocations().stream().filter(location -> location.getLocationIdentifier().equals(identifier))
                        .findFirst();
                devices.forEach(deviceHandler -> deviceHandler.updateChannels(config,
                        gardenLocation.isPresent() ? gardenLocation.get() : null));
            } else if (discoveryListener != null) {
                discoveryListener.onDeviceDiscovered(THING_TYPE_SENSOR_DEVICE, identifier, config.getPlantNickname());
            }
        });
    }

    private void handleUserProfileResponse(ProfileRepsonse profileResponse) {
        UserProfile userProfile = profileResponse.getUserProfile();

        String mail = userProfile.getEmail().toLowerCase();

        List<UserProfileHandler> profiles = handlerFactory.getUserProfileHandlerList().stream()
                .filter(profileHandler -> mail
                        .equals(profileHandler.getThing().getConfiguration().getProperties().get("email")))
                .collect(Collectors.toList());
        if (profiles.size() > 0) {
            profiles.forEach(profileHandler -> profileHandler.updateChannels(userProfile));
        } else if (discoveryListener != null) {
            discoveryListener.onDeviceDiscovered(THING_TYPE_USER_PROFILE, mail, "Parrot User Profile");
        }
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.ONLINE);
        parrotFlowerPowerApi = createRetrofit().create(ParrotFlowerPowerApi.class);
        initRefresh();
    }

    private void initRefresh() {
        ParrotFlowerBridgeConfiguration configuration = getConfigAs(ParrotFlowerBridgeConfiguration.class);
        if (configuration.refreshInterval <= 0) {
            logger.error("invalid refresh interval {}", configuration.refreshInterval);
            return;
        }

        if (refreshDisposable != null) {
            refreshDisposable.dispose();
            refreshDisposable = null;
        }

        refreshDisposable = Observable.interval(0, configuration.refreshInterval, TimeUnit.MINUTES)
                .flatMapCompletable((interval) -> {
                    updateRefreshChannel(OnOffType.ON);
                    return loadData().onErrorResumeNext((throwable) -> {
                        if (throwable instanceof HttpException) {
                            HttpException httpException = (HttpException) throwable;
                            if (httpException.code() == 401 && !StringUtils.isEmpty(accessToken)) {
                                accessToken = null;
                                return loadData();
                            }
                        }

                        logger.warn("refresh failed {}", throwable);
                        return Completable.complete();
                    }).doOnTerminate(() -> {
                        updateRefreshChannel(OnOffType.OFF);
                    });
                }).subscribe(() -> {
                    logger.debug("refresh completed");
                }, (throwable) -> {
                    logger.warn("init refresh failed {}", throwable);
                });

    }

    private Completable loadAuth() {
        ParrotFlowerBridgeConfiguration configuration = getConfigAs(ParrotFlowerBridgeConfiguration.class);
        if (!StringUtils.isEmpty(accessToken)) {
            return Completable.complete();
        }
        return parrotFlowerPowerApi.loadAuth("password", configuration.username, configuration.password,
                configuration.clientId, configuration.clientSecret).doOnSuccess((authResponse) -> {
                    this.accessToken = authResponse.getAccessToken();
                }).toCompletable();
    }

    public Completable loadData() {
        return loadAuth().andThen(Completable.mergeArray(
                parrotFlowerPowerApi.loadProfile().doOnSuccess(this::handleUserProfileResponse).toCompletable(),

                Single.zip(parrotFlowerPowerApi.loadGardenConfiguration(),
                        parrotFlowerPowerApi.loadGardenLocationStatus(), GardenResponseZipModel::new)
                        .doOnSuccess(this::handleGardenLocationStatusResponse).toCompletable()

        ));
    }

    public void setDiscoveryListener(DiscoveryListener discoveryListener) {
        this.discoveryListener = discoveryListener;
    }

    private void updateRefreshChannel(OnOffType onOffType) {
        Channel channel = getThing().getChannel(CHANNEL_REFRESH);
        if (channel == null) {
            return;
        }
        updateState(channel.getUID(), onOffType);
    }
}
