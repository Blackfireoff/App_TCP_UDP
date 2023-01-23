package com.example.sae302_heron;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;

public class SocketTask extends AsyncTask<Void, Void, Void> {

    private Socket socket;
    private String username;
    private StringBuilder sb;
    private TextView message;
    private String message_client;
    private DataInputStream in;
    private JSONObject json;

    SocketTask(Socket sk,String nameUser,StringBuilder sbe, TextView TVM) {
        socket = sk;
        username = nameUser;
        sb = sbe;
        message= TVM;
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            while(socket.isConnected()) {

                message_client = in.readUTF();
                json = new JSONObject(message_client);
                if(!message_client.isEmpty()) {
                    sb.append(json.getString("Username") + " : " + json.getString("Data") + "\n");
                    publishProgress();
                }
            }
            System.out.println("Socket fermé");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        message.setText(sb);
    }

}
