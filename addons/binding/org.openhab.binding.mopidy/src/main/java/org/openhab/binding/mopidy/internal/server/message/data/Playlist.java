package org.openhab.binding.mopidy.internal.server.message.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Playlist {
    @SerializedName("__model__")
    private String model;
    @SerializedName("last_modified")
    private long lastModified;
    @SerializedName("name")
    private String name;
    @SerializedName("uri")
    private String uri;
    @SerializedName("tracks")
    private List<Track> tracks;

    public String getModel() {
        return model;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public List<Track> getTracks() {
        return tracks;
    }
}