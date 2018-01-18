package org.openhab.binding.parrotflower.internal;

import static org.openhab.binding.parrotflower.ParrotFlowerBindingConstants.API_URL;

import java.io.IOException;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.parrotflower.internal.api.AuthResponse;
import org.openhab.binding.parrotflower.internal.api.ParrotFlowerPowerApi;
import org.openhab.binding.parrotflower.internal.api.ProfileRepsonse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder newBuilder = chain.request().newBuilder();
                newBuilder.addHeader("Authorization", String.format("Bearer %s", configuration.accessToken));
                return chain.proceed(newBuilder.build());
            }
        }).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create())
                .client(httpClient).build();
        parrotFlowerPowerApi = retrofit.create(ParrotFlowerPowerApi.class);

        configuration = getConfigAs(ParrotFlowerBridgeConfiguration.class);
        loadAuth();
    }

    private void loadAuth() {
        parrotFlowerPowerApi.loadAuth("password", configuration.username, configuration.password,
                configuration.clientId, configuration.clientSecret).enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                        logger.warn("load auth failed", throwable);
                        updateStatus(ThingStatus.OFFLINE);
                    }

                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful()) {
                            logger.info("load auth successful");
                            updateStatus(ThingStatus.ONLINE);
                            configuration.accessToken = response.body().getAccessToken();
                            loadProfile();
                        } else {
                            updateStatus(ThingStatus.OFFLINE);
                            logger.warn("load auth failed with status code {}", response.code());
                        }

                    }
                });

    }

    private void loadProfile() {
        parrotFlowerPowerApi.loadProfile().enqueue(new Callback<ProfileRepsonse>() {
            @Override
            public void onFailure(Call<ProfileRepsonse> call, Throwable throwable) {
                logger.warn("auth profile failed", throwable);

            }

            @Override
            public void onResponse(Call<ProfileRepsonse> call, Response<ProfileRepsonse> response) {
                if (response.isSuccessful()) {
                    logger.info("load profile successful");

                } else {
                    logger.warn("load profile failed with status code {}", response.code());
                }
            }
        });
    }

}
