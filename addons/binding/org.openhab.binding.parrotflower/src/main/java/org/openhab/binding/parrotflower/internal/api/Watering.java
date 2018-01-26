package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

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
