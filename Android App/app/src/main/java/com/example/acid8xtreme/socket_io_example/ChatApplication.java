package com.example.acid8xtreme.socket_io_example;

import android.app.Application;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class ChatApplication extends Application {

    public Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.SOCKET_IO_SERVER);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
