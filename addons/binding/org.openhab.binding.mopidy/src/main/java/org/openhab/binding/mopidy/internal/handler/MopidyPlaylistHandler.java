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
package org.openhab.binding.mopidy.internal.handler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.mopidy.internal.MopidyBindingConstants;
import org.openhab.binding.mopidy.internal.config.PlaylistConfig;
import org.openhab.binding.mopidy.internal.server.message.data.Playlist;
import org.openhab.binding.mopidy.internal.server.message.data.Track;
import org.openhab.binding.mopidy.internal.server.message.event.PlaylistResponse;
import org.openhab.binding.mopidy.internal.server.message.rpc.AddToTracklistMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.ClearTracklistMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PlayMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PlaylistLookupMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.CompositeDisposable;

/**
 * The {@link MopidyPlaylistHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Daniel Bauer - Initial contribution
 */
public class MopidyPlaylistHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(MopidyPlaylistHandler.class);
    private PlaylistConfig config;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MopidyPlaylistHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.ONLINE);
        config = getConfigAs(PlaylistConfig.class);
        MopidyServerHandler serverHandler = getServerHandler();
        if (serverHandler == null) {
            return;
        }
        compositeDisposable.add(serverHandler.observeEventMessages().subscribe(eventMessage -> {
            if (eventMessage instanceof PlaylistResponse) {
                PlaylistResponse response = (PlaylistResponse) eventMessage;
                Playlist playList = response.getPlaylist();
                if (playList == null) {
                    updateStatus(ThingStatus.OFFLINE);
                    return;
                }
                updateState(MopidyBindingConstants.CHANNEL_PLAYLIST_NAME, new StringType(playList.getName()));

                ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(playList.getLastModified()),
                        ZoneId.of("UTC"));
                updateState(MopidyBindingConstants.CHANNEL_PLAYLIST_LAST_MODIFIED, new DateTimeType(zonedDateTime));

                if (response.isPlayTracks()) {
                    ArrayList<String> uris = new ArrayList<String>();
                    for (Track track : playList.getTracks()) {
                        uris.add(track.getUri());
                    }

                    updateState(MopidyBindingConstants.CHANNEL_PLAYLIST_PLAY, OnOffType.OFF);
                    serverHandler.sendMessage(new ClearTracklistMessage());
                    serverHandler.sendMessage(new AddToTracklistMessage(uris));
                    serverHandler.sendMessage(new PlayMessage());
                }
            }
        }, throwable -> {
            logger.error("observe event messages failed", throwable);
        }));
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        MopidyServerHandler serverHandler = getServerHandler();
        if (serverHandler == null) {
            return;
        }

        if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYLIST_NAME)) {
            if (command instanceof RefreshType) {
                serverHandler.sendMessage(new PlaylistLookupMessage(config.getUri(), false));
            }
        } else if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYLIST_PLAY)) {
            if (command instanceof RefreshType) {
                updateState(MopidyBindingConstants.CHANNEL_PLAYLIST_PLAY, OnOffType.OFF);
            } else if (OnOffType.ON.equals(command)) {
                serverHandler.sendMessage(new PlaylistLookupMessage(config.getUri(), true));
            }
        }
    }

    @Override
    public void dispose() {
        logger.info("Disposing MopidyPlaylistHandler");
        compositeDisposable.dispose();
    }

    private MopidyServerHandler getServerHandler() {
        Bridge bridge = getBridge();
        if (bridge == null) {
            return null;
        }
        return (MopidyServerHandler) bridge.getHandler();
    }
}
