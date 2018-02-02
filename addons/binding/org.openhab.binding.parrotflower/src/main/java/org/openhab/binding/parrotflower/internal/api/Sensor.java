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
 * The {@link Sensor} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class Sensor {
    @SerializedName("current_history_index")
    private String currentHistoryIndex;
    @SerializedName("firmware_update")
    private Firmware firmwareUpdate;
    @SerializedName("sensor_identifier")
    private String sensorIdentifier;
    @SerializedName("sensor_type")
    private String sensorType;

    public String getCurrentHistoryIndex() {
        return currentHistoryIndex;
    }

    public Firmware getFirmwareUpdate() {
        return firmwareUpdate;
    }

    public String getSensorIdentifier() {
        return sensorIdentifier;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setCurrentHistoryIndex(String currentHistoryIndex) {
        this.currentHistoryIndex = currentHistoryIndex;
    }

    public void setFirmwareUpdate(Firmware firmwareUpdate) {
        this.firmwareUpdate = firmwareUpdate;
    }

    public void setSensorIdentifier(String sensorIdentifier) {
        this.sensorIdentifier = sensorIdentifier;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

}