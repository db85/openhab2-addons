package org.openhab.binding.parrotflower.internal.api;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GardenConfigurationResponse {

    @SerializedName("garden_status_version")
    private String gardenStatusVersion;
    @SerializedName("locations")
    private List<GardenConfiguration> locations;
    @SerializedName("server_identifier")
    private String serverIdentifier;
    @SerializedName("user_config_version")
    private String userConfigVersion;

    public String getGardenStatusVersion() {
        return gardenStatusVersion;
    }

    public List<GardenConfiguration> getLocations() {
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

    public void setLocations(List<GardenConfiguration> locations) {
        this.locations = locations;
    }

    public void setServerIdentifier(String serverIdentifier) {
        this.serverIdentifier = serverIdentifier;
    }

    public void setUserConfigVersion(String userConfigVersion) {
        this.userConfigVersion = userConfigVersion;
    }

}
