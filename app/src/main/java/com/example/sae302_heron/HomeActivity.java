package com.example.sae302_heron;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sae302_heron.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    private Button ButtonClient;
    private Button ButtonServer;
    private Button ButtonFTP;
    private Button test;
    private FloatingActionButton ButtonAdmin;
    private FloatingActionButton ButtonValid;
    private EditText login_admin;
    private EditText pass_admin;
    private String login;
    private String pass;

    private EncryptionAES aes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ButtonClient = binding.buttonGoToClient;
        ButtonServer = binding.buttonGoToServer;
        ButtonFTP = binding.buttonGoToFtp;
        ButtonAdmin = binding.fABAdmin;
        ButtonValid = binding.fABValidLog;
        login_admin = binding.loginAdmin;
        pass_admin = binding.passAdmin;
        test = binding.test;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ButtonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ButtonServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServerActivity.class);
                startActivity(intent);
            }
        });

        ButtonFTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, icmpactivity.class);
                startActivity(intent);
            }
        });

        ButtonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonAdmin.setVisibility(View.INVISIBLE);
                login_admin.setVisibility(View.VISIBLE);
                pass_admin.setVisibility(View.VISIBLE);
                ButtonValid.setVisibility(View.VISIBLE);
            }
        });

        ButtonValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = login_admin.getText().toString();
                pass = pass_admin.getText().toString();

                if(!login.equals("admin") || !pass.equals("admin")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Mauvais login et/ou mot de passe");
                    builder.setMessage("Vous avez entrez un mauvais login ou un mauvais mot de passe. \nMerci de reessayer.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    ButtonFTP.setVisibility(View.VISIBLE);
                    ButtonAdmin.setVisibility(View.VISIBLE);
                    login_admin.setVisibility(View.INVISIBLE);
                    pass_admin.setVisibility(View.INVISIBLE);
                    ButtonValid.setVisibility(View.INVISIBLE);
                }
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    File file = new File("src/main/java/com/example/myproject/key.aes");
                    String absolutePath = file.getAbsolutePath();
                    byte[] keyBytes = new byte[(int) absolutePath.length()];
                try {
                    FileInputStream keyFile = new FileInputStream(absolutePath);
                    keyFile.read(keyBytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Key key = new SecretKeySpec(keyBytes, "AES");
                aes = new EncryptionAES("test",key);
                aes.encryptMessage();
                System.out.println(aes.getEncryptedMessage());
            }
        });

    }


}