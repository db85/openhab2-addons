package org.openhab.binding.parrotflower.internal.api;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GardenConfiguration {

    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("in_pot")
    private String inPot;
    @SerializedName("is_indoor")
    private String isIndoor;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("location_identifier")
    private String locationIdentifier;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("pictures")
    private List<Picture> pictures;
    @SerializedName("plant_assigned_datetime_utc")
    private String plantAssignedDatetimeUtc;
    @SerializedName("plant_ids")
    private List<String> plantIds;
    @SerializedName("plant_nickname")
    private String plantNickname;
    @SerializedName("sensor")
    private SensorConfig sensor;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getInPot() {
        return inPot;
    }

    public String getIsIndoor() {
        return isIndoor;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLocationIdentifier() {
        return locationIdentifier;
    }

    public String getLongitude() {
        return longitude;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public String getPlantAssignedDatetimeUtc() {
        return plantAssignedDatetimeUtc;
    }

    public List<String> getPlantIds() {
        return plantIds;
    }

    public String getPlantNickname() {
        return plantNickname;
    }

    public SensorConfig getSensor() {
        return sensor;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setInPot(String inPot) {
        this.inPot = inPot;
    }

    public void setIsIndoor(String isIndoor) {
        this.isIndoor = isIndoor;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLocationIdentifier(String locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public void setPlantAssignedDatetimeUtc(String plantAssignedDatetimeUtc) {
        this.plantAssignedDatetimeUtc = plantAssignedDatetimeUtc;
    }

    public void setPlantIds(List<String> plantIds) {
        this.plantIds = plantIds;
    }

    public void setPlantNickname(String plantNickname) {
        this.plantNickname = plantNickname;
    }

    public void setSensor(SensorConfig sensor) {
        this.sensor = sensor;
    }

}