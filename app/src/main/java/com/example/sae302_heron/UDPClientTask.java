package com.example.sae302_heron;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UDPClientTask{

    private DatagramSocket socket;
    private DatagramPacket sendPacket;
    private String message;
    private InetAddress serverAddress;
    private int port;

    UDPClientTask(String ip, int port_send){
        // Création d'un socket UDP
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName(ip);
            System.out.println(serverAddress);
            port = port_send;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
    public void main(String message_to_send) {
        try {
            message = message_to_send;
            while (true) {
                // Préparation d'un paquet pour envoyer le message
                byte[] sendBuffer = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, port);

                // Envoi du paquet
                socket.send(sendPacket);

                // Préparation d'un paquet pour recevoir la réponse
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                // Attente de la réception d'un paquet
                socket.receive(receivePacket);

                // Récupération des données contenues dans le paquet
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Affichage de la réponse
                System.out.println("Réponse : " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
