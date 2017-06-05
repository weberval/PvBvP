package de.dhbw_loerrach.pvbvp.Network;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Class for handling the networking.
 *
 * startServerReceiver : Starts the thread that listens. runs the whole time.
 * startClientReceiver: Starts the thread for the client. runs the whole time.
 * startClient: Sends broadcasts, is finished when connection is established.
 *
 * Created by renat on 23.05.17.
 */

public class Networking {

    public static final String TAG_SERVER = "Server";
    public static final String TAG_CLIENT = "Client";

    public static boolean SERVER = false;
    public static int PORT = 4567;

    public static boolean HB_STARTED = false;
    public static final int SLEEP_TIME = 100;
    public static final int TIME_OUT = 5;
    public static int TIME_OUT_COUNTER = 0;

    //while true receivers AND heartbeat are running.
    public static boolean RECEIVER_RUNNING = true;

    public static boolean CLIENT_CONNECTED = false;

    private static DatagramSocket socket;
    private static DatagramSocket socketReceive;

    public static InetAddress partnerAddress = null;

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

                    socket = new DatagramSocket();
                    socket.setReuseAddress(true);

                    Log.i(TAG_SERVER,"Server listening...");
                    socketReceive = new DatagramSocket(PORT);
                    while(RECEIVER_RUNNING){
                        socketReceive.receive(packet);
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
            Log.i("Sending","packet to " + partnerAddress.getHostAddress() + " with the data : " + msg);
        }catch(Exception e){
            Log.i((SERVER) ? TAG_SERVER : TAG_CLIENT,"Error in send():" + e.getMessage());
        }
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
                    socket = new DatagramSocket();
                    socket.setReuseAddress(true);
                    socket.setBroadcast(true);


                    InetAddress broadcast = InetAddress.getByName("255.255.255.255");
                    byte[] data = Protocol.get_msg(Protocol.CLT_MSG_HELLO,null).getBytes();
                    DatagramPacket packet = new DatagramPacket(data,data.length,broadcast,PORT);

                    Log.i(TAG_CLIENT,"connecting...");
                    while(!CLIENT_CONNECTED) {
                        socket.send(packet);
                        Thread.sleep(100);
                    }
                    Log.i(TAG_CLIENT,"connected!");
                }catch (Exception e){
                    Log.i(TAG_CLIENT,"Error in startClient() : " + e.getMessage());
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
                    DatagramPacket packet = new DatagramPacket(new byte[1024],1024);
                    Log.i(TAG_CLIENT,"Starting ClientReceiver");
                    while(RECEIVER_RUNNING){
                        socketReceive.receive(packet);
                        Protocol.serverMsg(packet);
                    }
                }catch(Exception e){
                    Log.i(TAG_CLIENT,"Error in startClientReceiver() : " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * closes all connections and reset
     */
    public static void time_out(){
        Log.i((SERVER) ? TAG_SERVER :  TAG_CLIENT,"time out!");
        RECEIVER_RUNNING = false;
        Protocol.time_out();
    }


    public static void heartbeat(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(RECEIVER_RUNNING) {
                    try {
                        Thread.sleep(SLEEP_TIME);
                        TIME_OUT_COUNTER++;
                        if (TIME_OUT_COUNTER == TIME_OUT)
                            time_out();
                    } catch (Exception e) {
                        Log.i("HEARTBEAT", e.getMessage());
                    }
                }
            }
        }).start();
    }
}
