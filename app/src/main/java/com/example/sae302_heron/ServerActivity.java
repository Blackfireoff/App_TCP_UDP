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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Switch;
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
    private EditText UsernameServer;
    private Switch switchUDP;
    private String ip_serv;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        startServerButton = findViewById(R.id.start_server_button);
        serverPort = findViewById(R.id.server_port_2);
        ipAddress = findViewById(R.id.ip_address);
        clientButton = findViewById(R.id.client_server);
        UsernameServer = findViewById(R.id.Username_server);
        switchUDP = findViewById(R.id.switch_to_udp_server);

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        ipAddress.setText(ip);
                        ip_serv = ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }


        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String port = serverPort.getText().toString();
                System.out.println("Port ecrit : "+port);
                Intent intent = new Intent(ServerActivity.this, messageActivity_server.class);
                intent.putExtra("ip_serveur",ip_serv);
                intent.putExtra("port_server",port);
                intent.putExtra("Username_server",UsernameServer.getText().toString());
                intent.putExtra("value_udp",switchUDP.isChecked());
                startActivity(intent);
                }
        });
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServerActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

}

