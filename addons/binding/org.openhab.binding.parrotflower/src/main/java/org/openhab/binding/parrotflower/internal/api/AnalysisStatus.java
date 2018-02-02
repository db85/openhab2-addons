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
 * The {@link AnalysisStatus} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class AnalysisStatus {
    @SerializedName("gauge_values")
    private GaugeValues gaugeValues;
    @SerializedName("instruction_key")
    private String instructionKey;
    @SerializedName("next_analysis_datetime_utc")
    private String nextAnalysisDatetimeUtc;
    @SerializedName("predicted_action_datetime_utc")
    private String predictedActionDatetimeUtc;
    @SerializedName("status_key")
    private String statusKey;

    public GaugeValues getGaugeValues() {
        return gaugeValues;
    }

    public String getInstructionKey() {
        return instructionKey;
    }

    public String getNextAnalysisDatetimeUtc() {
        return nextAnalysisDatetimeUtc;
    }

    public String getPredictedActionDatetimeUtc() {
        return predictedActionDatetimeUtc;
    }

    public String getStatusKey() {
        return statusKey;
    }

    public void setGaugeValues(GaugeValues gaugeValues) {
        this.gaugeValues = gaugeValues;
    }

    public void setInstructionKey(String instructionKey) {
        this.instructionKey = instructionKey;
    }

    public void setNextAnalysisDatetimeUtc(String nextAnalysisDatetimeUtc) {
        this.nextAnalysisDatetimeUtc = nextAnalysisDatetimeUtc;
    }

    public void setPredictedActionDatetimeUtc(String predictedActionDatetimeUtc) {
        this.predictedActionDatetimeUtc = predictedActionDatetimeUtc;
    }

    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

}