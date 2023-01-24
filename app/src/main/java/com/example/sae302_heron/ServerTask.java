package com.example.sae302_heron;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTask extends AsyncTask<String, Void, Void> {

    private ServerSocket serverSocket;
    private StringBuilder sb;
    private String username;
    private TextView message;
    private ArrayList<SocketTask> SkT;
    private ArrayList<Socket> Sk;


    //Constructeur de la classe
    ServerTask(int port, StringBuilder sbe, String nameUser, TextView TVM){
        try {
            serverSocket = new ServerSocket(port);
            sb = sbe;
            username = nameUser;
            message = TVM;
            SkT = new ArrayList<>();
            Sk = new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(String... message) {
        while(true) {
            try {
                System.out.println("En attente de connexion d'un client");
                Socket socket = serverSocket.accept();
                Sk.add(socket);
                System.out.println("Client connecté");

                SocketTask T = new SocketTask(socket, username, sb, this.message,Sk,this);
                SkT.add(T);
                T.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
