package org.openhab.binding.parrotflower.internal;

import static org.openhab.binding.parrotflower.ParrotFlowerBindingConstants.API_URL;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.parrotflower.internal.api.ParrotFlowerPowerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Completable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParrotFlowerBridgeHandler extends BaseBridgeHandler {

    public ParrotFlowerBridgeConfiguration configuration;
    private Logger logger = LoggerFactory.getLogger(ParrotFlowerBridgeHandler.class);
    private ParrotFlowerPowerApi parrotFlowerPowerApi;

    public ParrotFlowerBridgeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void handleCommand(@NonNull ChannelUID channelUID, @NonNull Command command) {
        // todo
    }

    @Override
    public void initialize() {
        parrotFlowerPowerApi = createRetrofit().create(ParrotFlowerPowerApi.class);
        configuration = getConfigAs(ParrotFlowerBridgeConfiguration.class);
    }

    private Retrofit createRetrofit() {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder newBuilder = chain.request().newBuilder();
                newBuilder.addHeader("Authorization", String.format("Bearer %s", configuration.accessToken));
                return chain.proceed(newBuilder.build());
            }
        }).build();
        return new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(httpClient).build();
    }

    public Completable loadAuth() {
        if (!StringUtils.isEmpty(configuration.accessToken)) {
            return Completable.complete();
        }
        return parrotFlowerPowerApi.loadAuth("password", configuration.username, configuration.password,
                configuration.clientId, configuration.clientSecret).doOnSuccess((authResponse) -> {
                    configuration.accessToken = authResponse.getAccessToken();
                }).toCompletable();
    }

    // public Observable<DiscoveryResult> fetchData() {
    // return loadAuth().doOnComplete(() -> {
    // logger.debug("load data success");
    // updateStatus(ThingStatus.ONLINE);
    // }).doOnError((throwable) -> {
    // logger.error("load data failed", throwable);
    // updateStatus(ThingStatus.OFFLINE);
    // }).andThen(Observable.mergeArray(fetchProfile()));
    // }
    //
    // private Observable<DiscoveryResult> fetchProfile() {
    // return parrotFlowerPowerApi.loadProfile().map((profileResponse) -> {
    // return new DiscoveryResult();
    // }).toObservable();
    // }

    public ParrotFlowerPowerApi getParrotFlowerPowerApi() {
        return parrotFlowerPowerApi;
    }
}
