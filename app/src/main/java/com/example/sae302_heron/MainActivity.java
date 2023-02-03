package com.example.sae302_heron;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;

import android.view.View.OnClickListener;

import android.content.Intent;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity {
    private Button connectButton;
    private EditText serverAddress;
    private EditText serverPort;
    private EditText Username;
    private Button serverButton;
    private Switch switchUDP;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = findViewById(R.id.connect_button);
        serverAddress = findViewById(R.id.server_address);
        serverPort = findViewById(R.id.server_port);
        serverButton = findViewById(R.id.server_button);
        Username = findViewById(R.id.nom_utilisateur);
        switchUDP = findViewById(R.id.switch_to_udp_client);


        //Bouton qui renvoie vers l'activité messageActivity, qui est la partie connexion client et interface utilisateur.
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String server = serverAddress.getText().toString();
                int port = Integer.parseInt(serverPort.getText().toString());

                //J'utilise la méthode Intent pour changer d'activté et transférer des éléments entre elles.
                Intent intent = new Intent(MainActivity.this, messageActivity.class);
                intent.putExtra("server", server);
                intent.putExtra("port",port);
                intent.putExtra("Username",Username.getText().toString());
                intent.putExtra("value_of_udp",switchUDP.isChecked());
                startActivity(intent);

            }
        });
        //Bouton qui renvoie vers l'activité HomeActivté
        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }


}
