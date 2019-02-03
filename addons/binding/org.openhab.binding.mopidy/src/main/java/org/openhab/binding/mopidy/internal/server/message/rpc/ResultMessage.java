package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class ResultMessage<T> extends RpcMessage {
    @SerializedName("result")
    private T result;

    public T getResult() {
        return result;
    }
}
