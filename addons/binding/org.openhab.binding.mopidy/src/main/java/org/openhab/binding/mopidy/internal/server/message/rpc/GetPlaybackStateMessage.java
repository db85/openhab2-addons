package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class GetPlaybackStateMessage extends RpcMessage {
    @SerializedName("method")
    private String method = "core.playback.get_state";

    public String getMethod() {
        return method;
    }
}
