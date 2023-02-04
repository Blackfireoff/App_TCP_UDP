package com.example.sae302_heron;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.sae302_heron.databinding.ActivityHomeBinding;
import com.example.sae302_heron.databinding.ActivityMessageServerBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;


public class UDPServerTask extends AsyncTask<Void, Void, Void> {

    private DatagramSocket socket;
    private byte[] receiveBuffer;
    private DatagramPacket receivePacket;
    private JSONObject json;
    private StringBuilder sb;
    private TextView message;
    private ArrayList<String> IP;
    private int port;
    private Hashtable<String,String> json_bd;


    UDPServerTask(int port_temp, TextView msg){
        try {
            // Création d'un socket UDP sur le port "port"
            port = port_temp;
            socket = new DatagramSocket(port);
            receiveBuffer = new byte[1024];
            message = msg;
            sb = new StringBuilder();
            IP = new ArrayList<>();

            // Préparation d'un paquet pour recevoir les données
            receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }




    @Override
    protected Void doInBackground(Void... Void) {
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
                String username = json.getString("Username");
                String message_recu = json.getString("Data");
                sb.append( username + " : " +  message_recu + "\n");
                String IP_temp = receivePacket.getAddress().toString();
                System.out.println("IP du client : "+IP_temp);

                if(!IP.contains(IP_temp)){
                    IP.add(IP_temp);
                }

                json_bd = new Hashtable<>();
                json_bd.put("Username",username);
                json_bd.put("Data",message_recu);
                json = new JSONObject(json_bd);
                byte[] sendBuffer = json.toString().getBytes();

                for (int i = 0; i < IP.size(); i++) {
                    String ip = IP.get(i);
                    if (ip.startsWith("/")) {
                        ip = ip.substring(1);
                    }
                    InetAddress clientAddress = InetAddress.getByName(ip);
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, port+1);
                    socket.send(sendPacket);
                    System.out.println("Packet envoyé : "+json.toString()+" à l'IP : "+ clientAddress);
                }

                publishProgress();

                // Affichage du message reçu
                System.out.println(message_recu);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        message.setText(sb);
    }
}
