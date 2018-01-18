package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private String expiresIn;
    @SerializedName("import_status")
    private String importStatus;
    @SerializedName("refresh_token")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
