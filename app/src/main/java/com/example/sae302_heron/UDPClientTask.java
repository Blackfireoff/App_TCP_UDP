package com.example.sae302_heron;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class UDPClientTask extends AsyncTask<Void, Void, Void> {

    private DatagramSocket socket;
    private DatagramPacket sendPacket;
    private String message;
    private InetAddress serverAddress;
    private int port;
    private Queue<String> pile_message;
    private String username;
    private JSONObject json;
    private Hashtable<String,String> json_bd;
    private read_message_UDP rmUDP;
    private TextView message_tv;

    private boolean isConnected;

    private boolean waitco;

    private Context context;

    //Constructeur de la classe
    UDPClientTask(String ip, int port_send, String user, TextView message_temp, Context cont){
        // Création d'un socket UDP
        try {
            isConnected = true;
            waitco = true;
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);
            System.out.println(serverAddress);
            port = port_send;
            pile_message = new ArrayBlockingQueue<>(50);
            username = user;
            json_bd = new Hashtable<>();
            json_bd.put("Username",username);
            message_tv = message_temp;
            rmUDP = new read_message_UDP(port,message_tv,context);
            rmUDP.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
        } catch (SocketException e) {
            e.printStackTrace();
            CharSequence test = "test";
            Toast.makeText(context, test, Toast.LENGTH_SHORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    //Ajoute un message dans une liste au format pile, ce qui permet une surchage des messages si le client en envoie plusieur en même temps
    public void add_message(String message_received){
        pile_message.add(message_received);
        System.out.println("Ajout a la pile du message : " + message_received);
    }

    public void socket_close(){
        pile_message.add("Fin de la communication.");
        waitco = false;
    }


    //Vide la pile, mes les messages dans un fichier JSON, et envoie le JSON
    @Override
    protected Void doInBackground(Void... Void) {
        while (isConnected) {
            while ((message = pile_message.poll())!=null) {
                System.out.println("Dépilage de la pile : " + message);
                String connected = "true";
                try {
                    System.out.println("Message à envoyer : "+message);
                    if(waitco == false){
                        connected = "false";
                    }
                    json_bd.put("Data",message);
                    json_bd.put("Type","UDP");
                    json_bd.put("isConnected",connected);
                    json = new JSONObject(json_bd);

                    // Préparation d'un paquet pour envoyer le message
                    byte[] sendBuffer = json.toString().getBytes();
                    sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, port);

                    // Envoi du paquet
                    socket.send(sendPacket);

                    System.out.println("Packet envoyé : "+json.toString()+" à l'IP : "+serverAddress);
                    if(waitco == false){
                        isConnected = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        socket.close();
        return null;
    }

}
