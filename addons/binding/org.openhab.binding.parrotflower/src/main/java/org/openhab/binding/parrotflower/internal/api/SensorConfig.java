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
 * The {@link SensorConfig} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class SensorConfig {
    @SerializedName("assignment_datetime_utc")
    private String assignmentDatetimeUtc;
    @SerializedName("calibration_data")
    private String calibrationData;
    @SerializedName("color")
    private String color;
    @SerializedName("firmware_version")
    private String firmwareVersion;
    @SerializedName("hardware_revision")
    private String hardwareRevision;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("sensor_identifier")
    private String sensorIdentifier;
    @SerializedName("sensor_type")
    private String sensorType;
    @SerializedName("system_id")
    private String systemId;

    public String getAssignmentDatetimeUtc() {
        return assignmentDatetimeUtc;
    }

    public String getCalibrationData() {
        return calibrationData;
    }

    public String getColor() {
        return color;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getHardwareRevision() {
        return hardwareRevision;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSensorIdentifier() {
        return sensorIdentifier;
    }

    public String getSensorType() {
        return sensorType;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setAssignmentDatetimeUtc(String assignmentDatetimeUtc) {
        this.assignmentDatetimeUtc = assignmentDatetimeUtc;
    }

    public void setCalibrationData(String calibrationData) {
        this.calibrationData = calibrationData;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public void setHardwareRevision(String hardwareRevision) {
        this.hardwareRevision = hardwareRevision;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSensorIdentifier(String sensorIdentifier) {
        this.sensorIdentifier = sensorIdentifier;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

}
