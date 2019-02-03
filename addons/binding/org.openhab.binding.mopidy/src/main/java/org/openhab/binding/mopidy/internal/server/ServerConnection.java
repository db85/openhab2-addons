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

import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.mopidy.internal.server.message.data.Playlist;
import org.openhab.binding.mopidy.internal.server.message.data.TLTrack;
import org.openhab.binding.mopidy.internal.server.message.event.EventMessage;
import org.openhab.binding.mopidy.internal.server.message.event.MuteChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlayerStateChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlaylistResponse;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackChanged;
import org.openhab.binding.mopidy.internal.server.message.event.VolumeChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetCurrentPlayingTrack;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetMuteMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetPlaybackStateMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetVolumeMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PlaylistLookupMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.ResultMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.RpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposables;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * Holds information about connection to Mopidy server.
 *
 * @author Daniel Bauer - Initial contribution
 */
public class ServerConnection {

    private Logger logger = LoggerFactory.getLogger(ServerConnection.class);
    private Gson gson = new Gson();
    private String host;
    private int port;
    private ThingUID thingUID;
    private MopidyWebSocketListener listener;
    private WebSocket webSocket;
    private long messageId = 1;
    private HashMap<Long, RpcMessage> messages = new HashMap<Long, RpcMessage>();

    private ServerConnection(ThingUID thingUID, String host, int port) {
        this.host = host;
        this.port = port;
        this.thingUID = thingUID;
    }

    public static Observable<ServerConnection> create(final ThingUID thingUID, final String host, final int port) {
        final String serverUrl = String.format("ws://%s:%d/mopidy/ws", host, port);

        return Observable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            final OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(serverUrl).build();
            MopidyWebSocketListener listener = new MopidyWebSocketListener();
            final WebSocket webSocket = client.newWebSocket(request, listener);

            ServerConnection serverConnection = new ServerConnection(thingUID, host, port);
            serverConnection.setWebSocketListener(listener);
            serverConnection.setWebSocket(webSocket);

            emitter.setDisposable(Disposables.fromRunnable(() -> {
                webSocket.cancel();
            }));
            emitter.onNext(serverConnection);
        });
    }

    private void setWebSocketListener(MopidyWebSocketListener listener) {
        this.listener = listener;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public Observable<EventMessage> observeEventMessages() {
        return Observable.merge(listener.observeEventMessages(),
                listener.observeRpcResultMessages().flatMap(rpcResultJson -> {
                    RpcMessage rpcMessage = gson.fromJson(rpcResultJson, RpcMessage.class);
                    RpcMessage outgoingMessage = messages.get(rpcMessage.getId());

                    if (outgoingMessage instanceof GetVolumeMessage) {
                        return Observable.just(
                                new VolumeChangedEvent(fromJson(rpcResultJson, Double.class).getResult().intValue()));
                    } else if (outgoingMessage instanceof GetPlaybackStateMessage) {
                        return Observable.just(
                                new PlayerStateChangedEvent("", fromJson(rpcResultJson, String.class).getResult()));
                    } else if (outgoingMessage instanceof GetMuteMessage) {
                        return Observable
                                .just(new MuteChangedEvent(fromJson(rpcResultJson, Boolean.class).getResult()));
                    } else if (outgoingMessage instanceof GetCurrentPlayingTrack) {
                        return Observable
                                .just(new TrackPlaybackChanged(fromJson(rpcResultJson, TLTrack.class).getResult()));
                    } else if (outgoingMessage instanceof PlaylistLookupMessage) {
                        PlaylistLookupMessage lookupMessage = (PlaylistLookupMessage) outgoingMessage;
                        return Observable.just(new PlaylistResponse(fromJson(rpcResultJson, Playlist.class).getResult(),
                                lookupMessage.isPlayTracks()));
                    }

                    return Observable.empty();
                }));
    }

    private <T> ResultMessage<T> fromJson(String text, Class<T> typeoff) {
        Type type = TypeToken.getParameterized(ResultMessage.class, typeoff).getType();
        return gson.fromJson(text, type);
    }

    public synchronized void sendMessage(RpcMessage rpcMessage) {
        long id = messageId++;
        rpcMessage.setId(id);

        messages.put(id, rpcMessage);

        String json = gson.toJson(rpcMessage);
        logger.info("send message: {}", json);
        webSocket.send(json);
    }
}
