package de.dhbw_loerrach.pvbvp.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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

        RelativeLayout rl = ((RelativeLayout)findViewById(R.id.screen_layout));

        if(TYPE == 's'){
            title.setText(GAME_NAME);
            play_button.setText("Start");

        }
        if(TYPE == 'e'){
            title.setText("Game Over!");
            play_button.setText("Replay");

            Button closeApp = new Button(this);
            closeApp.setText("Close");
            closeApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.exit(0);
                }
            });
            //RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)rl.getLayoutParams();
            //rlp.addRule(RelativeLayout.CENTER_IN_PARENT,closeApp.getId());
            //rlp.addRule(RelativeLayout.BELOW,((Button)findViewById(R.id.screen_button)).getId());
            rl.addView(closeApp);

        }

        Log.i(TAG,"created");
    }

    public void startGame(View view){

        String mode = mode_button.getText().toString();
        Main.MODE = mode;
        switch (mode){
            case SERVER:
                //display server waiting... message
                play_button.setEnabled(false); //will be enabled when connected
                Networking.startServerReceiver();
                break;
            case CLIENT:
                play_button.setEnabled(false); //will not be enabled if you're the client. game starts automatically when the INIT from server is received.
                //display client connecting message
                Networking.startClientReceiver();
                Networking.startClient();
                break;
        }
        /*
        if(Main.MODE.equals(SERVER)){
            //if this is the server, this button will cause the START message to be send to the client

        }
        */
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
        finish();
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

}
