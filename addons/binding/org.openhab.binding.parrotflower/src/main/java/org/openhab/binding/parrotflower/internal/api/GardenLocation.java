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
 * The {@link GardenLocation} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class GardenLocation {
    @SerializedName("air_temperature")
    private AnalysisStatus airTemperature;
    @SerializedName("battery")
    private Battery battery;
    @SerializedName("fertilizer")
    private AnalysisStatus fertilizer;
    @SerializedName("first_sample_utc")
    private String firstSampleUtc;
    @SerializedName("global_validity_datetime_utc")
    private String globalValidityDatetimeUtc;
    @SerializedName("growth_day")
    private String growthDay;
    @SerializedName("last_processed_upload_datetime_utc")
    private String lastProcessedUploadDatetimeUtc;
    @SerializedName("last_sample_upload")
    private String lastSampleUpload;
    @SerializedName("last_sample_utc")
    private String lastSampleUtc;
    @SerializedName("light")
    private AnalysisStatus light;
    @SerializedName("location_identifier")
    private String locationIdentifier;
    @SerializedName("processing_uploads")
    private String processingUploads;
    @SerializedName("sensor")
    private Sensor sensor;
    @SerializedName("total_sample_count")
    private String totalSampleCount;
    @SerializedName("watering")
    private Watering watering;

    public AnalysisStatus getAirTemperature() {
        return airTemperature;
    }

    public Battery getBattery() {
        return battery;
    }

    public AnalysisStatus getFertilizer() {
        return fertilizer;
    }

    public String getFirstSampleUtc() {
        return firstSampleUtc;
    }

    public String getGlobalValidityDatetimeUtc() {
        return globalValidityDatetimeUtc;
    }

    public String getGrowthDay() {
        return growthDay;
    }

    public String getLastProcessedUploadDatetimeUtc() {
        return lastProcessedUploadDatetimeUtc;
    }

    public String getLastSampleUpload() {
        return lastSampleUpload;
    }

    public String getLastSampleUtc() {
        return lastSampleUtc;
    }

    public AnalysisStatus getLight() {
        return light;
    }

    public String getLocationIdentifier() {
        return locationIdentifier;
    }

    public String getProcessingUploads() {
        return processingUploads;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public String getTotalSampleCount() {
        return totalSampleCount;
    }

    public Watering getWatering() {
        return watering;
    }

    public void setAirTemperature(AnalysisStatus airTemperature) {
        this.airTemperature = airTemperature;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public void setFertilizer(AnalysisStatus fertilizer) {
        this.fertilizer = fertilizer;
    }

    public void setFirstSampleUtc(String firstSampleUtc) {
        this.firstSampleUtc = firstSampleUtc;
    }

    public void setGlobalValidityDatetimeUtc(String globalValidityDatetimeUtc) {
        this.globalValidityDatetimeUtc = globalValidityDatetimeUtc;
    }

    public void setGrowthDay(String growthDay) {
        this.growthDay = growthDay;
    }

    public void setLastProcessedUploadDatetimeUtc(String lastProcessedUploadDatetimeUtc) {
        this.lastProcessedUploadDatetimeUtc = lastProcessedUploadDatetimeUtc;
    }

    public void setLastSampleUpload(String lastSampleUpload) {
        this.lastSampleUpload = lastSampleUpload;
    }

    public void setLastSampleUtc(String lastSampleUtc) {
        this.lastSampleUtc = lastSampleUtc;
    }

    public void setLight(AnalysisStatus light) {
        this.light = light;
    }

    public void setLocationIdentifier(String locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
    }

    public void setProcessingUploads(String processingUploads) {
        this.processingUploads = processingUploads;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setTotalSampleCount(String totalSampleCount) {
        this.totalSampleCount = totalSampleCount;
    }

    public void setWatering(Watering watering) {
        this.watering = watering;
    }

}