package de.dhbw_loerrach.pvbvp;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import de.dhbw_loerrach.pvbvp.Network.Protocol;
import de.dhbw_loerrach.pvbvp.function.GameController;
import de.dhbw_loerrach.pvbvp.gui.GameView;
import de.dhbw_loerrach.pvbvp.gui.TouchHandler;
import de.dhbw_loerrach.pvbvp.screens.Screen;
import de.dhbw_loerrach.pvbvp.sound.Soundeffects;

/**
 * Start of application.
 * Only activity for the game.
 */
public class Main extends Activity {
	
	private static final String TAG = "MAIN";

	public static boolean PORTAL = true;
	public static String MODE;


	private GameController gameController;
	private GameView gameView;
	private View.OnTouchListener touchListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);

		Protocol.main = this;
		Soundeffects.init(this);
		
		//set actionbar and navigationbar invisible
		final View decorView = getWindow().getDecorView();
		final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		
		gameView = new GameView(this, getWindowDim());
		
		//set this layout / view to fullscreen
		gameView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.game_layout);
		layout.getLayoutParams().height = getWindowDim().y;
		layout.addView(gameView);
		
		//setup listener to keep action and navigation -bar invisible when minimized and maximized again
		decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
					decorView.setSystemUiVisibility(uiOptions);
				}
			}
		});
		
		//setting up touchlistener & handler
		touchListener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent me) {

				int index = MotionEventCompat.getActionIndex(me);
				//Log.i(TAG,"index of event " + index);


				//Log.i(TAG,"Event "+MotionEvent.actionToString(me.getAction()) + " as int " + me.getAction());
				if (me.getAction() == MotionEvent.ACTION_UP || me.getAction() == 262
						|| me.getAction() == 261 || me.getAction() == MotionEvent.ACTION_DOWN) {
					//touchHandler.action(me.getX(index), me.getX(index),me);
					if(MODE.equals(Screen.LOCAL))
						TouchHandler.action(MotionEventCompat.getX(me,index),MotionEventCompat.getY(me,index),me);
					else TouchHandler.action_remote(MotionEventCompat.getX(me,index),me);
					//touchHandler.action(me.getX(),me.getY(),me);
					//Log.i(TAG,""+MotionEventCompat.getX(me,index) + " is ? " + me.getX(index) + " is ? " + me.getX());
				}
				return true;
			}
		};
		gameView.setOnTouchListener(touchListener);

		
		gameController = new GameController(this, gameView);
		TouchHandler.setup(gameController);

		gameController.start();
		
		
		Log.i(TAG, "created");
	}
	
	
	/**
	 * Calculates window dimensions
	 *
	 * @return point.x = screen width, point.y = height
	 */
	public Point getWindowDim() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return new Point(metrics.widthPixels, metrics.heightPixels);
	}

	public void gameOver(){
		gameController.RUNNING = false;
		Intent intent = new Intent(this,Screen.class);
		startActivity(intent);
		finish();
	}
}
