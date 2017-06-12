package de.dhbw_loerrach.pvbvp.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

import de.dhbw_loerrach.pvbvp.function.Ball;
import de.dhbw_loerrach.pvbvp.function.GameController;
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
	public static int numberOfTraces = 3;
	
	/**
	 * ball to be drawn
	 */
	private Ball ball;
	
	/**
	 * panels to be drawn
	 */
	private Panel[] panels;
	
	private Paint paint;
	private Paint paint_points;
	private Paint test_paint;

	/**
	 * positions for the point counter
	 */
	private int x_pos_ply1;
	private int y_pos_ply1;
	private int x_pos_ply2;
	private int y_pos_ply2;

	/**
	 * images for the traces (3)
	 */
	private Bitmap[] ballTraceImages;

	private ImageLoader imageLoader;
	private Bitmap ballBM;
	private Bitmap brickLeft;
	private Bitmap brickRight;
	private Bitmap brickDestructed1Left;
	private Bitmap brickDestructed2Left;
	private Bitmap brickDestructed3Left;
	private Bitmap brickDestructed1Right;
	private Bitmap brickDestructed2Right;
	private Bitmap brickDestructed3Right;
	private Bitmap master;
	private Bitmap panel;

	public GameView(Context context, Point windowDim) {
		super(context);
		
		SCREEN_WIDTH = windowDim.x;
		SCREEN_HEIGHT = windowDim.y;

		x_pos_ply1 = SCREEN_WIDTH-10;
		y_pos_ply1 = SCREEN_HEIGHT-10;
		x_pos_ply2 = 20;
		y_pos_ply2 = 30;

		blockWidth = (float) (SCREEN_WIDTH / World.PLAYGROUND_WIDTH);
		blockHeight = (float) (SCREEN_HEIGHT / World.PLAYGROUND_HEIGHT);
		
		imageLoader = new ImageLoader(context);
		ballBM = imageLoader.getImage(GameObjImage.BALL, (int) blockWidth, (int) blockHeight);
		brickLeft = imageLoader.getImage(GameObjImage.BRICK_LEFT, (int) blockWidth, (int) blockHeight);
		brickRight = imageLoader.getImage(GameObjImage.BRICK_RIGHT, (int) blockWidth, (int) blockHeight);
		brickDestructed1Left = imageLoader.getImage(GameObjImage.BRICK_DESTRUCTED_1_LEFT, (int) blockWidth, (int) blockHeight);
		brickDestructed2Left = imageLoader.getImage(GameObjImage.BRICK_DESTRUCTED_2_LEFT, (int) blockWidth, (int) blockHeight);
		brickDestructed3Left = imageLoader.getImage(GameObjImage.BRICK_DESTRUCTED_3_LEFT, (int) blockWidth, (int) blockHeight);
		brickDestructed1Right = imageLoader.getImage(GameObjImage.BRICK_DESTRUCTED_1_RIGHT, (int) blockWidth, (int) blockHeight);
		brickDestructed2Right = imageLoader.getImage(GameObjImage.BRICK_DESTRUCTED_2_RIGHT, (int) blockWidth, (int) blockHeight);
		brickDestructed3Right = imageLoader.getImage(GameObjImage.BRICK_DESTRUCTED_3_RIGHT, (int) blockWidth, (int) blockHeight);
		master = imageLoader.getImage(GameObjImage.MASTER, (int) blockWidth, (int) blockHeight);
		panel = imageLoader.getImage(GameObjImage.PANEL, (int) blockWidth * Panel.PANEL_WIDTH, (int) blockHeight);

		ballTraceImages = imageLoader.getBallTrace((int)blockWidth,(int)blockHeight);


		ballTrace = new LinkedList<>();

		paint = new Paint();

		paint_points = new Paint();
		paint_points.setColor(Color.WHITE);
		paint_points.setTextSize(35);

		test_paint = new Paint();
		test_paint.setColor(Color.GREEN);

		Log.i(TAG, "created");
	}
	
	/**
	 * The gameview will be updated and redrawn.
	 */
	public void update() {
		this.world = World.playground;
		this.panels = World.panels;
		this.ball = World.ball;
		postInvalidate();
	}


	public void ballMovementUpdate(){
		this.ball = World.ball;
		ballTrace.add(new int[]{ball.getX(),ball.getY()});
		if(ballTrace.size() > numberOfTraces) {
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
						case DESTUCTEDBRICK:
							switch (world[i][j].getSide()) {
								case 'r':
									switch (world[i][j].getDestructionStage()) {
										case 3:
											canvas.drawBitmap(brickDestructed1Right, i * blockWidth, j * blockHeight, paint);
											break;
										case 2:
											canvas.drawBitmap(brickDestructed2Right, i * blockWidth, j * blockHeight, paint);
											break;
										case 1:
											canvas.drawBitmap(brickDestructed3Right, i * blockWidth, j * blockHeight, paint);
											break;
										default: // For debugging purposes only
											canvas.drawRect(i * blockWidth, j * blockHeight, i * blockWidth + blockWidth, j * blockHeight + blockHeight, test_paint);
									}
									break;
								case 'l':
									switch (world[i][j].getDestructionStage()) {
										case 3:
											canvas.drawBitmap(brickDestructed1Left, i * blockWidth, j * blockHeight, paint);
											break;
										case 2:
											canvas.drawBitmap(brickDestructed2Left, i * blockWidth, j * blockHeight, paint);
											break;
										case 1:
											canvas.drawBitmap(brickDestructed3Left, i * blockWidth, j * blockHeight, paint);
											break;
										default: // For debugging purposes only
											canvas.drawRect(i * blockWidth, j * blockHeight, i * blockWidth + blockWidth, j * blockHeight + blockHeight, test_paint);
									}
							}
							break;
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

		canvas.save();
		canvas.drawText(""+ GameController.points[0],x_pos_ply1,y_pos_ply1,paint_points);
		canvas.rotate(180,x_pos_ply2,y_pos_ply2);
		canvas.drawText(""+ GameController.points[1],x_pos_ply2,y_pos_ply2,paint_points);
		canvas.restore();
	}
}
