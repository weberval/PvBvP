package de.dhbw_loerrach.pvbvp.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.dhbw_loerrach.pvbvp.Main;
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen);

        title = (TextView) findViewById(R.id.screen_title);
        play_button = (Button) findViewById(R.id.screen_button);

        if(TYPE == 's'){
            title.setText("Welcome to PvBvP!");
            play_button.setText("Start");
        }
        if(TYPE == 'e'){
            title.setText("Game Over!");
            play_button.setText("Replay");

            //Additional Button for Endscreen to close the app, TODO
            Button closeApp = new Button(this);

        }

        Log.i(TAG,"created");
    }

    public void startGame(View view){
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
