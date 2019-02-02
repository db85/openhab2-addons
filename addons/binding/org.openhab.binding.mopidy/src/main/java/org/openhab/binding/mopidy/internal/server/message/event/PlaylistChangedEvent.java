package org.openhab.binding.mopidy.internal.server.message.event;

import org.openhab.binding.mopidy.internal.server.message.data.Playlist;

import com.google.gson.annotations.SerializedName;

public class PlaylistChangedEvent extends EventMessage {
    @SerializedName("playlist")
    private Playlist playlist;

    public Playlist getPlaylist() {
        return playlist;
    }
}
