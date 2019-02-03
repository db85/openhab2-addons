package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class PauseMessage extends RpcMessage {
    @SerializedName("method")
    private String method = "core.playback.pause";

    public String getMethod() {
        return method;
    }
}
