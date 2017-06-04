package de.dhbw_loerrach.pvbvp.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.dhbw_loerrach.pvbvp.Main;
import de.dhbw_loerrach.pvbvp.Network.Networking;
import de.dhbw_loerrach.pvbvp.Network.Protocol;
import de.dhbw_loerrach.pvbvp.R;

/**
 * Screens
 * There is a Startscreen: Shows at the beginning and has a 'start' button
 * And Endscreen: After a gameover. May show some stats and gives the option the restart or to close the application
 *
 * Created by renat on 29.04.17.
 */
public class Screen extends Activity {

    public static final String TAG = "SCREEN";

    /**
     * type of the screen. Can be 's' for Startscreen, or 'e' for endscreen
     */
    public static char TYPE = 's';

    /**
     * Title of the screen. Either <Gamename>, or 'Game Over'
     */
    private TextView title;

    /**
     * Button for start / replaying
     */
    private Button play_button;

    /**
     * for server / client / local
     */
    private Button mode_button;

    private static final String GAME_NAME = "The Game Ultimate Deluxe 3000";

    public static final String SERVER = "Server";
    public static final String CLIENT = "Client";
    public static final String LOCAL = "Local";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen);

        title = (TextView) findViewById(R.id.screen_title);
        mode_button = (Button) findViewById(R.id.gamemode);
        mode_button.setText(LOCAL);
        play_button = (Button) findViewById(R.id.screen_button);

        Protocol.con = this.getApplicationContext();


        if(TYPE == 's'){
            title.setText(GAME_NAME);
            play_button.setText("Start");

        }
        if(TYPE == 'e'){
            title.setText("Game Over!");
            play_button.setText("Replay");
        }

        Log.i(TAG,"created");
    }

    /**
     * in case of a local game this button will function as expected, starting the game
     * in case this device is the server, pressing the button will cause the game to start (but freezed) and a message saying "waiting for other to connect"
     * will appear. when the other player connected, the game can be started with a button click
     * in case this device is the client, the game will start but freezed. As soon as the server sends the message the game starts
     * @param view
     */
    public void startGame(View view){

        String mode = mode_button.getText().toString();
        Main.MODE = mode;
        Intent intent;
        switch (mode){
            case SERVER:
                intent = new Intent(this,WaitScreen.class);
                WaitScreen.TEXT = "Server waiting...";
                startActivity(intent);

                Networking.startServerReceiver();
                break;
            case CLIENT:
                intent = new Intent(this,WaitScreen.class);
                WaitScreen.TEXT = "Client trying to connect...";
                startActivity(intent);

                Networking.startClientReceiver();
                Networking.startClient();
                break;
            case LOCAL:
                intent = new Intent(this,Main.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    /**
     * decides if the game is local, or remote (client / server)
     * @param v
     */
    public void gameMode(View v){
        String mode = mode_button.getText().toString();
        if(mode.equals(LOCAL))
            mode = CLIENT;
        else if(mode.equals(CLIENT))
            mode = SERVER;
        else if(mode.equals(SERVER))
            mode = LOCAL;
        mode_button.setText(mode);
    }

    public void close(View v){
        System.exit(0);
    }


    //------------------------------------
    public void debug_server(View view){
        Networking.startServerReceiver();
    }

    public void debug_client(View view){
        Networking.startClientReceiver();
        Networking.startClient();
    }
    //------------------------------------

}
