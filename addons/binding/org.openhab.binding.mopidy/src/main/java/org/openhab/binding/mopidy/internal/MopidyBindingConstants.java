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
package org.openhab.binding.mopidy.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link MopidyBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Daniel Bauer - Initial contribution
 */
@NonNullByDefault
public class MopidyBindingConstants {

    private static final String BINDING_ID = "mopidy";

    public static final String CHANNEL_PLAYBACK_STATE = "playback_state";
    public static final String CHANNEL_PLAYBACK_TRACK_NAME = "playback_track_name";
    public static final String CHANNEL_PLAYBACK_TRACK_ALBUM = "playback_track_album";
    public static final String CHANNEL_PLAYBACK_VOLUME = "playback_volume";
    public static final String CHANNEL_PLAYBACK_VOLUME_MUTE = "playback_volume_mute";
    public static final String CHANNEL_PLAYER = "player";
    public static final String CHANNEL_PLAYLIST_NAME = "name";
    public static final String CHANNEL_PLAYLIST_LAST_MODIFIED = "last_modified";
    public static final String CHANNEL_PLAYLIST_PLAY = "play";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_SERVER = new ThingTypeUID(BINDING_ID, "server");
    public static final ThingTypeUID THING_TYPE_PLAYLIST = new ThingTypeUID(BINDING_ID, "playlist");
}
