package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class RpcMessage {
    @SerializedName("jsonrpc")
    private String jsonrpc = "2.0";
    @SerializedName("id")
    private long id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
