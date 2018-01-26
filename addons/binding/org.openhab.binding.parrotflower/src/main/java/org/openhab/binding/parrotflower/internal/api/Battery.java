package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

public class Battery {
    @SerializedName("gauge_values")
    private GaugeValues gaugeValues;

    public GaugeValues getGaugeValues() {
        return gaugeValues;
    }

    public void setGaugeValues(GaugeValues gaugeValues) {
        this.gaugeValues = gaugeValues;
    }
}
