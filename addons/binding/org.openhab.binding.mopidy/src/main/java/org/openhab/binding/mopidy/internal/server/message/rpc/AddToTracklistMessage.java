package org.openhab.binding.mopidy.internal.server.message.rpc;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AddToTracklistMessage extends RpcMessage {

    class AddToTracklistPara {
        @SerializedName("uris")
        private List<String> uris;

        public AddToTracklistPara(List<String> uris) {
            this.uris = uris;
        }
    }

    @SerializedName("params")
    private AddToTracklistPara params;

    public AddToTracklistMessage(List<String> uris) {
        params = new AddToTracklistPara(uris);
    }

    @SerializedName("method")
    private String method = "core.tracklist.add";

    public String getMethod() {
        return method;
    }
}
