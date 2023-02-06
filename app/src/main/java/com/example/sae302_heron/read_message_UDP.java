package com.example.sae302_heron;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;



public class read_message_UDP extends AsyncTask {

    private DatagramSocket socket;
    private byte[] receiveBuffer;
    private DatagramPacket receivePacket;
    private JSONObject json;
    private StringBuilder sb;
    private TextView message;

    private Context context;

    read_message_UDP(int port, TextView msg, Context cont){
        try {
            // Création d'un socket UDP sur le port "port"
            socket = new DatagramSocket(port+1);
            receiveBuffer = new byte[1024];
            message = msg;
            sb = new StringBuilder();
            context = cont;




            // Préparation d'un paquet pour recevoir les données
            receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }





    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            while(true) {
                // Attente de la réception d'un paquet
                System.out.println("En attente de packet entrant");
                socket.receive(receivePacket);
                System.out.println("Packet recu");
                // Récupération des données contenues dans le paquet
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(message);
                json = new JSONObject(message);
                sb.append( json.getString("Type")+" - "+json.getString("Username") + " : " + json.getString("Data") + "\n");


                publishProgress();

                // Affichage du message reçu
                System.out.println(sb);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Affichage à l'écran du message.
    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        message.setText(sb);
    }
}
