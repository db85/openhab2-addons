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

import java.util.List;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.library.types.PlayPauseType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.mopidy.internal.MopidyBindingConstants;
import org.openhab.binding.mopidy.internal.config.ServerConfig;
import org.openhab.binding.mopidy.internal.server.ServerConnection;
import org.openhab.binding.mopidy.internal.server.message.data.Album;
import org.openhab.binding.mopidy.internal.server.message.data.Artist;
import org.openhab.binding.mopidy.internal.server.message.data.TLTrack;
import org.openhab.binding.mopidy.internal.server.message.data.Track;
import org.openhab.binding.mopidy.internal.server.message.event.EventMessage;
import org.openhab.binding.mopidy.internal.server.message.event.MuteChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlayerStateChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.RepeatChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.SeekedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackChanged;
import org.openhab.binding.mopidy.internal.server.message.event.VolumeChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.rpc.RpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * The {@link MopidyServerHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Daniel Bauer - Initial contribution
 */
public class MopidyServerHandler extends BaseBridgeHandler {

    private static final String UNKOWN = "unkown";
    private Logger logger = LoggerFactory.getLogger(MopidyServerHandler.class);
    private ServerConfig config;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private BehaviorSubject<ServerConnection> connectionSubject = BehaviorSubject.create();
    private PublishSubject<EventMessage> eventMessageSubject = PublishSubject.create();
    private CommandRequestMapper commandMapper = new CommandRequestMapper();

    public MopidyServerHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void initialize() {
        logger.info("Initializing MopidyServerHandler");
        config = getConfigAs(ServerConfig.class);
        connectToServer();
    }

    @Override
    public void dispose() {
        logger.info("Disposing MopidyServerHandler");
        compositeDisposable.dispose();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.info("handleCommand {}, {}", channelUID.getAsString(), command.toFullString());
        RpcMessage message = commandMapper.transform(channelUID, command);
        if (message != null) {
            sendMessage(message);
        }
        if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYBACK_STOP)) {
            if (command instanceof RefreshType) {
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_STOP, OnOffType.OFF);
            } else if (OnOffType.ON.equals(command)) {
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_STOP, OnOffType.OFF);
            }
        }
    }

    public void sendMessage(RpcMessage rpcMessage) {
        compositeDisposable.add(connectionSubject.firstOrError().subscribe(connection -> {
            connection.sendMessage(rpcMessage);
        }, throwable -> {
            logger.error("send message failed: ", throwable);
        }));
    }

    public Observable<EventMessage> observeEventMessages() {
        return eventMessageSubject;
    }

    private void connectToServer() {
        String host = config.getHostname();
        int port = config.getPort();

        compositeDisposable.add(ServerConnection.create(getThing().getUID(), host, port).subscribe(connection -> {
            logger.info("connection created");
            connectionSubject.onNext(connection);
            connected(connection);
        }, throwable -> {
            logger.info("connection failed", throwable);
            updateStatus(ThingStatus.OFFLINE);
        }));
    }

    private void connected(ServerConnection connection) {
        compositeDisposable.add(connection.observeEventMessages().subscribe(eventMessage -> {
            if (eventMessage instanceof PlayerStateChangedEvent) {
                String state = ((PlayerStateChangedEvent) eventMessage).getNewState();
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_STATE, new StringType(state));
                if (state.equals("playing")) {
                    updateState(MopidyBindingConstants.CHANNEL_PLAYER, PlayPauseType.PLAY);
                } else {
                    updateState(MopidyBindingConstants.CHANNEL_PLAYER, PlayPauseType.PAUSE);
                }
                if (state.equals("stopped")) {
                    updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_PROGRESS, new DecimalType(0));
                }
            } else if (eventMessage instanceof VolumeChangedEvent) {
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_VOLUME,
                        new PercentType(((VolumeChangedEvent) eventMessage).getVolume()));
            } else if (eventMessage instanceof TrackPlaybackChanged) {
                TrackPlaybackChanged model = (TrackPlaybackChanged) eventMessage;
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_TRACK_NAME,
                        new StringType(getPlayBackNameFromEvent(model)));
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_TRACK_ALBUM,
                        new StringType(getPlayBackAlbumFromEvent(model)));
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_TRACK_ARTIST,
                        new StringType(getPlayBackArtistFromEvent(model)));
                long length = getPlayBackLengthFromEvent(model);
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_LENGHT, new DecimalType(length));
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_PROGRESS, new DecimalType(model.getTimePosition()));
            } else if (eventMessage instanceof MuteChangedEvent) {
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_VOLUME_MUTE,
                        OnOffType.from(((MuteChangedEvent) eventMessage).getMute()));
            } else if (eventMessage instanceof RepeatChangedEvent) {
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_REPEAT,
                        OnOffType.from(((RepeatChangedEvent) eventMessage).getRepeat()));
            } else if (eventMessage instanceof SeekedEvent) {
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_PROGRESS,
                        new DecimalType(((SeekedEvent) eventMessage).getTimePosition()));
            } else {
                eventMessageSubject.onNext(eventMessage);
            }
        }, throwable -> {
            logger.info("observe event messages failed: ", throwable);
            updateStatus(ThingStatus.OFFLINE);
        }));

        compositeDisposable.add(connection.observeConnectionStatus().subscribe(connectionStatus -> {
            if (connectionStatus == ServerConnection.ConnectionStatus.CONNECTED) {
                updateStatus(ThingStatus.ONLINE);
            } else if (connectionStatus == ServerConnection.ConnectionStatus.DISCONNECTED) {
                updateStatus(ThingStatus.OFFLINE);
            }
        }));
    }

    private String getPlayBackNameFromEvent(TrackPlaybackChanged event) {
        TLTrack tlTrack = event.getTl_track();
        if (tlTrack == null) {
            return UNKOWN;
        }
        Track track = tlTrack.getTrack();
        if (track == null) {
            return UNKOWN;
        }

        String name = track.getName();
        if (name != null) {
            return name;
        }
        String uri = track.getUri();
        if (uri != null) {
            return uri;
        }
        return UNKOWN;
    }

    private String getPlayBackAlbumFromEvent(TrackPlaybackChanged event) {
        TLTrack tlTrack = event.getTl_track();
        if (tlTrack == null) {
            return UNKOWN;
        }
        Track track = tlTrack.getTrack();
        if (track == null) {
            return UNKOWN;
        }

        Album album = track.getAlbum();
        if (album == null) {
            return "";
        }

        String name = album.getName();
        if (name != null) {
            return name;
        }
        return "";
    }

    private String getPlayBackArtistFromEvent(TrackPlaybackChanged event) {
        TLTrack tlTrack = event.getTl_track();
        if (tlTrack == null) {
            return UNKOWN;
        }
        Track track = tlTrack.getTrack();
        if (track == null) {
            return UNKOWN;
        }

        List<Artist> artists = track.getArtists();
        if (artists == null || artists.isEmpty()) {
            return UNKOWN;
        }
        return artists.get(0).getName();
    }

    private long getPlayBackLengthFromEvent(TrackPlaybackChanged event) {
        TLTrack tlTrack = event.getTl_track();
        if (tlTrack == null) {
            return 0;
        }
        Track track = tlTrack.getTrack();
        if (track == null) {
            return 0;
        }

        return track.getLength();
    }

    // private void stopPlackbackProgressTimer() {
    // if (plackbackProgressDisposable != null && !plackbackProgressDisposable.isDisposed()) {
    // plackbackProgressDisposable.dispose();
    // }
    // currentTimerEnd = 0;
    // }

    // private void startPlackbackProgressTimer(long start, long end) {
    // stopPlackbackProgressTimer();
    // currentTimerEnd = end;
    // plackbackProgressDisposable = Observable.intervalRange(start, end - start, 0L, 1000L, TimeUnit.MILLISECONDS)
    // .subscribe(time -> {
    // updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_PROGRESS, new DecimalType(time));
    // }, throwable -> {
    // logger.error("timer failed", throwable);
    // });
    // }
}
