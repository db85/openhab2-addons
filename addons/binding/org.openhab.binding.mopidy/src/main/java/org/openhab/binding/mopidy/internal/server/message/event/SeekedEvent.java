package org.openhab.binding.mopidy.internal.server.message.event;

import com.google.gson.annotations.SerializedName;

public class SeekedEvent extends EventMessage {
    @SerializedName("time_position")
    private int timePosition;

    public int getTimePosition() {
        return timePosition;
    }
}
