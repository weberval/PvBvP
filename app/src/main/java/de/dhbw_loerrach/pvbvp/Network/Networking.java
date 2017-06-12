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


    public static boolean HB_STARTED;
    public static final int TIME_OUT = 5;
    public static int TIME_OUT_COUNTER = 0;
    public static final int SLEEP_TIME = 5000;

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
                            Log.i(TAG_SERVER, "READING : " + new String(buffer, "utf-8"));

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

    public static void heartbeat(){
        if (GOOD_TO_GO) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(RUNNING) {
                        try {
                            Thread.sleep(SLEEP_TIME);
                            TIME_OUT_COUNTER++;
                            if (TIME_OUT_COUNTER == TIME_OUT)
                                time_out();
                            else send((SERVER) ? Protocol.get_msg(Protocol.SRV_MSG_HBOK,null) : Protocol.get_msg(Protocol.CLT_MSG_HB,null));
                        } catch (Exception e) {
                            Log.e("HEARTBEAT","", e);
                        }
                    }
                }
            }).start();
        }
    }

    public static void time_out(){
        Log.i((SERVER) ? TAG_SERVER :  TAG_CLIENT,"time out!");
        RUNNING = false;
        Protocol.time_out();
    }

}
