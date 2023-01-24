package com.example.sae302_heron;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class read_message extends AsyncTask<Void, Void, Void> {

    private DataInputStream in;
    private Socket socket;
    private String json_receive;

    read_message(Socket ST) {
        socket = ST;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
