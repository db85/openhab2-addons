package org.openhab.binding.mopidy.internal.handler;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.NextPreviousType;
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
import org.openhab.binding.mopidy.internal.server.message.data.TLTrack;
import org.openhab.binding.mopidy.internal.server.message.data.Track;
import org.openhab.binding.mopidy.internal.server.message.event.EventMessage;
import org.openhab.binding.mopidy.internal.server.message.event.MuteChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.PlayerStateChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.event.TrackPlaybackChanged;
import org.openhab.binding.mopidy.internal.server.message.event.VolumeChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetCurrentPlayingTrack;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetMuteMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetPlaybackStateMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetVolumeMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.NextMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PauseMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PlayMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PreviousMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.RpcMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.SetMuteMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.SetVolumeMessage;
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

        if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYER)) {
            if (PlayPauseType.PLAY.equals(command)) {
                sendMessage(new PlayMessage());
            } else if (PlayPauseType.PAUSE.equals(command)) {
                sendMessage(new PauseMessage());
            } else if (NextPreviousType.NEXT.equals(command)) {
                sendMessage(new NextMessage());
            } else if (NextPreviousType.PREVIOUS.equals(command)) {
                sendMessage(new PreviousMessage());
            } else if (command instanceof RefreshType) {
                sendMessage(new GetPlaybackStateMessage());
            }

        } else if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYBACK_VOLUME_MUTE)) {
            if (OnOffType.ON.equals(command)) {
                sendMessage(new SetMuteMessage(true));
            } else if (OnOffType.OFF.equals(command)) {
                sendMessage(new SetMuteMessage(false));
            } else if (command instanceof RefreshType) {
                sendMessage(new GetMuteMessage());
            }
        } else if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYBACK_VOLUME)) {
            if (command instanceof RefreshType) {
                sendMessage(new GetVolumeMessage());
            } else if (command instanceof DecimalType) {
                DecimalType value = (DecimalType) command;
                sendMessage(new SetVolumeMessage(value.intValue()));
            }
        } else if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYBACK_TRACK_NAME)) {
            if (command instanceof RefreshType) {
                sendMessage(new GetCurrentPlayingTrack());
            }
        }
    }

    public void sendMessage(RpcMessage rpcMessage) {
        compositeDisposable.add(connectionSubject.firstOrError().subscribe(connection -> {
            connection.sendMessage(rpcMessage);
        }, throwable -> {
            logger.error("send message failed", throwable);
        }));
    }

    public Observable<EventMessage> observeEventMessages() {
        return eventMessageSubject;
    }

    private void connectToServer() {
        String host = config.getHostname();
        int port = config.getPort();

        // serverConnectionRX = ServerConnection.create(getThing().getUID(), host, port)
        // .doOnNext(item -> updateOnlineState(true)).doOnError(e -> updateOnlineState(false))
        // .retryWhen(new RetryWithDelay(1, TimeUnit.MINUTES)).repeat().replay(1).refCount();
        //
        // Subscription serverUpdateSubscription = serverConnectionRX
        // .flatMap(connection -> connection.getSocketHandler().getServerRx())
        // .subscribe(serverData -> updateServerState(serverData));
        //
        // Subscription serverConnectionSubscription = serverConnectionRX.subscribe(connection -> {
        // this.connection = connection;
        // });
        //
        // subscription.add(serverUpdateSubscription);
        // subscription.add(serverConnectionSubscription);

        compositeDisposable.add(ServerConnection.create(getThing().getUID(), host, port).subscribe(connection -> {
            logger.info("connection created");
            updateStatus(ThingStatus.ONLINE);
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
            } else if (eventMessage instanceof VolumeChangedEvent) {
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_VOLUME,
                        new PercentType(((VolumeChangedEvent) eventMessage).getVolume()));
            } else if (eventMessage instanceof TrackPlaybackChanged) {
                TrackPlaybackChanged model = (TrackPlaybackChanged) eventMessage;
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_TRACK_NAME,
                        new StringType(getPlayBackNameFromEvent(model)));
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_TRACK_ALBUM,
                        new StringType(getPlayBackAlbumFromEvent(model)));
            } else if (eventMessage instanceof MuteChangedEvent) {
                updateState(MopidyBindingConstants.CHANNEL_PLAYBACK_VOLUME_MUTE,
                        OnOffType.from(((MuteChangedEvent) eventMessage).getMute()));
            } else {
                eventMessageSubject.onNext(eventMessage);
            }
        }, throwable -> {
            logger.info("observe event messages failed", throwable);
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
}
