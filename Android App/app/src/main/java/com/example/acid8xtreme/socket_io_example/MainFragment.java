package com.example.acid8xtreme.socket_io_example;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainFragment extends Fragment {

    public static Socket mSocket;
    private static Handler mHandler;
    public static int[] ids;

    public static MainFragment newInstance(Handler handler) {
        mHandler = handler;
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatApplication app = (ChatApplication) getActivity().getApplication();
        mSocket = app.getSocket();
        mSocket.on("completeItem", onCompleteItem);
        mSocket.on("stackOnly", onStackOnly);
        mSocket.on("playerInfo", onPlayerInfo);
        mSocket.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("completeItem", onCompleteItem);
        mSocket.off("stackOnly", onStackOnly);
        mSocket.off("playerInfo", onPlayerInfo);
    }

    public void attemptSend(String type, String message) {
        if (mSocket != null && mSocket.connected()) mSocket.emit(type, message);
    }

    private Emitter.Listener onCompleteItem = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    int id = (int) args[1];
                    //if (id == MainActivity.listeningID) {
                        Message SocketMsg = mHandler.obtainMessage(Constants.MESSAGE_COMPLETE_ITEM);
                        Bundle bundle = new Bundle();
                        bundle.putString("MESSAGE", message);
                        SocketMsg.setData(bundle);
                        mHandler.sendMessage(SocketMsg);
                    //}
                }
            });
        }
    };

    private Emitter.Listener onStackOnly = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    int id = (int) args[1];
                    boolean found = false;
                    for (int i : ids) {
                        if (i == id) found = true;
                    }
                    if (!found) ids[ids.length] = id;
                    //if (id == MainActivity.listeningID) {
                        Message SocketMsg = mHandler.obtainMessage(Constants.MESSAGE_STACK_ONLY);
                        Bundle bundle = new Bundle();
                        bundle.putString("MESSAGE", message);
                        SocketMsg.setData(bundle);
                        mHandler.sendMessage(SocketMsg);
                    //}
                }
            });
        }
    };

    private Emitter.Listener onPlayerInfo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String message = (String) args[0];
                    int id = (int) args[1];
                    //if (id == MainActivity.listeningID) {
                        Message SocketMsg = mHandler.obtainMessage(Constants.MESSAGE_PLAYER_INFO);
                        Bundle bundle = new Bundle();
                        bundle.putString("MESSAGE", message);
                        SocketMsg.setData(bundle);
                        mHandler.sendMessage(SocketMsg);
                    //}
                }
            });
        }
    };
}