package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

public class Firmware {
    @SerializedName("firmware_update_url")
    private String firmwareUpdateUrl;
    @SerializedName("firmware_upgrade_available")
    private String firmwareUpgradeVvailable;
    @SerializedName("firmware_version")
    private String firmwareVersion;

    public String getFirmwareUpdateUrl() {
        return firmwareUpdateUrl;
    }

    public String getFirmwareUpgradeVvailable() {
        return firmwareUpgradeVvailable;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareUpdateUrl(String firmwareUpdateUrl) {
        this.firmwareUpdateUrl = firmwareUpdateUrl;
    }

    public void setFirmwareUpgradeVvailable(String firmwareUpgradeVvailable) {
        this.firmwareUpgradeVvailable = firmwareUpgradeVvailable;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

}
