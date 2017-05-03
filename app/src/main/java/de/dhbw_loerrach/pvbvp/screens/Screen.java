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

    private static final String GAME_NAME = "The Game Ultimate Deluxe 3000";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen);

        title = (TextView) findViewById(R.id.screen_title);
        play_button = (Button) findViewById(R.id.screen_button);

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
            RelativeLayout rl = ((RelativeLayout)findViewById(R.id.screen_layout));
            //RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)rl.getLayoutParams();
            //rlp.addRule(RelativeLayout.CENTER_IN_PARENT,closeApp.getId());
            //rlp.addRule(RelativeLayout.BELOW,((Button)findViewById(R.id.screen_button)).getId());
            rl.addView(closeApp);

        }

        Log.i(TAG,"created");
    }

    public void startGame(View view){
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
