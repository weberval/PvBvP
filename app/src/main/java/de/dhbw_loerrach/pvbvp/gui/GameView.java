package de.dhbw_loerrach.pvbvp.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import de.dhbw_loerrach.pvbvp.function.Ball;
import de.dhbw_loerrach.pvbvp.function.GameObj;
import de.dhbw_loerrach.pvbvp.function.Panel;
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

    /**
     * ball to be drawn
     */
    private Ball ball;

    /**
     * panels to be drawn
     */
    private Panel[] panels;

    private Paint paint;
    //private Paint paintDrawFrame;


    private ImageLoader imageLoader;
    private Bitmap ballBM;
    private Bitmap brickLeft;
    private Bitmap brickRight;
    private Bitmap masterLeft;
    private Bitmap masterRight;
    private Bitmap panel;
    private Bitmap air;

    public GameView(Context context, Point windowDim) {
        super(context);

        SCREEN_WIDTH = windowDim.x;
        SCREEN_HEIGHT = windowDim.y;

        blockWidth = (float) (SCREEN_WIDTH / World.PLAYGROUND_WIDTH  );
        blockHeight = (float) (SCREEN_HEIGHT / World.PLAYGROUND_HEIGHT );

        imageLoader = new ImageLoader(context);
        ballBM = imageLoader.getImage(GameObjImage.BALL,(int)blockWidth,(int)blockHeight);
        brickLeft = imageLoader.getImage(GameObjImage.BRICK_LEFT,(int)blockWidth,(int)blockHeight);
        brickRight = imageLoader.getImage(GameObjImage.BRICK_RIGHT,(int)blockWidth,(int)blockHeight);
        masterLeft = imageLoader.getImage(GameObjImage.MASTER_LEFT,(int)blockWidth,(int)blockHeight);
        masterRight = imageLoader.getImage(GameObjImage.MASTER_RIGHT,(int)blockWidth,(int)blockHeight);
        panel = imageLoader.getImage(GameObjImage.PANEL,(int)blockWidth*7,(int)blockHeight);
        air = imageLoader.getImage(GameObjImage.AIR, (int)blockWidth*2,(int)blockHeight);

        paint = new Paint();

        Log.i(TAG,"created");
    }

    /**
     * The gameview will be updated and redrawn.
     */
    public void update(GameObj[][] world, Ball ball, Panel[] panels){

        this.world = world;
        this.panels = panels;
        this.ball = ball;
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        //draw playground
        for(int i = 0; i < world.length; i ++){
            for(int j = 0; j < world[i].length; j ++){

                switch (world[i][j].getType()){
                    case BRICK:
                        if(world[i][j].getSide() == 'r') {
                            canvas.drawBitmap(brickRight, i * blockWidth, j * blockHeight, paint);
                        }else {
                            canvas.drawBitmap(brickLeft, i * blockWidth, j * blockHeight, paint);
                        }
                        break;
                    case MASTER:
                        if(world[i][j].getSide() == 'r') {
                            canvas.drawBitmap(masterRight, i * blockWidth, j * blockHeight, paint);
                        }else {
                            canvas.drawBitmap(masterLeft, i * blockWidth, j * blockHeight, paint);
                        }
                        break;
                    case AIR:
                        canvas.drawBitmap(air,i * blockWidth, j * blockHeight, paint);
                }
            }
        }

        //draw ball
        //canvas.drawBitmap(ballBM,ball.x * blockWidth, ball.y * blockHeight, paint);

        //draw panels
        for(int i = 0; i < panels.length; i ++)
        canvas.drawBitmap(panel,panels[i].getX() * blockWidth, panels[i].getY() * blockHeight, paint);
    }
}
