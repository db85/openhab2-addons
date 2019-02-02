package org.openhab.binding.mopidy.internal.server.message.data;

import com.google.gson.annotations.SerializedName;

public class Artist {

    @SerializedName("__model__")
    private String model;
    @SerializedName("name")
    private String name;
    @SerializedName("uri")
    private String uri;

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}
