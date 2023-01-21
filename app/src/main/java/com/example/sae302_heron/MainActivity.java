package com.example.sae302_heron;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.widget.Button;
import android.widget.EditText;

import android.view.View;

import android.view.View.OnClickListener;

import android.content.Intent;


import android.os.AsyncTask;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    private Button connectButton;
    private EditText serverAddress;
    private EditText serverPort;
    private EditText Username;
    private Button serverButton;
    private Button messageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = findViewById(R.id.connect_button);
        serverAddress = findViewById(R.id.server_address);
        serverPort = findViewById(R.id.server_port);
        serverButton = findViewById(R.id.server_button);
        messageButton = findViewById(R.id.button_message);
        Username = findViewById(R.id.nom_utilisateur);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new ConnectTask().execute();
                String server = serverAddress.getText().toString();
                int port = Integer.parseInt(serverPort.getText().toString());
                Intent intent = new Intent(MainActivity.this, messageActivity.class);
                intent.putExtra("server", server);
                intent.putExtra("port",port);
                intent.putExtra("Username",Username.getText().toString());
                startActivity(intent);

            }
        });
        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServerActivity.class);
                startActivity(intent);
            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, messageActivity.class);
                startActivity(intent);
            }
        });
    }
/*
    public class SocketSingleton {

        private SocketSingleton instance;
        private Socket socket;

        private SocketSingleton() {
            // Initialisez le socket ici
            String server = serverAddress.getText().toString();
            int port = Integer.parseInt(serverPort.getText().toString());
            try {
                socket = new Socket(server, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public SocketSingleton getInstance() {
            if (instance == null) {
                instance = new SocketSingleton();
            }
            return instance;
        }

        public Socket getSocket() {
            return socket;
        }

        public void closeSocket(){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

*/




}
