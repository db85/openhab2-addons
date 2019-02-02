package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class SetVolumeMessage extends RpcMessage {

    class VolumePara {
        @SerializedName("volume")
        private int volume;

        public VolumePara(int volume) {
            this.volume = volume;
        }
    }

    @SerializedName("params")
    private VolumePara params;

    public SetVolumeMessage(int volume) {
        params = new VolumePara(volume);
    }

    @SerializedName("method")
    private String method = "core.mixer.set_volume";

    public String getMethod() {
        return method;
    }
}
