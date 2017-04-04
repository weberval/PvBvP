package de.dhbw_loerrach.pvbvp.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

/**
 * Created by Renat on 04.04.2017.
 * Where the game is drawn
 */
public class GameView extends View {

    private static final String TAG = "GameView";

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;

    /**
     *  Width of one single block in pixel.
     */
    private int blockWidth;

    /**
     *  Height of one single block in pixel.
     */
    private int blockHeight;


    public GameView(Context context, Point windowDim) {
        super(context);

        SCREEN_WIDTH = windowDim.x;
        SCREEN_HEIGHT = windowDim.y;

        Log.i(TAG,"created");
    }

    /**
     * The gameview will be updated and redrawn.
     */
    public void update(){

        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //TEST
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(10,10,100,100,paint);
    }
}
