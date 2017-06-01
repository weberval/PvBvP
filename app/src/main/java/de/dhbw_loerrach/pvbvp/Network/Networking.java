package de.dhbw_loerrach.pvbvp.Network;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by renat on 23.05.17.
 */

public class Networking {

    public static final String TAG_SERVER = "Server";
    public static final String TAG_CLIENT = "Client";

    public static boolean SERVER = false;
    public static int PORT = 4567;

    public static boolean CLIENT_CONNECTED = false;

    private static DatagramSocket socket;
    private static DatagramSocket socketReceive;

    private static InetAddress partnerAddress;

    /**
     * starting the server.
     * waiting endlessly to connect.
     * when connected, precede
     */
    public static void startServerReceiver(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SERVER = true;
                DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
                try {
                    socketReceive = new DatagramSocket(PORT);
                    while(true){
                        //TEST
                        Log.i(TAG_SERVER,"Server listening");
                        //TEST
                        socketReceive.receive(packet);

                        //TEST
                        Log.i(TAG_SERVER, packet.toString());
                        //TEST

                        Protocol.clientMsg(packet);
                    }
                }catch (Exception e){
                    Log.i(TAG_SERVER,e.getMessage());
                }
            }
        }).start();
    }


    /**
     *
     */
    public static void send(String msg){
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data,data.length,partnerAddress,PORT);
        try {
            socket.send(packet);
        }catch(Exception e){
            Log.i((SERVER) ? TAG_SERVER : TAG_CLIENT,e.getMessage());
        }
        Protocol.msg_ID++;
    }

    /**
     * starting client
     * broadcasting packets endlessly
     * when connected, precede, end thread
     */
    public static void startClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new DatagramSocket(null);
                    socket.setReuseAddress(true);
                    socket.setBroadcast(true);
                    socket.bind(new InetSocketAddress(PORT));

                    Log.i(TAG_CLIENT+": add",socket.getInetAddress().getAddress().toString());

                    InetAddress broadcast = InetAddress.getByName("255.255.255.255");
                    byte[] data = "CLIENT;TEST".getBytes();
                    DatagramPacket packet = new DatagramPacket(data,data.length,broadcast,PORT);

                    while(!CLIENT_CONNECTED) {
                        socket.send(packet);
                        Thread.sleep(100);
                    }
                }catch (Exception e){
                    Log.i(TAG_CLIENT + " : startClient",e.getMessage());
                }
            }
        }).start();
    }

    /**
     * start client receiver
     */
    public static void startClientReceiver(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketReceive = new DatagramSocket(null);
                    socketReceive.setReuseAddress(true);
                    socketReceive.bind(new InetSocketAddress(PORT));
                    DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
                    while(true){
                        socketReceive.receive(packet);
                        Protocol.serverMsg(packet);
                    }
                }catch(Exception e){
                    Log.i(TAG_CLIENT + " : startCReceiver",e.getMessage());
                }
            }
        }).start();
    }
}
