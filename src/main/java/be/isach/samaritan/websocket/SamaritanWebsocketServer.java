package be.isach.samaritan.websocket;

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
 *
 * Description: WebSocket Server, for UI interface.
 */
public class SamaritanWebsocketServer extends WebSocketServer {

    public SamaritanWebsocketServer(InetSocketAddress address) throws UnknownHostException {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("WEB_SOCKET: opened.");
    }

    public void send(String s) {
        for(WebSocket webSocket : connections()) {
            webSocket.send(s);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("WEB_SOCKET: closed.");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("WEB_SOCKET: message received: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("WEB_SOCKET: error received: " + ex.getMessage());
    }
}
