package org.openhab.binding.mopidy.internal.server.message.event;

import com.google.gson.annotations.SerializedName;

public class MuteChangedEvent extends EventMessage {
    @SerializedName("mute")
    private Boolean mute;

    public Boolean getMute() {
        return mute;
    }

    public void setMute(Boolean mute) {
        this.mute = mute;
    }
}
