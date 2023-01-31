package com.example.sae302_heron;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.telephony.mbms.MbmsErrors;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;


public class SocketTask extends AsyncTask<Void, Void, Void> {

    private Socket socket;
    private String username;
    private StringBuilder sb;
    private TextView message;
    private String message_client;
    private DataInputStream in;
    private DataOutputStream out;
    private JSONObject json;
    private ArrayList<Socket> Sk;
    private ServerTask ST;

    @SuppressLint("WrongThread")
    SocketTask(Socket sk,String nameUser,StringBuilder sbe, TextView TVM,ServerTask ST) {
        socket = sk;
        username = nameUser;
        sb = sbe;
        this.ST = ST;
        message = TVM;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void SendMessage(String message){
        try {
            out.writeUTF(message);
            System.out.println("JSON Envoyé au socket : " + socket.toString());
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
                System.out.println("SocketTask 1");
                if(!message_client.isEmpty()) {
                    ST.publishMessage(message_client);
                    System.out.println("2");
                    sb.append(json.getString("Username") + " : " + json.getString("Data") + "\n");
                    publishProgress();
                }
            }
            System.out.println("Socket fermé");
            socket.close();
            Sk.remove(socket);
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
