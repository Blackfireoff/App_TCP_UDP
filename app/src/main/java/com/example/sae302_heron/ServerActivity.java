package com.example.sae302_heron;

import androidx.appcompat.app.AppCompatActivity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.view.View;

import android.util.Log;


import android.view.View.OnClickListener;

import android.os.AsyncTask;
import android.os.Bundle;

public class ServerActivity extends AppCompatActivity {
    private Button startServerButton;
    private Button clientButton;
    private EditText serverPort;
    private TextView ipAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        startServerButton = findViewById(R.id.start_server_button);
        serverPort = findViewById(R.id.server_port_2);
        ipAddress = findViewById(R.id.ip_address);
        clientButton = findViewById(R.id.client_server);

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        ipAddress.setText(ip);
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }


        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ServerTask().execute();
            }
        });
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private class ServerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                int port = Integer.parseInt(serverPort.getText().toString());
                ServerSocket serverSocket = new ServerSocket(port);

                while (true) {
                    System.out.println("En attente de connexion d'un client");
                    Socket socket = serverSocket.accept();
                    System.out.println("Connexie établie");
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    String nomClient = in.readUTF();
                    String s1 = "Bienvenue "+nomClient+", t'es bien connecté bro";
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.writeUTF(s1);
                    socket.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

