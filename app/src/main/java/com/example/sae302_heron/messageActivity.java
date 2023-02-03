package com.example.sae302_heron;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.*;
import android.os.Bundle;

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
import java.net.Socket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



import com.example.sae302_heron.databinding.ActivityMessageBinding;
import com.example.sae302_heron.ClientTask;

public class messageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;
    private TextView message;
    private Button socket_close;
    private Button send_message;
    private EditText write_message;
    private ClientTask CT;
    private UDPClientTask UDPCT;

    String message_recu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        message = binding.messageDisplay;
        socket_close = binding.buttonQuit;
        send_message = binding.sendMessage;
        write_message = binding.WriteMessage;
        message.setMovementMethod(new ScrollingMovementMethod());
        StringBuilder sb = new StringBuilder("Bienvenue sur l'application de messagerie de Heron !\n\n");
        message.setText(sb);
        // Récupération du socket à partir de l'Intent
        Intent intent = getIntent();
        String server = intent.getStringExtra("server");
        System.out.println("L'IP du server est :"+server);
        String username = intent.getStringExtra("Username");
        int port = intent.getIntExtra("port", 5000);
        Boolean value_udp = intent.getBooleanExtra("value_of_udp",false);



        //Bouton qui renvoie vers l'activité MainActivity
        socket_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Permet d'ajouter le message dans la pile de la classe ClientTask
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = write_message.getText().toString();
                if(value_udp==false) {
                    CT.add_message(messageToSend);
                    System.out.println("message envoyé depuis le client");
                }else {
                    UDPCT.add_message(messageToSend);
                    System.out.println("message envoyé depuis le client");
                }
            }
        });


        //Permet d'executer le client TCP ou UDP selon l'état du switch
        if(value_udp == false) {
            System.out.println("Mode TCP activé");
            CT = new ClientTask(server, port, username,message,sb);
            CT.execute();
        }else{
            System.out.println("Mode UDP activé");
            UDPCT = new UDPClientTask(server,port,username,message);
            UDPCT.execute();
        }

        }
    };
