package org.openhab.binding.mopidy.internal.server.message.event;

import com.google.gson.annotations.SerializedName;

public class PlaylistDeletedEvent extends EventMessage {
    @SerializedName("uri")
    private String uri;

    public String getUri() {
        return uri;
    }
}
