/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link Firmware} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
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
