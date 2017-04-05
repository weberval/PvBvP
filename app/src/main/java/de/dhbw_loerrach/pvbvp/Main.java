package de.dhbw_loerrach.pvbvp;


import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import de.dhbw_loerrach.pvbvp.gui.GameView;
import de.dhbw_loerrach.pvbvp.gui.TouchHandler;

/**
 * Start of application.
 * Only activity for the game.
 */
public class Main extends Activity {

    private static final String TAG = "MAIN";

    private GameView gameView;
    private View.OnTouchListener touchListener;
    private TouchHandler touchHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        //set actionbar and navigationbar invisible
        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        gameView = new GameView(this,getWindowDim());

        //set this layout / view to fullscreen
        gameView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        LinearLayout layout = (LinearLayout) findViewById(R.id.game_layout);
        layout.getLayoutParams().height = getWindowDim().y;
        layout.addView(gameView);

        //setting up touchlistener & handler
        touchHandler = new TouchHandler();
        touchListener = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent me){
                //action will be executed only on release.
                if(me.getAction() == MotionEvent.ACTION_UP) {
                    touchHandler.action(me.getX(), me.getY());
                }
                return true;
            }
        };
        gameView.setOnTouchListener(touchListener);

        Log.i(TAG,"created");
    }


    /**
     *  Calculates window dimensions
     * @return point.x = screen width, point.y = height
     */
    public Point getWindowDim(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new Point(metrics.widthPixels,metrics.heightPixels);
    }
}
