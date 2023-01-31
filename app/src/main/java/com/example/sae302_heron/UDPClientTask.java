package com.example.sae302_heron;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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

    UDPClientTask(String ip, int port_send){
        // Création d'un socket UDP
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);
            System.out.println(serverAddress);
            port = port_send;
            pile_message = new ArrayBlockingQueue<>(50);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void add_message(String message){
        pile_message.add(message);
    }


    @Override
    protected Void doInBackground(Void... Void) {
        while (true) {
            while ((message = pile_message.poll())!=null) {
                try {
                    System.out.println("Message à envoyer : "+message);
                    // Préparation d'un paquet pour envoyer le message
                    byte[] sendBuffer = message.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, port);

                    // Envoi du paquet
                    socket.send(sendPacket);

                    System.out.println("Packet envoyé");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
