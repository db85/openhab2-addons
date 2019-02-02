package org.openhab.binding.mopidy.internal.server.message.data;

import com.google.gson.annotations.SerializedName;

public class TLTrack {
    @SerializedName("__model__")
    private String model;
    @SerializedName("tlid")
    private int tlid;
    @SerializedName("track")
    private Track track;

    public String getModel() {
        return model;
    }

    public int getTlid() {
        return tlid;
    }

    public Track getTrack() {
        return track;
    }
}
