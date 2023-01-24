package com.example.sae302_heron;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


public class ClientTask extends AsyncTask<Void, Void, Void> {

    private Socket socket;
    private DataOutputStream out;
    private Queue<String> pile_message;
    private String username;
    private JSONObject json;
    private Hashtable<String,String> json_bd;
    private read_message RM;


    ClientTask(String server, int port, String name){
        try{
            socket = new Socket(server, port);
            out = new DataOutputStream(socket.getOutputStream());
            pile_message = new ArrayBlockingQueue<>(50);
            username = name;
            json_bd = new Hashtable<>();
            json_bd.put("Username",username);
            RM = new read_message(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add_message(String message){
        pile_message.add(message);
    }

    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(Void... voids) {
        String message;
        while (true) {
            while ((message = pile_message.poll())!=null) {
                try {
                    json_bd.put("Data",message);
                    json = new JSONObject(json_bd);
                    RM.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
                    out.writeUTF(json.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}


