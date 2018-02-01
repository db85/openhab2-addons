package org.openhab.binding.parrotflower.internal.discovery;

import static org.openhab.binding.parrotflower.ParrotFlowerBindingConstants.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.parrotflower.handler.ParrotFlowerBridgeHandler;
import org.openhab.binding.parrotflower.handler.SensorDeviceHandler;
import org.openhab.binding.parrotflower.handler.UserProfileHandler;
import org.openhab.binding.parrotflower.internal.ParrotFlowerHandlerFactory;
import org.openhab.binding.parrotflower.internal.api.GardenConfiguration;
import org.openhab.binding.parrotflower.internal.api.GardenConfigurationResponse;
import org.openhab.binding.parrotflower.internal.api.GardenLocation;
import org.openhab.binding.parrotflower.internal.api.GardenLocationStatusResponse;
import org.openhab.binding.parrotflower.internal.api.ParrotFlowerPowerApi;
import org.openhab.binding.parrotflower.internal.api.ProfileRepsonse;
import org.openhab.binding.parrotflower.internal.api.UserProfile;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

@Component(service = DiscoveryService.class, immediate = true, configurationPid = "discovery.parrotflower")
public class ParrotFlowerDiscoveryService extends AbstractDiscoveryService {

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

        public void setGardenConfigurationResponse(GardenConfigurationResponse gardenConfigurationResponse) {
            this.gardenConfigurationResponse = gardenConfigurationResponse;
        }

        public GardenLocationStatusResponse getGardenLocationStatusResponse() {
            return gardenLocationStatusResponse;
        }

        public void setGardenLocationStatusResponse(GardenLocationStatusResponse gardenLocationStatusResponse) {
            this.gardenLocationStatusResponse = gardenLocationStatusResponse;
        }

    }

    private static final int CHANGED_CHECK_INTERVAL_MINUTES = 60;
    private static final int DISCOVER_TIMEOUT_SECONDS = 10;
    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.singleton(THING_TYPE_USER_PROFILE);

    private ParrotFlowerHandlerFactory handlerFactory;
    private final Logger logger = LoggerFactory.getLogger(ParrotFlowerDiscoveryService.class);

    // private CompositeDisposable jobDisposeable;
    private CompositeDisposable scanDisposeable;
    // private CompositeDisposable backgroundDisposeable;

    public ParrotFlowerDiscoveryService() {
        super(SUPPORTED_THING_TYPES, DISCOVER_TIMEOUT_SECONDS, false);
    }

    // @Override
    // protected void startBackgroundDiscovery() {
    // handlerFactory.getBridgeList().forEach((bridge) -> {
    // backgroundDisposeable
    // .add(Observable.interval(CHANGED_CHECK_INTERVAL_MINUTES, TimeUnit.MINUTES).flatMap((interval) -> {
    // return loadData(bridge).toObservable();
    // }).subscribe());
    // });
    // }

    @Override
    public synchronized void abortScan() {
        if (scanDisposeable != null && !scanDisposeable.isDisposed()) {
            scanDisposeable.dispose();
        }
        super.abortScan();
    }

    private void handleGardenLocationStatusResponse(ParrotFlowerBridgeHandler bridge,
            GardenResponseZipModel gardenResponseZipModel) {

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
            } else {
                ThingUID sensorThing = new ThingUID(THING_TYPE_SENSOR_DEVICE,
                        identifier.replaceAll("[^a-zA-Z0-9_]", ""));
                Map<String, Object> properties = new HashMap<>();
                properties.put("identifier", identifier);

                thingDiscovered(DiscoveryResultBuilder.create(sensorThing).withBridge(bridge.getThing().getBridgeUID())
                        .withLabel(config.getPlantNickname()).withProperties(properties).build());
            }
        });
    }

    private void handleUserProfileResponse(ParrotFlowerBridgeHandler bridge, ProfileRepsonse profileResponse) {
        UserProfile userProfile = profileResponse.getUserProfile();

        String mail = userProfile.getEmail().toLowerCase();

        List<UserProfileHandler> profiles = handlerFactory.getUserProfileHandlerList().stream()
                .filter(profileHandler -> mail
                        .equals(profileHandler.getThing().getConfiguration().getProperties().get("email")))
                .collect(Collectors.toList());
        if (profiles.size() > 0) {
            profiles.forEach(profileHandler -> profileHandler.updateChannels(userProfile));
        } else {
            ThingUID userProfileThing = new ThingUID(THING_TYPE_USER_PROFILE, mail.replaceAll("[^a-zA-Z0-9_]", ""));
            Map<String, Object> properties = new HashMap<>();
            properties.put("email", mail);

            thingDiscovered(DiscoveryResultBuilder.create(userProfileThing).withBridge(bridge.getThing().getBridgeUID())
                    .withLabel("Parrot User Profile").withProperties(properties).build());
        }
    }

    private Completable loadData(ParrotFlowerBridgeHandler bridge) {
        ParrotFlowerPowerApi parrotFlowerPowerApi = bridge.getParrotFlowerPowerApi();
        return bridge.loadAuth().andThen(Completable.mergeArray(
                parrotFlowerPowerApi.loadProfile().doOnSuccess(response -> handleUserProfileResponse(bridge, response))
                        .toCompletable(),

                Single.zip(parrotFlowerPowerApi.loadGardenConfiguration(),
                        parrotFlowerPowerApi.loadGardenLocationStatus(), GardenResponseZipModel::new)
                        .doOnSuccess(zipModel -> handleGardenLocationStatusResponse(bridge, zipModel)).toCompletable()

        ));
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
    public void setParrotFlowerHandlerFactory(ParrotFlowerHandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    @Override
    protected void startScan() {
        if (scanDisposeable != null && !scanDisposeable.isDisposed()) {
            scanDisposeable.dispose();
        }
        scanDisposeable = new CompositeDisposable();

        handlerFactory.getBridgeHandlerList().forEach((bridge) -> {
            scanDisposeable.add(loadData(bridge).subscribe(() -> {
                logger.debug("scan load data success");
            }, (throwable) -> {
                logger.warn("scan load data failed {}", throwable);
            }));
        });
    }

    // @Override
    // protected void stopBackgroundDiscovery() {
    // if (backgroundDisposeable != null && !backgroundDisposeable.isDisposed()) {
    // backgroundDisposeable.dispose();
    // }
    // }

    public void unsetParrotFlowerHandlerFactory(ParrotFlowerHandlerFactory handlerFactory) {
        this.handlerFactory = null;
    }
}