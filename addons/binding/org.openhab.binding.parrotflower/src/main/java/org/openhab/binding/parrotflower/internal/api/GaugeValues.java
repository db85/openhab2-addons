package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

public class GaugeValues {
    @SerializedName("current_value")
    private String currentValue;
    @SerializedName("max_threshold")
    private String maxThreshold;
    @SerializedName("min_threshold")
    private String minThreshold;

    public String getCurrentValue() {
        return currentValue;
    }

    public String getMaxThreshold() {
        return maxThreshold;
    }

    public String getMinThreshold() {
        return minThreshold;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public void setMaxThreshold(String maxThreshold) {
        this.maxThreshold = maxThreshold;
    }

    public void setMinThreshold(String minThreshold) {
        this.minThreshold = minThreshold;
    }

}