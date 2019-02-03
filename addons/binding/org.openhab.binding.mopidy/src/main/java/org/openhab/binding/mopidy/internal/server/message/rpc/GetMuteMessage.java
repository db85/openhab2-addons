package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class GetMuteMessage extends RpcMessage {
    @SerializedName("method")
    private String method = "core.mixer.get_mute";

    public String getMethod() {
        return method;
    }
}
