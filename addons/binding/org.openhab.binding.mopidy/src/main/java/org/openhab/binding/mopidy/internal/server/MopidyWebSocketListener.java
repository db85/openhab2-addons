package org.openhab.binding.mopidy.internal.server;

import org.openhab.binding.mopidy.internal.server.message.event.EventMessage;
import org.openhab.binding.mopidy.internal.server.message.event.MuteChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.OptionsChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlayerStateChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlaylistChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlaylistDeletedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlaylistsLoadedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.SeekedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.StreamTitleChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackEnded;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackPaused;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackResumed;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackStarted;
import org.openhab.binding.mopidy.internal.server.message.event.TracklistChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.VolumeChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.rpc.ResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * WebSocket Listener
 *
 * @author Daniel Bauer - Initial contribution
 */
public class MopidyWebSocketListener extends WebSocketListener {

    private Logger logger = LoggerFactory.getLogger(MopidyWebSocketListener.class);
    private BehaviorSubject<EventMessage> messageSubject = BehaviorSubject.create();
    private BehaviorSubject<ResultMessage> rpcResultSubject = BehaviorSubject.create();

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
    private Gson defaultGson = new GsonBuilder().create();

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        logger.info("webSocket open");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        logger.debug("webSocket onMessage: {}", text);

        if (text.startsWith("{\"jsonrpc\"")) {
            ResultMessage resultMessage = defaultGson.fromJson(text, ResultMessage.class);
            rpcResultSubject.onNext(resultMessage);
        } else {
            EventMessage event = eventGson.fromJson(text, EventMessage.class);
            logger.info("event {} recived", event.getEvent());
            messageSubject.onNext(event);
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        logger.info("webSocket closed {}, {}", code, reason);
        messageSubject.onComplete();
        rpcResultSubject.onComplete();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {
        logger.error("webSocket failed", throwable);
        messageSubject.onError(throwable);
        rpcResultSubject.onError(throwable);
    }

    public Observable<EventMessage> observeEventMessages() {
        return messageSubject;
    }

    Observable<ResultMessage> observeRpcResultMessages() {
        return rpcResultSubject;
    }
}
