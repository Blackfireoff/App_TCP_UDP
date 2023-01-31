package com.example.sae302_heron;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServerTask extends AsyncTask<Void, Void, Void> {

    private DatagramSocket socket;
    private byte[] receiveBuffer;
    private DatagramPacket receivePacket;

    UDPServerTask(int port){
        try {
            // Création d'un socket UDP sur le port "port"
            socket = new DatagramSocket(port);
            receiveBuffer = new byte[1024];

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
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // Affichage du message reçu
                System.out.println("Reçu de " + clientAddress + ":" + clientPort + " : " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
