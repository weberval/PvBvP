package de.dhbw_loerrach.pvbvp.gui;

import android.util.Log;
import de.dhbw_loerrach.pvbvp.function.GameController;

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
	private GameController gameController;
	
	public TouchHandler(GameController gameController) {
		
		this.gameController = gameController;
		
		VER_FENCE = GameView.SCREEN_WIDTH / 2;
		HOR_FENCE = GameView.SCREEN_HEIGHT / 2;
	}
	
	/**
	 * Will be called when the screen is touched / swiped at x, y
	 *
	 * @param x Coordinate in pixel
	 * @param y Coordinate in pixel
	 */
	public void action(float x, float y) {
		Log.i(TAG, "Screen touched at: x " + x + " y: " + y);
		//TEST
		movePanel(1, 'r');
	}
	
	/**
	 * moves the panel on gameController
	 *
	 * @param player
	 * @param dir
	 */
	public void movePanel(int player, char dir) {
		gameController.movePanel(player, dir);
	}
	
	
}
