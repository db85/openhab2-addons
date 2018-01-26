package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

public class Sensor {
    @SerializedName("currentHistoryIndex")
    private String currentHistoryIndex;
    @SerializedName("firmware_update")
    private Firmware firmwareUpdate;
    @SerializedName("sensorIdentifier")
    private String sensor_identifier;
    @SerializedName("sensor_type")
    private String sensorType;

    public String getCurrentHistoryIndex() {
        return currentHistoryIndex;
    }

    public Firmware getFirmwareUpdate() {
        return firmwareUpdate;
    }

    public String getSensor_identifier() {
        return sensor_identifier;
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

    public void setSensor_identifier(String sensor_identifier) {
        this.sensor_identifier = sensor_identifier;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

}