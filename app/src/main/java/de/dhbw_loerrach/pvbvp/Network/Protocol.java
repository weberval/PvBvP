package de.dhbw_loerrach.pvbvp.Network;

import android.util.Log;

import java.net.DatagramPacket;


/**
 * Created by renat on 23.05.17.
 *
 * Schema:
 *  SIDE : MSG : DATA
 *
 *  ":" Being the separator
 *  "::" Being the secondary separator
 *
 *  SIDE is either "SERVER" or "CLIENT"
 *
 *  CLIENT SIDE:
 *  MSG is either
 *       "HELLO"  - Hello message (only sent from client)
 *                - DATA is none
 *       "DOWN"   - Touch event started
 *                - DATA is
 *                      - x (int) :: y (int)
 *       "UP"     - Touch event ended
 *                - DATA is none
 *       "HB"     - heartbeat, connection check
 *                - DATA is none
 *
 *
 *  SERVER SIDE:
 *  MSG is either
 *       "INIT"   - setting the initial state of the game, send from the server
 *                - DATA: Serialised GameObj Array (playground) :: x_ball (int) :: y_ball (int) :: ball_direction (int)
 *       "START"  - game starting (is activated by user)
 *                - DATA is none
 *       "UPDATE" - the updated playground
 *                - DATA is
 *                      - ball_x :: ball_y :: (destroyed_brick1_x,destroyed_brick1_y) :: (dbrick2_x,dbrick2_y) :: ...
 *       "WIN"    - game over, masterbrick was hit, next level automatically starts (new INIT)
 *                - DATA is
 *                      - player who won (int)
 *       "GAMEOVER" - game over, ball is out of bounds, new game automatically starts from level 1, (new INIT)
 *                  - DATA is none (or player who lost?)
 *       "HB_OK"  - heartbeat answer, connection check
 *                - DATA is none
 *   -----------------------------------------------------------------------------------------------------------------------
 *   EXAMPLE:
 *
 *   User A starts a game as the server (choosen by user)
 *   Server is running and waiting...
 *
 *   User B starts a game as client
 *   Client sends broadcasts out
 *       "CLIENT:HELLO" ... "CLIENT:HELLO"
 *
 *   server receives the hello packets and replies with INIT
 *       "SERVER:INIT:<javaobject>::ball_x::ball_y::ball_dir"
 *
 *   client receives the INIT and prepares the game, informs the user and starts to send out HB messages (to server)
 *       "CLIENT:HB" ... CLIENT:HB"
 *
 *   server replies to each HB
 *       "SERVER:HB_OK" ... "SERVER:HB_OK"
 *
 *   (HB and HB_OK messages are treated as a separated connection, in separate threads)
 *
 *   when the server is ready, it sends the start
 *       "SERVER:START"
 *
 *   From now on the game runs on the server and the receives client input:
 *       "CLIENT:DOWN:x_pos::y_pos" .... "CLIENT:UP"
 *   Those are received asynchronously
 *
 *   The server sends an update of the playground in fixed timesteps
 *       "SERVER:UPDATE:ball_x::ball_y" ... "SERVER:UPDATE:ball_x::ball_y::(somex,somey)::(somex,somey)"
 *
 *   When the game ends (in either way)
 *       "SERVER:WIN:player_id" OR "SERVER:GAMEOVER"
 *   The client changes its inner states (increment level or setting it back...) and waits for a new INIT from the server
 *
 *   The server sends a new INIT in a fixed amount of time, automatically and the game continues.
 *   -----------------------------------------------------------------------------------------------------------------------
 *
 *   HEARTBEAT:
 *
 *   Client side: if no "HB_OK" message is received for 3 "HB" messages sent out, the user will be informed of a time out, and the current
 *   (frozen) game will be interrupted. user can restart the client (button press "restart?")
 *
 *   Server side: if no "HB" message is received for 3 "HB_OK" messages sent out, "" "" . the game resets, the user can stop/restart server
 */
public class Protocol {

   public static final String TAG = "PROTOCOL";

   private static final String SEPARATOR = ":";
   private static final String DATA_SEPARATOR = "::";

   public static final int SRV_MSG_INIT = 0;
   public static final int SRV_MSG_START = 1;
   public static final int SRV_MSG_UPDATE = 2;
   public static final int SRV_MSG_WIN = 3;
   public static final int SRV_MSG_GAMEOVER = 4;
   public static final int SRV_MSG_HBOK = 5;

   public static final int CLT_MSG_HELLO = 0;
   public static final int CLT_MSG_DOWN = 1;
   public static final int CLT_MSG_UP = 2;
   public static final int CLT_MSG_HB = 3;

   public static final int SERVER = 0;
   public static final int CLIENT = 1;

   private static int type;
   private static DatagramPacket packet;

   /**
    * server side
    * every message received from the client
    * @param input
    */
   public static void clientMsg(DatagramPacket input) {
      packet = input;
      String[] msg;
      try {
         msg = new String(input.getData(),"utf-8").split(SEPARATOR);
         type = Integer.parseInt(msg[1]);
      }catch (Exception e){
         Log.i(TAG,"CORRUPT PACKET from CLIENT: " + e.getMessage());
         return;
      }
      switch (type) {
         case CLT_MSG_HELLO:
            cltHello();
            break;
         case CLT_MSG_HB:
            cltHb();
            break;
         case CLT_MSG_UP:
            cltUp();
            break;
         case CLT_MSG_DOWN:
            if (msg.length == 3)
               cltDown(msg[2]);
            else
               Log.i(TAG, "CORRUPT PACKET: CLT_MSG_DOWN");
            break;
      }
   }


   /**
    * client side
    * every message received from the server
    * @param input
    */
   public static void serverMsg(DatagramPacket input){
      packet = input;
      String[] msg;
      try {
         msg = new String(input.getData(),"utf-8").split(SEPARATOR);
         type = Integer.parseInt(msg[1]);
      }catch (Exception e){
         Log.i(TAG,"CORRUPT PACKET from SERVER" + e.getMessage());
         return;
      }
      switch (type){
         case SRV_MSG_UPDATE:
            if(msg.length == 3)
               srvUpdate(msg[2]);
            else
               Log.i(TAG,"CORRUPT PACKET: SRV_MSG_UPDATE");
            break;
         case SRV_MSG_HBOK:
            srvHbOK();
            break;
         case SRV_MSG_INIT:
            if(msg.length == 3)
               srvInit(msg[2]);
            else
               Log.i(TAG,"CORRUPT PACKET: SRV_MSG_INIT");
            break;
         case SRV_MSG_START:
            srvStart();
            break;
         case SRV_MSG_WIN:
            if(msg.length == 3)
               srvWin(msg[2]);
            else
               Log.i(TAG,"CORRUPT PACKET: SRV_MSG_WIN");
            break;
         case SRV_MSG_GAMEOVER:
            if(msg.length == 3)
               srvGameover(msg[2]);
            else
               Log.i(TAG,"CORRUPT PACKET: SRV_MSG_GAMEOVER");
            break;
      }
   }


   /**
    * server side
    */

   /**
    * Server received a signal from a client (maybe for the first time)
    * if it's not for the fist time, ignore it. we only accept one client (the one we're playing with)
    * send an INIT back
    */
   private static void cltHello(){
      if(Networking.partnerAddress == null) {
         Networking.partnerAddress = packet.getAddress();
         Networking.send(createMsg(SRV_MSG_INIT,new String[]{"TESTDATA"}));
      }
   }

   private static void cltHb(){

   }

   private static void cltUp(){

   }

   private static void cltDown(String para){

   }


   /**
    * client side
    */


   private static void srvUpdate(String para){

   }

   private static void srvHbOK(){

   }

   /**
    * An INIT was received from the server
    *
    */
   private static void srvInit(String para){
      if(Networking.partnerAddress == null) {
         Networking.partnerAddress = packet.getAddress();
         Networking.CLIENT_CONNECTED = true;

         //start heartbeating etc...
      }
   }

   private static void srvStart(){

   }

   private static void srvWin(String para){

   }

   private static void srvGameover(String para){

   }


   /**
    * creating a valid message
    */
   public static String createMsg(int type,String[] data){
      int sender = (Networking.SERVER) ? SERVER : CLIENT;
      String result = Integer.toString(sender) + SEPARATOR + Integer.toString(type);
      if(data != null) {
         for (String s : data) {
            result += s + DATA_SEPARATOR;
         }
      }
      return result;
   }
}