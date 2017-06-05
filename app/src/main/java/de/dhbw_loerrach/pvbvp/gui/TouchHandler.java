package de.dhbw_loerrach.pvbvp.gui;

import android.util.Log;
import android.view.MotionEvent;

import de.dhbw_loerrach.pvbvp.function.GameController;
import de.dhbw_loerrach.pvbvp.function.PanelPlayer;

/**
 * Created by Renat on 05.04.2017.
 * This class handles the user input
 */
public class TouchHandler {
	
	private static final String TAG = "TouchHandler";

	/**
	 * Vertical Fence, splits the screen through half vertically.
	 * Any touch smaller than VER_FENCE is accounted to player 1, any touch larger to player 2.
	 */
	private static int VER_FENCE;

	/**
	 * Horizontal Fence, splits the screen though half horizontally.
	 * Any touch smaller than HOR_FENCE is accounted the left, any touch larger to the right side.
	 */
	private static int HOR_FENCE;

	private static GameController gameController;

	private static boolean playerPanelThread[];


	public static void setup(GameController gameController_){
		gameController = gameController_;

		playerPanelThread = new boolean[2];
		VER_FENCE = GameView.SCREEN_WIDTH / 2;
		HOR_FENCE = GameView.SCREEN_HEIGHT /2;
	}
	
	/**
	 * Will be called when the screen is touched / swiped at x, y
	 * for local players
	 *
	 * @param x Coordinate in pixel
	 * @param y Coordinate in pixel
	 */
	public static void action(float x, float y, MotionEvent me) {

		int p = 2;
		char dir =' ';

		if (x < VER_FENCE && y < HOR_FENCE){
			p = PanelPlayer.PLAYER1.index;
			dir = 'l';
		}else if (x > VER_FENCE && y < HOR_FENCE){
			p = PanelPlayer.PLAYER1.index;
			dir = 'r';
		}else if(x < VER_FENCE && y > HOR_FENCE){
			p = PanelPlayer.PLAYER2.index;
			dir = 'l';
		}else if(x > VER_FENCE && y > HOR_FENCE) {
			p = PanelPlayer.PLAYER2.index;
			dir = 'r';
		}


		if(me.getAction() == MotionEvent.ACTION_DOWN || me.getAction() == MotionEvent.ACTION_POINTER_DOWN || me.getAction() == 261){
			Log.i(TAG,"start thread for " + p + " in direction " + dir);
			//start thread to move
			playerPanelThread[p] = true;
			startThread(p,dir);
		}

		if(me.getAction() == MotionEvent.ACTION_UP || me.getAction() == MotionEvent.ACTION_POINTER_UP || me.getAction() == 262 || me.getAction() == 6){
			Log.i(TAG,"end thread for " + p + " in direction " + dir);
			//end thread
			playerPanelThread[p] = false;
		}
	}

	/**
	 * touch handling for remote games, y doesn't matter since only one player is on the device
	 * you are always player 0 on your device
	 * @param x
	 * @param me
	 */
	public static void action_remote(float x, MotionEvent me){
		char dir = ' ';

		if(x < VER_FENCE)
			dir = 'l';
		else dir = 'r';

		if(me.getAction() == MotionEvent.ACTION_DOWN || me.getAction() == MotionEvent.ACTION_POINTER_DOWN){
			playerPanelThread[0] = true;
			startThread(0,dir);
		}

		if(me.getAction() == MotionEvent.ACTION_UP || me.getAction() == MotionEvent.ACTION_POINTER_UP){
			playerPanelThread[0] = false;
		}
	}

	public static void startThread(final int player, final char dir){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(playerPanelThread[player]) {
					movePanel(player, dir);
					try{Thread.sleep(50);}catch (Exception e){}
				}
			}
		});
		thread.start();
	}

	/**
	 * only called by the server, simulates the input of the client
	 */
	public static void client_action(float x, char updown){
		if(updown == 'd'){
			playerPanelThread[1] = true;
			startThread(1,(x < VER_FENCE) ? 'l' : 'r'); //switch or not?? testing
		}
		if(updown == 'u'){
			playerPanelThread[1] = false;
		}
	}

	
	/**
	 * moves the panel on gameController
	 *
	 * @param player
	 * @param dir
	 */
	public static void movePanel(int player, char dir) {
		gameController.movePanel(player, dir);
	}
	
	
}
