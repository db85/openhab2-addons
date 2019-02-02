// package org.openhab.binding.mopidy.internal.server;
//
// import java.io.IOException;
// import java.util.Map;
//
// import org.openhab.binding.mopidy.internal.server.message.event.EventMessage;
// import org.openhab.binding.mopidy.internal.server.message.event.MuteChangedEvent;
// import org.openhab.binding.mopidy.internal.server.message.event.OptionsChangedEvent;
// import org.openhab.binding.mopidy.internal.server.message.event.PlayerStateChangedEvent;
// import org.openhab.binding.mopidy.internal.server.message.event.PlaylistChangedEvent;
// import org.openhab.binding.mopidy.internal.server.message.event.PlaylistDeletedEvent;
// import org.openhab.binding.mopidy.internal.server.message.event.PlaylistsLoadedEvent;
// import org.openhab.binding.mopidy.internal.server.message.event.SeekedEvent;
// import org.openhab.binding.mopidy.internal.server.message.event.StreamTitleChangedEvent;
// import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackChanged;
// import org.openhab.binding.mopidy.internal.server.message.event.TracklistChangedEvent;
// import org.openhab.binding.mopidy.internal.server.message.event.VolumeChangedEvent;
//
// import com.squareup.moshi.FromJson;
// import com.squareup.moshi.JsonAdapter;
// import com.squareup.moshi.JsonDataException;
// import com.squareup.moshi.JsonReader;
// import com.squareup.moshi.JsonWriter;
// import com.squareup.moshi.ToJson;
//
// @SuppressWarnings("unchecked")
// public class EventMessageAdapter {
//
// @FromJson
// private EventMessage fromJson(JsonReader reader,
// JsonAdapter<PlayerStateChangedEvent> playerStateChangedEventAdapter,
// JsonAdapter<TrackPlaybackChanged> trackPlaybackChangedAdapter,
// JsonAdapter<VolumeChangedEvent> volumeChangedEventAdapter, JsonAdapter<SeekedEvent> seekedEventAdapter,
// JsonAdapter<MuteChangedEvent> muteChangedEvent, JsonAdapter<OptionsChangedEvent> optionsChangedEventAdapter,
// JsonAdapter<TracklistChangedEvent> tracklistChangedEventAdapter,
// JsonAdapter<PlaylistChangedEvent> playlistChangedEventAdapter,
// JsonAdapter<PlaylistDeletedEvent> playlistDeletedEventAdapter,
// JsonAdapter<PlaylistsLoadedEvent> playlistsLoadedEventAdapter,
// JsonAdapter<StreamTitleChangedEvent> streamTitleChangedEventAdapter) throws IOException {
//
// Object rawValue = reader.readJsonValue();
//
// String type = (String) ((Map<String, Object>) rawValue).get("event");
// switch (type) {
// case "playback_state_changed":
// return playerStateChangedEventAdapter.fromJsonValue(rawValue);
// case "track_playback_paused":
// case "track_playback_resumed":
// case "track_playback_started":
// case "track_playback_ended":
// return trackPlaybackChangedAdapter.fromJsonValue(rawValue);
// case "seeked":
// return seekedEventAdapter.fromJsonValue(rawValue);
// case "volume_changed":
// return volumeChangedEventAdapter.fromJsonValue(rawValue);
// case "mute_changed":
// return muteChangedEvent.fromJsonValue(rawValue);
// case "options_changed":
// return optionsChangedEventAdapter.fromJsonValue(rawValue);
// case "tracklist_changed":
// return tracklistChangedEventAdapter.fromJsonValue(rawValue);
// case "playlist_changed":
// return playlistChangedEventAdapter.fromJsonValue(rawValue);
// case "playlist_deleted":
// return playlistDeletedEventAdapter.fromJsonValue(rawValue);
// case "playlists_loaded":
// return playlistsLoadedEventAdapter.fromJsonValue(rawValue);
// case "stream_title_changed":
// return streamTitleChangedEventAdapter.fromJsonValue(rawValue);
// default:
// throw new JsonDataException("Unexpected type: " + type);
// }
// }
//
// @ToJson
// private void toJson(JsonWriter writer, EventMessage value) {
// throw new UnsupportedOperationException();
// }
// }
