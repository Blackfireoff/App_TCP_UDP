package com.example.sae302_heron;

import android.os.AsyncTask;
import android.widget.TextView;

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

    //Constructeur de la classe
    UDPClientTask(String ip, int port_send, String user, TextView message_temp){
        // Création d'un socket UDP
        try {

            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);
            System.out.println(serverAddress);
            port = port_send;
            pile_message = new ArrayBlockingQueue<>(50);
            username = user;
            json_bd = new Hashtable<>();
            json_bd.put("Username",username);
            message_tv = message_temp;
            rmUDP = new read_message_UDP(port,message_tv);
            rmUDP.execute();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    //Ajoute un message dans une liste au format pile, ce qui permet une surchage des messages si le client en envoie plusieur en même temps
    public void add_message(String message){
        pile_message.add(message);
    }


    //Vide la pile, mes les messages dans un fichier JSON, et envoie le JSON
    @Override
    protected Void doInBackground(Void... Void) {
        while (true) {
            while ((message = pile_message.poll())!=null) {
                try {
                    System.out.println("Message à envoyer : "+message);

                    json_bd.put("Data",message);
                    json = new JSONObject(json_bd);

                    // Préparation d'un paquet pour envoyer le message
                    byte[] sendBuffer = json.toString().getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, port);

                    // Envoi du paquet
                    socket.send(sendPacket);

                    System.out.println("Packet envoyé : "+json.toString()+" à l'IP : "+serverAddress);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
