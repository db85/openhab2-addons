package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class MuteMessage extends RpcMessage {

    class MutePara {
        @SerializedName("mute")
        private Boolean mute;

        public MutePara(Boolean mute) {
            this.mute = mute;
        }
    }

    @SerializedName("params")
    private MutePara params;

    public MuteMessage(Boolean mute) {
        params = new MutePara(mute);
    }

    @SerializedName("method")
    private String method = "core.mixer.set_mute";

    public String getMethod() {
        return method;
    }
}
