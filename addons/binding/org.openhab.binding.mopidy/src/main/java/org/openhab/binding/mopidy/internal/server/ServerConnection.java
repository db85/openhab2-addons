/**
 *
 */
package org.openhab.binding.mopidy.internal.server;

import java.util.HashMap;

import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.mopidy.internal.server.message.event.EventMessage;
import org.openhab.binding.mopidy.internal.server.message.event.VolumeChangedEvent;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetVolumeMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.RpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

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
                listener.observeRpcResultMessages().flatMap(rpcResult -> {
                    RpcMessage outgoingMessage = messages.get(rpcResult.getId());
                    if (outgoingMessage instanceof GetVolumeMessage) {
                        return Observable.just(new VolumeChangedEvent(((Double) rpcResult.getResult()).intValue()));
                    }
                    return Observable.empty();
                }));
    }

    public void sendMessage(RpcMessage rpcMessage) {
        long id = messageId++;
        rpcMessage.setId(id);

        messages.put(id, rpcMessage);

        String json = gson.toJson(rpcMessage);
        logger.info("send message: {}", json);
        webSocket.send(json);
    }
}
