package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class NextMessage extends RpcMessage {
    @SerializedName("method")
    private String method = "core.playback.next";

    public String getMethod() {
        return method;
    }
}
