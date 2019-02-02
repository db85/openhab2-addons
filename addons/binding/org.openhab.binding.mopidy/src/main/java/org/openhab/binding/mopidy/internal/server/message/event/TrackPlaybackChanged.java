package org.openhab.binding.mopidy.internal.server.message.event;

import org.openhab.binding.mopidy.internal.server.message.data.TLTrack;

import com.google.gson.annotations.SerializedName;

public abstract class TrackPlaybackChanged extends EventMessage {
    @SerializedName("time_position")
    private int timePosition;
    @SerializedName("tl_track")
    private TLTrack tl_track;

    public int getTimePosition() {
        return timePosition;
    }

    public TLTrack getTl_track() {
        return tl_track;
    }
}
