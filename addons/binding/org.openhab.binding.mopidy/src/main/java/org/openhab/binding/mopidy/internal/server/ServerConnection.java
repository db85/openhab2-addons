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
package org.openhab.binding.mopidy.internal.server;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.mopidy.internal.server.message.data.Playlist;
import org.openhab.binding.mopidy.internal.server.message.data.TLTrack;
import org.openhab.binding.mopidy.internal.server.message.event.EventMessage;
import org.openhab.binding.mopidy.internal.server.message.event.MuteChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.OptionsChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlayerStateChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlaylistChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlaylistDeletedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlaylistResponse;
import org.openhab.binding.mopidy.internal.server.message.event.PlaylistsLoadedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.RepeatChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.SeekedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.StreamTitleChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackChanged;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackEnded;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackPaused;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackResumed;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackStarted;
import org.openhab.binding.mopidy.internal.server.message.event.TracklistChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.VolumeChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetCurrentPlayingTrack;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetMuteMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetPlaybackStateMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetRepeatMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetVolumeMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PlaylistLookupMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.ResultMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.RpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Holds information about connection to Mopidy server.
 *
 * @author Daniel Bauer - Initial contribution
 */
public class ServerConnection {

    public enum ConnectionStatus {
        DISCONNECTED,
        CONNECTED,
        CONNECTING
    }

    public static Observable<ServerConnection> create(final ThingUID thingUID, final String host, final int port) {

        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            ServerConnection serverConnection = new ServerConnection(thingUID, host, port);
            serverConnection.reconnect();
            emitter.setDisposable(Disposables.fromRunnable(() -> {
                serverConnection.disconnect();
            }));
            emitter.onNext(serverConnection);
        });
    }

    private BehaviorSubject<EventMessage> messageSubject = BehaviorSubject.create();
    private BehaviorSubject<String> rpcResultSubject = BehaviorSubject.create();
    private BehaviorSubject<ConnectionStatus> connectionStatusSubject = BehaviorSubject.create();

    private Logger logger = LoggerFactory.getLogger(ServerConnection.class);
    private Gson gson = new Gson();
    private String host;
    private int port;
    private ThingUID thingUID;
    private WebSocket webSocket;
    private Request request;
    private long messageId = 1;
    private OkHttpClient httpClient;
    private ConnectionStatus status = ConnectionStatus.DISCONNECTED;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private WebSocketListener socketListener = new WebSocketListener() {
        private RuntimeTypeAdapterFactory<EventMessage> adapterFactory = RuntimeTypeAdapterFactory
                .of(EventMessage.class, "event").registerSubtype(MuteChangedEvent.class, "mute_changed")
                .registerSubtype(PlayerStateChangedEvent.class, "playback_state_changed")
                .registerSubtype(VolumeChangedEvent.class, "volume_changed")
                .registerSubtype(OptionsChangedEvent.class, "options_changed")
                .registerSubtype(TracklistChangedEvent.class, "tracklist_changed")
                .registerSubtype(PlaylistChangedEvent.class, "playlist_changed")
                .registerSubtype(PlaylistDeletedEvent.class, "playlist_deleted")
                .registerSubtype(PlaylistsLoadedEvent.class, "playlists_loaded")
                .registerSubtype(StreamTitleChangedEvent.class, "stream_title_changed")
                .registerSubtype(TrackPlaybackPaused.class, "track_playback_paused")
                .registerSubtype(TrackPlaybackResumed.class, "track_playback_resumed")
                .registerSubtype(TrackPlaybackStarted.class, "track_playback_started")
                .registerSubtype(TrackPlaybackEnded.class, "track_playback_ended")
                .registerSubtype(SeekedEvent.class, "seeked");

        private Gson eventGson = new GsonBuilder().registerTypeAdapterFactory(adapterFactory).create();

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            logger.info("webSocket closed {}, {}", code, reason);
            if (status != ConnectionStatus.CONNECTED) {
                reconnect();
            }
            updateConnectionStatus(ConnectionStatus.DISCONNECTED);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
            logger.error("webSocket failed: {} {}", status, throwable);
            if (status == ConnectionStatus.CONNECTED) {
                reconnect();
            }
            updateConnectionStatus(ConnectionStatus.DISCONNECTED);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            logger.debug("webSocket onMessage: {}", text);

            if (text.startsWith("{\"jsonrpc\"")) {
                rpcResultSubject.onNext(text);
            } else {
                EventMessage event = eventGson.fromJson(text, EventMessage.class);
                logger.info("event {} recived", event.getEvent());
                messageSubject.onNext(event);
            }
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            logger.info("webSocket connected");
            ServerConnection.this.webSocket = webSocket;
            updateConnectionStatus(ConnectionStatus.CONNECTED);
        }
    };

    private HashMap<Long, RpcMessage> messages = new HashMap<Long, RpcMessage>();

    private ServerConnection(ThingUID thingUID, String host, int port) {
        this.host = host;
        this.port = port;
        this.thingUID = thingUID;
    }

    private synchronized void connect() {
        if (status == ConnectionStatus.CONNECTING) {
            return;
        }
        if (webSocket != null) {
            return;
        }

        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder().readTimeout(2, TimeUnit.SECONDS).retryOnConnectionFailure(true)
                    .build();
        }
        if (request == null) {
            final String serverUrl = String.format("ws://%s:%d/mopidy/ws", host, port);
            // String serverUrl = String.format("ws://127.0.0.1:8860");
            request = new Request.Builder().url(serverUrl).build();
        }
        updateConnectionStatus(ConnectionStatus.CONNECTING);
        httpClient.dispatcher().cancelAll();
        httpClient.newWebSocket(request, socketListener);
    }

    public void disconnect() {
        compositeDisposable.dispose();
        updateConnectionStatus(ConnectionStatus.DISCONNECTED);
        if (httpClient != null) {
            httpClient.dispatcher().cancelAll();
        }
        if (webSocket != null) {
            webSocket.cancel();
            webSocket = null;
        }
    }

    private <T> ResultMessage<T> fromJson(String text, Class<T> typeoff) {
        Type type = TypeToken.getParameterized(ResultMessage.class, typeoff).getType();
        return gson.fromJson(text, type);
    }

    public Observable<ConnectionStatus> observeConnectionStatus() {
        return connectionStatusSubject;
    }

    public Observable<EventMessage> observeEventMessages() {
        return Observable.merge(messageSubject, rpcResultSubject.flatMap(rpcResultJson -> {
            RpcMessage rpcMessage = gson.fromJson(rpcResultJson, RpcMessage.class);
            RpcMessage outgoingMessage = messages.get(rpcMessage.getId());

            if (outgoingMessage instanceof GetVolumeMessage) {
                return Observable
                        .just(new VolumeChangedEvent(fromJson(rpcResultJson, Double.class).getResult().intValue()));
            } else if (outgoingMessage instanceof GetPlaybackStateMessage) {
                return Observable
                        .just(new PlayerStateChangedEvent("", fromJson(rpcResultJson, String.class).getResult()));
            } else if (outgoingMessage instanceof GetMuteMessage) {
                return Observable.just(new MuteChangedEvent(fromJson(rpcResultJson, Boolean.class).getResult()));
            } else if (outgoingMessage instanceof GetCurrentPlayingTrack) {
                return Observable.just(new TrackPlaybackChanged(fromJson(rpcResultJson, TLTrack.class).getResult()));
            } else if (outgoingMessage instanceof PlaylistLookupMessage) {
                PlaylistLookupMessage lookupMessage = (PlaylistLookupMessage) outgoingMessage;
                return Observable.just(new PlaylistResponse(fromJson(rpcResultJson, Playlist.class).getResult(),
                        lookupMessage.isPlayTracks()));
            } else if (outgoingMessage instanceof GetRepeatMessage) {
                return Observable.just(new RepeatChangedEvent(fromJson(rpcResultJson, Boolean.class).getResult()));
            }

            return Observable.empty();
        }));
    }

    private void reconnect() {
        webSocket = null;
        compositeDisposable.add(Observable.interval(15, TimeUnit.SECONDS)
                .takeWhile((val) -> status != ConnectionStatus.CONNECTED).subscribe(time -> {
                    logger.error("try to reconnect");
                    connect();
                }, throwable -> {
                    logger.error("timer failed: ", throwable);
                }));

    }

    public synchronized void sendMessage(RpcMessage rpcMessage) {
        if (status != ConnectionStatus.CONNECTED) {
            logger.info("send message failed: not connected");
        }

        long id = messageId++;
        rpcMessage.setId(id);

        messages.put(id, rpcMessage);

        String json = gson.toJson(rpcMessage);
        logger.info("send message: {}", json);
        if (webSocket != null && !webSocket.send(json)) {
            logger.error("send message failed: {}", json);
        }
    }

    private void updateConnectionStatus(ConnectionStatus status) {
        boolean changed = this.status != status;
        this.status = status;
        if (changed) {
            connectionStatusSubject.onNext(status);
        }

    }
}
