package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class ClearTracklistMessage extends RpcMessage {
    @SerializedName("method")
    private String method = "core.tracklist.clear";

    public String getMethod() {
        return method;
    }
}
