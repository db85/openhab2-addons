package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class ResultMessage extends RpcMessage {
    @SerializedName("result")
    private Object result;

    public Object getResult() {
        return result;
    }
}
