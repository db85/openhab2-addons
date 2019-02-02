package org.openhab.binding.mopidy.internal.server.message.event;

import com.google.gson.annotations.SerializedName;

public abstract class EventMessage {
    @SerializedName("event")
    private String event;

    public String getEvent() {
        return event;
    }
}