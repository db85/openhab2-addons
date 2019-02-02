package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class GetVolumeMessage extends RpcMessage {

    @SerializedName("method")
    private String method = "core.mixer.get_volume";

    public String getMethod() {
        return method;
    }
}
