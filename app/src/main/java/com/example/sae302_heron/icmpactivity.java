package com.example.sae302_heron;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sae302_heron.databinding.ActivityMessageBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class icmpactivity extends AppCompatActivity {

    private Button quit;
    private Button ping;
    private TextView show_ping;
    private EditText ip_to_ping;
    private String ip;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icmp);

        quit = findViewById(R.id.quit_button);
        ping = findViewById(R.id.BT_to_ping);
        show_ping = findViewById(R.id.result_icmp);
        ip_to_ping = findViewById(R.id.IP_to_ping);

        sb = new StringBuilder();

        //Permet d'activer le scroll sur un TextView
        show_ping.setMovementMethod(new ScrollingMovementMethod());

        //Bouton qui lance une commande PING vers l'IP spécifié par l'utilisateur.
        ping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = ip_to_ping.getText().toString();
                try {

                    String cmdPing = "ping -c 3 " + ip; //Ping 3 fois si plus changer l'argument ping -c x
                    Runtime r = Runtime.getRuntime();
                    Process p = r.exec(cmdPing);
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    sb.append("\n\n");
                    show_ping.setText(sb);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Bouton qui renvoie vers l'activité HomeActivity
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(icmpactivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}