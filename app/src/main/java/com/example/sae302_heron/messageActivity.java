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

        StringBuilder sb = new StringBuilder("Bienvenue sur l'application de messagerie de Heron !\n\n");
        message.setText(sb);
        // Récupération du socket à partir de l'Intent
        Intent intent = getIntent();
        String server = intent.getStringExtra("server");
        System.out.println("L'IP du server est :");
        System.out.println(server);
        String username = intent.getStringExtra("Username");
        int port = intent.getIntExtra("port", 5000);

        CT = new ClientTask(server,port,username);
        CT.execute();



        socket_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(messageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = write_message.getText().toString();

                CT.add_message(messageToSend);

                sb.append(username+" : "+ messageToSend + "\n");
                message.setText(sb);
                write_message.setText("");
            }
        });





        class ReadMessage extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                        try {

                            Socket socket = new Socket(server, port);
                            DataInputStream in = new DataInputStream(socket.getInputStream());
                            message_recu = in.readUTF();
                            System.out.println("le message est :");
                            System.out.println(message_recu);
                            sb.append("Server : "+message_recu + "\n");
                            message.setText(sb);
                            socket.close();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    return null;
                };




        }

        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                new ReadMessage().execute();
            }
        }, 0, 5, TimeUnit.SECONDS);




        }
    };
