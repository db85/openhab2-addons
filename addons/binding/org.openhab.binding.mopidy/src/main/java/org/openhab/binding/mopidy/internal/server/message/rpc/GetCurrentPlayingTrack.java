package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class GetCurrentPlayingTrack extends RpcMessage {
    @SerializedName("method")
    private String method = "core.playback.get_current_tl_track";

    public String getMethod() {
        return method;
    }
}
