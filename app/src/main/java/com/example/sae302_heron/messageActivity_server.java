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
    private ServerTask ST;
    private ClientTask CT;
    private UDPServerTask UDPST;


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
        Boolean value_udp = intent.getBooleanExtra("value_udp",false);


        //CT = new ClientTask()

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messageActivity_server.this, ServerActivity.class);
                startActivity(intent);
            }
        });

        if(value_udp == false) {
            ST = new ServerTask(port, sb, username, message);
            ST.execute();
        }
        else{
            UDPST = new UDPServerTask(port);
            UDPST.main();
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}