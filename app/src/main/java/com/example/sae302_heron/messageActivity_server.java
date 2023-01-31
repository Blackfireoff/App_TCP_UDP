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

import android.text.method.ScrollingMovementMethod;
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
    private UDPClientTask UDPCT;


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
        message.setMovementMethod(new ScrollingMovementMethod());

        // Récupération du socket à partir de l'Intent
        Intent intent = getIntent();
        String ip_serv = intent.getStringExtra("ip_serveur");
        String username = intent.getStringExtra("Username_server");
        System.out.println(username);
        String port_s = intent.getStringExtra("port_server");
        Boolean value_udp = intent.getBooleanExtra("value_udp",false);
        System.out.println(port_s);
        int port = Integer.parseInt(port_s);
        System.out.println(port);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messageActivity_server.this, ServerActivity.class);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = message_ecrit.getText().toString();
                if(value_udp==false) {
                    CT.add_message(messageToSend);
                    System.out.println("message envoyé depuis le serveur");
                }else {
                    UDPCT.add_message(messageToSend);
                    System.out.println("message envoyé");
                }
            }
        });


        if(value_udp == false) {
            ST = new ServerTask(port, sb, username, message);
            ST.execute();
            CT = new ClientTask("127.0.0.1",port,username,message,sb);
            CT.execute();
        }
        else{
            UDPST = new UDPServerTask(port);
            UDPST.execute();
            UDPCT = new UDPClientTask(ip_serv,port);
            UDPCT.execute();
        }





    }
}