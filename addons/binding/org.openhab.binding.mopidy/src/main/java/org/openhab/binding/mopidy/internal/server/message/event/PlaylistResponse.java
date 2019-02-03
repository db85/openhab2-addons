/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
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
