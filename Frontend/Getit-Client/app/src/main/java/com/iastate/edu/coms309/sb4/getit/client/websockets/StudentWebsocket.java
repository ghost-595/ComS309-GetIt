package com.iastate.edu.coms309.sb4.getit.client.websockets;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.iastate.edu.coms309.sb4.getit.client.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class StudentWebsocket {
    private WebSocketClient webSocket;
    private TextView attendanceOutput;
    private Context appContext;
    //public string of the output for testing
    public String onMessageReturnString = "";

    public StudentWebsocket(Context context, int id) {
        appContext = context;
        try {
            webSocket = new WebSocketClient(new URI("ws://10.0.2.2:8080/attendance/" + id)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.i("Student Websocket", "Opened");
                }

                @Override
                public void onMessage(String s) {
                    Log.i("Student Websocket", "Message Received " + s);
                    onMessageReturnString = s;
                    attendanceOutput = ((Activity) appContext).findViewById(R.id.studentAttendanceOutput);
                    attendanceOutput.setText(s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.i("Student Websocket", "Closed " + s);
                }

                @Override
                public void onError(Exception e) {
                    Log.i("Student Websocket", "Error " + e.getMessage());
                }
            };
            webSocket.connectBlocking();
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        webSocket.send(message);
    }

    public void closeWebsocket() {
        webSocket.close();
    }
}
