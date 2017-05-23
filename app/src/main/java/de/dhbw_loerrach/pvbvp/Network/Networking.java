package de.dhbw_loerrach.pvbvp.Network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by renat on 23.05.17.
 */

public class Networking {

    public static final String TAG_SERVER = "Server";
    public static final String TAG_CLIENT = "Client";

    public static boolean SERVER = false;
    public static int PORT = 4567;

    public static boolean SERVER_RUNNING = true;

    private static ServerSocket serverSocket;
    private static Socket clientSocket;



    public static void startClient(){

    }

    private static void clientThread(){

    }

    public static void startServer(){
        SERVER = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    clientSocket = serverSocket.accept();
                    serverThread(clientSocket);
                }catch (Exception e){
                    Log.i(TAG_SERVER,e.getMessage());
                }
            }
        }).start();
    }


    private static void serverThread(final Socket client){
        new Thread(new Runnable() {

            private BufferedReader input;

            @Override
            public void run() {

                try {
                    input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                }catch (Exception e){
                    Log.i(TAG_SERVER,e.getMessage());
                }

                while(SERVER_RUNNING){
                    try{
                        Protocol.clientMsg(input.readLine());
                    }catch (Exception e){
                        Log.i(TAG_SERVER,e.getMessage());
                    }
                }
            }
        }).start();
    }

}
