package org.openhab.binding.mopidy.internal.server.message.data;

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
}