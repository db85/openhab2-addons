package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

public class PlaylistLookupMessage extends RpcMessage {

    class LookupPara {
        @SerializedName("uri")
        private String uri;

        public LookupPara(String uri) {
            this.uri = uri;
        }
    }

    public PlaylistLookupMessage(String uri, boolean playTracks) {
        this.params = new LookupPara(uri);
        this.playTracks = playTracks;
    }

    @SerializedName("params")
    private LookupPara params;

    @SerializedName("method")
    private String method = "core.playlists.lookup";

    private transient boolean playTracks = false;

    public String getMethod() {
        return method;
    }

    public boolean isPlayTracks() {
        return playTracks;
    }
}
