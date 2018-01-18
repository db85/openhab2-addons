package org.openhab.binding.parrotflower.internal.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ParrotFlowerPowerApi {
    @GET("user/v1/authenticate")
    Call<AuthResponse> loadAuth(@Query("grant_type") String grantType, @Query("username") String username,
            @Query("password") String password, @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret);

    @GET("user/v4/profile")
    Call<ProfileRepsonse> loadProfile();
}
