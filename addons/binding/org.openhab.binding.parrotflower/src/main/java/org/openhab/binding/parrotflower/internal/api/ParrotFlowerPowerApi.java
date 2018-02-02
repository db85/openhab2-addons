/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.parrotflower.internal.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The {@link } api model class
 *
 * @author Daniel Bauer - Initial contribution
 */
public interface ParrotFlowerPowerApi {
    @GET("user/v1/authenticate")
    Single<AuthResponse> loadAuth(@Query("grant_type") String grantType, @Query("username") String username,
            @Query("password") String password, @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret);

    @GET("user/v4/profile")
    Single<ProfileRepsonse> loadProfile();

    @GET("/garden/v1/status")
    Single<GardenLocationStatusResponse> loadGardenLocationStatus();

    @GET("/garden/v2/configuration")
    Single<GardenConfigurationResponse> loadGardenConfiguration();
}
