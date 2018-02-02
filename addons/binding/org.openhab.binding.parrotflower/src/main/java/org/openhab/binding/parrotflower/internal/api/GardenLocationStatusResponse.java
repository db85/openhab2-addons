/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.parrotflower.internal.api;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link GardenLocationStatusResponse} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class GardenLocationStatusResponse {
    @SerializedName("garden_status_version")
    private String gardenStatusVersion;
    @SerializedName("locations")
    private List<GardenLocation> locations;
    @SerializedName("server_identifier")
    private String serverIdentifier;
    @SerializedName("user_config_version")
    private String userConfigVersion;

    public String getGardenStatusVersion() {
        return gardenStatusVersion;
    }

    public List<GardenLocation> getLocations() {
        return locations;
    }

    public String getServerIdentifier() {
        return serverIdentifier;
    }

    public String getUserConfigVersion() {
        return userConfigVersion;
    }

    public void setGardenStatusVersion(String gardenStatusVersion) {
        this.gardenStatusVersion = gardenStatusVersion;
    }

    public void setLocations(List<GardenLocation> locations) {
        this.locations = locations;
    }

    public void setServerIdentifier(String serverIdentifier) {
        this.serverIdentifier = serverIdentifier;
    }

    public void setUserConfigVersion(String userConfigVersion) {
        this.userConfigVersion = userConfigVersion;
    }

}