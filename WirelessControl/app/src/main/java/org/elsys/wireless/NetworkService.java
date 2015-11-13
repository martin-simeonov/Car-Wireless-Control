package org.elsys.wireless;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkService {

    public static final String BASE_URL = "192.168.240.1";
    public static final int BASE_PORT = 6666;
    private Socket server;
    private OutputStream serverOutput;
    private NetworkCallback connectedCallback;

    public void connect(NetworkCallback connectedCallback) {
        this.connectedCallback = connectedCallback;
        Thread netThread = new Thread(new NetworkThread());
        netThread.start();
    }

    public void disconnect() {
        try {
            if (server != null)
                server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface NetworkCallback {
        void onSuccess();
        void onFailure(String message);
    }

    public class NetworkThread implements Runnable {

        public void run() {
            try {
                server = new Socket(BASE_URL, BASE_PORT);
                server.setTcpNoDelay(true);
                serverOutput = server.getOutputStream();
                if (server.isConnected()) {
                    connectedCallback.onSuccess();
                } else {
                    connectedCallback.onFailure("Connection error");
                }
            } catch (IOException e) {
                e.printStackTrace();
                connectedCallback.onFailure("Connection error");
            }
        }

    }

    public boolean isConnected() {
        if (server != null)
            return server.isConnected();
        return false;
    }

    public void write(String command) {
        try {
            serverOutput.write(command.getBytes());
            serverOutput.flush();;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
