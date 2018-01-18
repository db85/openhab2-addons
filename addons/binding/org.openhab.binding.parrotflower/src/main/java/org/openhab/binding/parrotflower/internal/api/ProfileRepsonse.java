package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

public class ProfileRepsonse {
    @SerializedName("garden_status_version")
    private String gardenStatusVersion;
    @SerializedName("server_identifier")
    private String serverIdentifier;
    @SerializedName("user_config_version")
    private String userConfigVersion;
    @SerializedName("user_profile")
    private UserProfile userProfile;

    public String getGardenStatusVersion() {
        return gardenStatusVersion;
    }

    public String getServerIdentifier() {
        return serverIdentifier;
    }

    public String getUserConfigVersion() {
        return userConfigVersion;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setGardenStatusVersion(String gardenStatusVersion) {
        this.gardenStatusVersion = gardenStatusVersion;
    }

    public void setServerIdentifier(String serverIdentifier) {
        this.serverIdentifier = serverIdentifier;
    }

    public void setUserConfigVersion(String userConfigVersion) {
        this.userConfigVersion = userConfigVersion;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}