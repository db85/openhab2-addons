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
 * The {@link GaugeValues} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
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