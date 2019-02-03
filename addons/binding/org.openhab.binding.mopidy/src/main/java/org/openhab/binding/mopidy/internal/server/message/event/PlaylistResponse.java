package org.openhab.binding.mopidy.internal.server.message.event;

import org.openhab.binding.mopidy.internal.server.message.data.Playlist;

public class PlaylistResponse extends EventMessage {

    private Playlist playlist;
    private boolean playTracks;

    public PlaylistResponse(Playlist playlist, boolean playTracks) {
        super();
        this.playlist = playlist;
        this.playTracks = playTracks;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public boolean isPlayTracks() {
        return playTracks;
    }
}
