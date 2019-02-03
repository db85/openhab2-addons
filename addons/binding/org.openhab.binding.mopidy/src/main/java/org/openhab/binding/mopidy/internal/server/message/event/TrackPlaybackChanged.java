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

import org.openhab.binding.mopidy.internal.server.message.data.TLTrack;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link TrackPlaybackChanged} is a json model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class TrackPlaybackChanged extends EventMessage {
    @SerializedName("time_position")
    private int timePosition;
    @SerializedName("tl_track")
    private TLTrack tl_track;

    public TrackPlaybackChanged() {
        super();
    }

    public TrackPlaybackChanged(TLTrack tl_track) {
        super();
        this.tl_track = tl_track;
    }

    public int getTimePosition() {
        return timePosition;
    }

    public TLTrack getTl_track() {
        return tl_track;
    }
}
