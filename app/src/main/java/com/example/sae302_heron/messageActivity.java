package com.example.sae302_heron;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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
import java.net.Socket;


import com.example.sae302_heron.databinding.ActivityMessageBinding;

public class messageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;
    private TextView message;
    private Button socket_close;
    private Button send_message;
    private EditText write_message;

    int value_socket = 0;
    int value_message = 0;
    String message_recu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        message = binding.messageDisplay;
        socket_close = binding.buttonQuit;
        send_message = binding.sendMessage;
        write_message = binding.WriteMessage;

        StringBuilder sb = new StringBuilder("Bienvenue sur l'application de messagerie de Heron !\n\n");

        // Récupération du socket à partir de l'Intent
        Intent intent = getIntent();
        String server = intent.getStringExtra("server");
        int port = intent.getIntExtra("port",5000);

        socket_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_socket = 1;
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_message = 1;
                sb.append(message_recu+"\n");
                message.setText(sb);
            }
        });







        class ConnectTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Socket socket = new Socket(server, port);
                    System.out.println(value_message);
                    while (value_socket == 0) {
                        if (value_message == 1) {
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF(write_message.getText().toString());
                            value_message = 0;
                        }
                        /*
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        message_recu = in.readUTF();
                        System.out.println(message_recu);
                         */
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            protected void onPostExecute(){
                Intent intent = new Intent(messageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        ConnectTask connect = new ConnectTask();
        connect.execute();


    }
}