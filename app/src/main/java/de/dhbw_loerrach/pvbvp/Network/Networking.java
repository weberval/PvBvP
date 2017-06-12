package de.dhbw_loerrach.pvbvp.Network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import de.dhbw_loerrach.pvbvp.function.World;

/**
 * Class for handling the networking.
 *
 * Created by renat on 23.05.17.
 */

public class Networking {

    public static final String TAG_SERVER = "SERVER";
    public static final String TAG_CLIENT = "CLIENT";
    public static final String TAG = "NETWORKING";

    public static boolean SERVER = false;

    public static BluetoothAdapter badapter;
    private static BluetoothDevice device;

    public static final UUID PVBVP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //default ID
    public static BluetoothSocket socket;
    public static BluetoothServerSocket srv_socket;

    private static InputStream input;
    private static OutputStream output;
    private static byte[] buffer;

    public static boolean GOOD_TO_GO = false;
    public static boolean CLIENT_CONNECTED = false;


    public static int PACKET_LENGTH = 10000;
    public static boolean RUNNING = true;


    /**
     * setting up inital bluetooth
     */
    public static void setup_bluetooth(){
        buffer = new byte[PACKET_LENGTH];
        badapter = BluetoothAdapter.getDefaultAdapter();
        if(badapter == null){
            Log.i(TAG,"Bluetooth not supported");
            return;
        }
        if(!badapter.isEnabled()){
            Log.i(TAG,"Bluetooth not enabled");
            return;
        }
        //we only search for already bonded devices.
        Set<BluetoothDevice> list = badapter.getBondedDevices();
        if(list.size() >= 1){
            for(BluetoothDevice bd : list){
                device = bd;
            }
        }else{
            Log.i((SERVER) ? TAG_SERVER : TAG_CLIENT,"1 device must be paired");
            return;
        }

        //test
        if(device != null) {
            Log.i(TAG, "device recognized");
            Log.i(TAG,device.getName() + " : " + device.getAddress());
        }

        GOOD_TO_GO = true;
    }

    public static void send(String msg){
        if(GOOD_TO_GO) {
            try {
                output.write(msg.getBytes());
            } catch (Exception e) {
                Log.e((SERVER) ? TAG_SERVER : TAG_CLIENT, "", e);
            }
        }
    }

    public static void start_server_receiver(){
        SERVER = true;
        if(GOOD_TO_GO) {
            Log.i(TAG_SERVER, "start_server_receiver()");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        srv_socket = badapter.listenUsingRfcommWithServiceRecord("name", PVBVP_UUID);
                    } catch (Exception e) {
                        Log.e(TAG_SERVER, "", e);
                    }

                    while (true) {
                        try {
                            socket = srv_socket.accept();

                            input = socket.getInputStream();
                            output = socket.getOutputStream();
                        } catch (Exception e) {
                            Log.e(TAG_SERVER, "", e);
                            break;
                        }

                        if (socket != null)
                            break;
                    }

                    while (RUNNING) {
                        try {
                            input.read(buffer);
                            Protocol.clientMsg(new String(buffer, "utf-8"));
                        } catch (Exception e) {
                            Log.e(TAG_SERVER, "", e);
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * connect to server
     */
    public static void start_client(){
        if(GOOD_TO_GO) {
            socket = null;
            try {
                socket = device.createRfcommSocketToServiceRecord(PVBVP_UUID);
            } catch (Exception e) {
                Log.e(TAG_CLIENT, "start_client()", e);
                try {
                    socket.close();
                } catch (Exception e2) {
                    Log.e(TAG_CLIENT, "", e2);
                }
                return;
            }
            start_client_receiver();
            start_client_broadcast();
        }
    }

    public static void start_client_broadcast(){
        if(GOOD_TO_GO){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!CLIENT_CONNECTED){
                        send(Protocol.get_msg(Protocol.CLT_MSG_HELLO,null));
                        try {
                            Thread.sleep(1000);
                        }catch (Exception e){
                            Log.e(TAG_CLIENT,"",e);
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * listening to server
     */
    public static void start_client_receiver(){
        if (GOOD_TO_GO) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket.connect();
                        input = socket.getInputStream();
                        output = socket.getOutputStream();
                    }catch (Exception e){
                        Log.e(TAG_CLIENT,"",e);
                    }
                    while(RUNNING){
                        try{

                            input.read(buffer);
                            Log.i(TAG_CLIENT,"READING : " + new String(buffer,"utf-8"));
                            Protocol.serverMsg(new String(buffer,"utf-8"));

                        }catch (Exception e){
                            Log.i(TAG_CLIENT,"",e);
                            break;
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * thread that sends the update to the client
     */
    public static void game_updater(){
        if(GOOD_TO_GO){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(World.gameController.RUNNING){


                        //TODO Add Array to World for destroyed bricks to update
                        Protocol.send_msg(Protocol.SRV_MSG_UPDATE,new String[]{
                                Integer.toString(World.ball.getX()),
                                Integer.toString(World.ball.getY())
                                });

                        try{
                            Thread.sleep(Protocol.GAME_UPDATE);
                        }catch (Exception e){
                            Log.e(TAG,"",e);
                        }
                    }
                }
            }).start();
        }
    }
}
