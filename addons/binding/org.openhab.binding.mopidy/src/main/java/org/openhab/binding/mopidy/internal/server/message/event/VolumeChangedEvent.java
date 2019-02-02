package org.openhab.binding.mopidy.internal.server.message.event;

import com.google.gson.annotations.SerializedName;

public class VolumeChangedEvent extends EventMessage {
    @SerializedName("volume")
    private int volume;

    public VolumeChangedEvent() {

    }

    public VolumeChangedEvent(int volume) {
        super();
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }
}
