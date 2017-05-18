package de.dhbw_loerrach.pvbvp.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

import de.dhbw_loerrach.pvbvp.R;
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
	 * Width of one single block in pixel.
	 */
	private float blockWidth;
	
	/**
	 * Height of one single block in pixel.
	 */
	private float blockHeight;
	
	/**
	 * The game world to be drawn
	 */
	private GameObj[][] world;

	/**
	 * The trace of the ball to be drawn
	 */
	private LinkedList<int[]> ballTrace;

	/**
	 * Number of after images of the ball
	 */
	private int numberofTraces = 5;
	
	/**
	 * ball to be drawn
	 */
	private Ball ball;
	
	/**
	 * panels to be drawn
	 */
	private Panel[] panels;
	
	private Paint paint;
	

	private Bitmap[] ballTraceImages;

	private ImageLoader imageLoader;
	private Bitmap ballBM;
	private Bitmap brickLeft;
	private Bitmap brickRight;
	private Bitmap master;
	private Bitmap panel;

	public GameView(Context context, Point windowDim) {
		super(context);
		
		SCREEN_WIDTH = windowDim.x;
		SCREEN_HEIGHT = windowDim.y;
		
		blockWidth = (float) (SCREEN_WIDTH / World.PLAYGROUND_WIDTH);
		blockHeight = (float) (SCREEN_HEIGHT / World.PLAYGROUND_HEIGHT);
		
		imageLoader = new ImageLoader(context);
		ballBM = imageLoader.getImage(GameObjImage.BALL, (int) blockWidth, (int) blockHeight);
		brickLeft = imageLoader.getImage(GameObjImage.BRICK_LEFT, (int) blockWidth, (int) blockHeight);
		brickRight = imageLoader.getImage(GameObjImage.BRICK_RIGHT, (int) blockWidth, (int) blockHeight);
		master = imageLoader.getImage(GameObjImage.MASTER, (int) blockWidth, (int) blockHeight);
		panel = imageLoader.getImage(GameObjImage.PANEL, (int) blockWidth * Panel.PANEL_WIDTH, (int) blockHeight);

		ballTraceImages = imageLoader.getBallTrace((int)blockWidth,(int)blockHeight);


		ballTrace = new LinkedList<>();

		paint = new Paint();
		
		Log.i(TAG, "created");
	}
	
	/**
	 * The gameview will be updated and redrawn.
	 */
	public void update(GameObj[][] world, Ball ball, Panel[] panels) {
		this.world = world;
		this.panels = panels;
		this.ball = ball;
		postInvalidate();
	}


	public void ballMovementUpdate(Ball ball){
		ballTrace.add(new int[]{ball.getX(),ball.getY()});
		if(ballTrace.size() > numberofTraces) {
			ballTrace.pop();
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawRect(0,0,SCREEN_WIDTH,SCREEN_HEIGHT,paint);

		//draw playground
        if(world != null) {
            for (int i = 0; i < world.length; i++) {
                for (int j = 0; j < world[i].length; j++) {

                    switch (world[i][j].getType()) {
                        case BRICK:
                            if (world[i][j].getSide() == 'r') {
                                canvas.drawBitmap(brickRight, i * blockWidth, j * blockHeight, paint);
                            } else {
                                canvas.drawBitmap(brickLeft, i * blockWidth, j * blockHeight, paint);
                            }
                            break;
                        case MASTER:
                            canvas.drawBitmap(master, i * blockWidth, j * blockHeight, paint);
                            break;
                    }
                }
            }
        }

		if(ball != null)
			canvas.drawBitmap(ballBM,ball.getX() * blockWidth, ball.getY() * blockHeight, paint);


		if(panels != null) {
			for (int i = 0; i < panels.length; i++) {
				canvas.drawBitmap(panel, panels[i].getX() * blockWidth, panels[i].getY() * blockHeight, paint);
			}
		}

		for(int i = 0; i < ballTrace.size(); i ++) {
			int[] e = ballTrace.get(i);
			canvas.drawBitmap(ballTraceImages[i],e[0] * blockWidth,e[1] * blockHeight,paint);
		}


	}
}
