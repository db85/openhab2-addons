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
 * The {@link Battery} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
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
