package de.dhbw_loerrach.pvbvp.Network;

import java.net.DatagramPacket;

import de.dhbw_loerrach.pvbvp.function.GameController;
import de.dhbw_loerrach.pvbvp.function.World;

/**
 * Created by renat on 23.05.17.
 */
public class Protocol {

   public static short msg_ID = 0; // Short.MIN_VALUE;

   public static void clientMsg(DatagramPacket input){
      String data = input.getData().toString();
      // Short ID = toShort(data.substring(0,6));
      String ID = data.substring(0,6);
      String command = data.substring(7,9);
      data = data.substring(8);

      switch (command){
         case "INI":

      }
      // TODO: switch case for command decode
   }

   public static void serverMsg(DatagramPacket input){

   }

   /**
    * Msg-String encode:
    * 5*NUM_ID 3*Command x*Data
    *
    * NUM_ID < 32767
    * intit with MIN_VALUE
    *
    * Commands:
    * ACK = Acknowledged
    *    msg_ID + "ACK" + old_ID
    * INI = Initialisation
    *    msg_ID + "INI" + World.playground.toString()
    * MVP = Panel Move
    *    msg_ID + "MVP" + (player + direction)
    * PSB = Ball Position
    *    msg_ID + "PSB" + (X + Y)
    * TRM = Terminate Game
    *    msg_ID + "TRM"
    *
    */

   public static String getIDfixedString(){
      String output = msg_ID + "";
      String output2 = "";
      for (int i = 0; i < 6 - output.length();i++){
         output2 += "0";
      }
      return output2 + output;
   }

   public static void sendMsg(String command) {
      sendMsg(getIDfixedString() + command);
   }

   public static void sendMsg(String command, String data){
      Networking.send(getIDfixedString() + command + data);
   }

   // Utilities

   public static short toShort(String string) {
      char[] chars = string.toCharArray();
      short result = 0;
      int lenght = string.length();
      for (int i = 0; i < lenght; i++){
         result += (chars[i] - 30) * 10 ^ (lenght - i);
         /*
         switch (chars[i]){
            case '1':
               result += 1 * 10 ^ (lenght - i);
               break;
            case '2':
               result += 2 * 10 ^ (lenght - i);
               break;
            case '3':
               result += 3 * 10 ^ (lenght - i);
               break;
            case '4':
               result += 4 * 10 ^ (lenght - i);
               break;
            case '5':
               result += 5 * 10 ^ (lenght - i);
               break;
            case '6':
               result += 6 * 10 ^ (lenght - i);
               break;
            case '7':
               result += 7 * 10 ^ (lenght - i);
               break;
            case '8':
               result += 8 * 10 ^ (lenght - i);
               break;
            case '9':
               result += 9 * 10 ^ (lenght - i);
               break;
            case '0':
               result += 0 * 10 ^ (lenght - i);
               break;
         }
         */
      }
      return result;
   }
}
