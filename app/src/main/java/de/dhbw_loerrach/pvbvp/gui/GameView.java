package de.dhbw_loerrach.pvbvp.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import de.dhbw_loerrach.pvbvp.function.GameObj;
import de.dhbw_loerrach.pvbvp.function.World;

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
    private float blockWidth;

    /**
     *  Height of one single block in pixel.
     */
    private float blockHeight;

    /**
     * The game world to be drawn
     */
    private GameObj[][] world;

    private Paint paint;


    public GameView(Context context, Point windowDim) {
        super(context);

        SCREEN_WIDTH = windowDim.x;
        SCREEN_HEIGHT = windowDim.y;

        blockWidth = (float) (SCREEN_WIDTH / World.PLAYGROUND_WIDTH  );
        blockHeight = (float) (SCREEN_HEIGHT / World.PLAYGROUND_HEIGHT );

        paint = new Paint();

        Log.i(TAG,"created");
    }

    /**
     * The gameview will be updated and redrawn.
     */
    public void update(GameObj[][] world){

        this.world = world;
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        for(int i = 0; i < world.length; i ++){
            for(int j = 0; j < world[i].length; j ++){

                switch (world[i][j].getType()){
                    case "ball":

                        paint.setColor(Color.CYAN);
                        canvas.drawRect(i * blockWidth, j * blockHeight, i * blockWidth + blockWidth, j * blockHeight + blockHeight,paint);
                        Log.i(TAG,"ball drawn");

                        break;
                    case "brick":
                        switch (world[i][j].getSide()) {
                            case 'l':
                                paint.setColor(Color.BLUE);
                                canvas.drawRect(i * blockWidth, j * blockHeight, i * blockWidth + (blockWidth * 2), j * blockHeight + blockHeight, paint);
                                Log.i(TAG,"brick drawn");

                                break;
                            case 'r':
                                paint.setColor(Color.BLUE);
                                canvas.drawRect(i * blockWidth, j * blockHeight, i * blockWidth + (blockWidth * 2), j * blockHeight + blockHeight, paint);
                                Log.i(TAG,"brick drawn");

                                break;
                        }

                        break;
                    case "master":

                        paint.setColor(Color.YELLOW);
                        canvas.drawRect(i * blockWidth, j * blockHeight, i * blockWidth + blockWidth, j * blockHeight + blockHeight,paint);
                        Log.i(TAG,"master drawn");

                        break;
                    case "panel":

                        paint.setColor(Color.MAGENTA);
                        canvas.drawRect(i * blockWidth, j * blockHeight, i * blockWidth + blockWidth, j * blockHeight + blockHeight,paint);
                        Log.i(TAG,"panel drawn");

                        break;
                    case "air":
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(i * blockWidth, j * blockHeight, i * blockWidth + blockWidth, j * blockHeight + blockHeight,paint);
                        Log.i(TAG,"air drawn");
                        //Nothing, just black
                        break;
                }

            }
        }
    }
}
