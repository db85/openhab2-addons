package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class PreviousMessage extends RpcMessage {
    @SerializedName("method")
    private String method = "core.playback.previous";

    public String getMethod() {
        return method;
    }
}
