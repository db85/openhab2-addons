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
 * The {@link Watering} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class Watering {
    @SerializedName("automatic_watering")
    private AnalysisStatus automaticWatering;
    @SerializedName("instruction_key")
    private String instructionKey;
    @SerializedName("soil_moisture")
    private AnalysisStatus soilMoisture;
    @SerializedName("status_key")
    private String statusKey;

    public AnalysisStatus getAutomaticWatering() {
        return automaticWatering;
    }

    public String getInstructionKey() {
        return instructionKey;
    }

    public AnalysisStatus getSoilMoisture() {
        return soilMoisture;
    }

    public String getStatusKey() {
        return statusKey;
    }

    public void setAutomaticWatering(AnalysisStatus automaticWatering) {
        this.automaticWatering = automaticWatering;
    }

    public void setInstructionKey(String instructionKey) {
        this.instructionKey = instructionKey;
    }

    public void setSoilMoisture(AnalysisStatus soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public void setStatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

}
