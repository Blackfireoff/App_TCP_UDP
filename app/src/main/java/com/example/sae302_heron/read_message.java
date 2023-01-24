package com.example.sae302_heron;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class read_message extends AsyncTask<Void, Void, Void> {

    private DataInputStream in;
    private Socket socket;
    private String json_receive;
    private JSONObject json;
    private TextView message;
    private StringBuilder sb;

    read_message(Socket ST, TextView MS, StringBuilder Sb_temp) {
        socket = ST;
        message = MS;
        sb = Sb_temp;
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) {
            try {
                json_receive = in.readUTF();
                System.out.println(json_receive);
                json = new JSONObject(json_receive);
                sb.append(json.getString("Username") + " : " + json.getString("Data") + "\n");
                publishProgress();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        message.setText(sb);
    }
}
