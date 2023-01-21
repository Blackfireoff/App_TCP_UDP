package com.example.sae302_heron;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.*;
import android.os.Bundle;

import com.example.sae302_heron.databinding.ActivityMessageBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.StringBuilder;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.sae302_heron.databinding.ActivityMessageServerBinding;

import android.os.Bundle;


public class messageActivity_server extends AppCompatActivity {

    private ActivityMessageServerBinding binding;
    private Button exit;
    private Button send;
    private TextView message;
    private EditText message_ecrit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_server);

        binding = ActivityMessageServerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        exit = binding.quitbuttonServer;
        send = binding.sendMessageServer;
        message = binding.textView3;
        message_ecrit = binding.messageServer;

        StringBuilder sb = new StringBuilder("Bienvenue sur l'application de messagerie de Heron !\n\n");
        message.setText(sb);
        // Récupération du socket à partir de l'Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("Username");
        int port = intent.getIntExtra("port", 5000);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messageActivity_server.this, ServerActivity.class);
                startActivity(intent);
            }
        });

        class ServerTask extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... message) {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.out.println("En attente de connexion d'un client");
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connecté");
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    String message_client = in.readUTF();
                    sb.append(username+" : "+ message_client + "\n");
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(message[0]);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void test) {
                message.setText(sb);
            }
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = message_ecrit.getText().toString();
                new ServerTask().execute(messageToSend);
                sb.append(username+" : "+ messageToSend + "\n");
                message.setText(sb);
                message_ecrit.setText("");
            }
        });








    }
}