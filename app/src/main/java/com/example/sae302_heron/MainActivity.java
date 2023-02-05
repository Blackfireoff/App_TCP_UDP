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
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Button connectButton;
    private EditText serverAddress;
    private EditText serverPort;
    private EditText Username;
    private Button serverButton;
    private Switch switchUDP;

    private ProgressBar connecting;


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
        connecting = findViewById(R.id.Connecting);


        connecting.setVisibility(View.INVISIBLE);



        //Bouton qui renvoie vers l'activité messageActivity, qui est la partie connexion client et interface utilisateur.
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connecting.setVisibility(View.VISIBLE);

                String server = serverAddress.getText().toString();
                int port = Integer.parseInt(serverPort.getText().toString());

                String[] parts = server.split("\\.");
                if (parts.length != 4) {
                    showToast("Entrée non valide: ce n'est pas une adresse IP");
                    return;
                }

                for (String part : parts) {
                    try {
                        int value = Integer.parseInt(part);
                        if (value < 0 || value > 255) {
                            showToast("Entrée non valide: ce n'est pas une adresse IP");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        showToast("Entrée non valide: ce n'est pas une adresse IP");
                        return;
                    }
                }




                //J'utilise la méthode Intent pour changer d'activté et transférer des éléments entre elles.
                Intent intent = new Intent(MainActivity.this, messageActivity.class);
                intent.putExtra("server", server);
                intent.putExtra("port", port);
                intent.putExtra("Username", Username.getText().toString());
                intent.putExtra("value_of_udp", switchUDP.isChecked());
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

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }



}
