package be.isach.samaritan.websocket;

import be.isach.samaritan.Samaritan;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.websocket
 * Created by: Sacha
 * Created on: 22th May, 2016
 * at 01:12 am
 * <p>
 * Description: WebSocket Server, for UI interface.
 */
public class SamaritanWebsocketServer extends WebSocketServer {

    private Samaritan samaritan;

    public SamaritanWebsocketServer(InetSocketAddress address, Samaritan samaritan) throws UnknownHostException {
        super(address);
        this.samaritan = samaritan;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        samaritan.getLogger().writeFrom("Web Socket Server", "Connection Opened from: " + conn.getLocalSocketAddress().toString());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        samaritan.getLogger().writeFrom("Web Socket Server","Connection Closed from: " + conn.getLocalSocketAddress().toString());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        samaritan.getLogger().writeFrom("Web Socket Server","Message received from: " + conn.getLocalSocketAddress().toString());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        samaritan.getLogger().writeFrom("Web Socket Server","Error: " + ex.getMessage());
    }

    public void send(String s) {
        for (WebSocket webSocket : connections()) {
            webSocket.send(s);
        }
    }
}
