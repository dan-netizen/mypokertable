package com.higedata.mypokertable.clientlogic;

import com.higedata.mypokercliet.packets.RemovePlayerPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

public class PokerTableClient implements Runnable {

    //vars
    private String host;
    private int port;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = false;
    private DataListenerClient dataListenerClient;

    //constructor
    public PokerTableClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //connecting to server
    public void connect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            dataListenerClient = new DataListenerClient();
            new Thread(this).start();
        } catch (ConnectException e) {
            System.out.println("Can't connect to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //close connection to server
    public void close() {
        try {
            running = false;
            //send dc message to server
            RemovePlayerPacket packet = new RemovePlayerPacket();
            sendObject(packet);
            in.close();
            out.close();
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //sending the data to server
    public void sendObject(Object packet) {
        try {
            out.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            running = true;
            while (running) {
                try {
                    Object data = in.readObject();
                    //work with the data
                    dataListenerClient.received(data);
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                } catch(SocketException e) {
                    close();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
