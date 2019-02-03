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
package org.openhab.binding.mopidy.internal.server.message.rpc;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link PlaylistLookupMessage} is a json model
 *
 * @author Daniel Bauer - Initial contribution
 */
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
