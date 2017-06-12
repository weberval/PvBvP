package de.dhbw_loerrach.pvbvp.function;


import android.util.Log;

import de.dhbw_loerrach.pvbvp.Main;
import de.dhbw_loerrach.pvbvp.gui.GameView;
import de.dhbw_loerrach.pvbvp.screens.Screen;

/**
 * Created by Renat on 06.04.2017.
 * Class that manages the game
 */
public class GameController extends Thread {

	private static final String TAG = "GameController";

	/**
	 * The pause between each mainLoop routine in milliseconds
	 */
	private static final int WAIT = 50;

	/**
	 * Only every BALL_DELAY Ticks the ball will be moved.
	 */
	private static int BALL_DELAY = 10;


	private Main main;
	private GameView view;

	public boolean RUNNING = false;
	private int ball_counter;

	/**
	 * level of current game. higher level -> higher difficulty
	 */
	private int level;

	/**
	 * counter of points
	 */
	public static int[] points;

	public GameController(Main main, GameView view) {
		this.main = main;
		this.view = view;

		points = new int[]{0,0};
		level = 1;

		World.setController(this);
		if(Main.MODE == Screen.LOCAL)
			World.init(level);

	}

	/**
	 * starting the thread
	 */
	public void run() {
		RUNNING = true;
		mainLoop();
	}

	/**
	 * mainloop of the game.
	 */
	public void mainLoop() {

		view.update();
		wait_(1000);
		while(RUNNING) {
			action();
			wait_(WAIT);
			view.update();
		}
	}


	/**
	 * This method waits WAIT milliseconds between each routine
	 */
	public void wait_(int wait) {
		try {
			sleep(wait);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}


	/**
	 * This method checks for any event happening ingame.
	 */
	public void action() {
		if(ball_counter == 0){
			World.ball.move();
			view.ballMovementUpdate();
			ball_counter = BALL_DELAY;
		}else ball_counter--;
	}

	/**
	 * moves the panel on world
	 *
	 * @param player
	 * @param dir
	 */
	public void movePanel(int player, char dir) {
		World.movePanel(player, dir);
	}


	public void gameOver(PanelPlayer ply){
		//TODO: do some player related work
		Log.i(TAG,"Game Over");
		Screen.TYPE = Screen.END;
		main.gameOver();
	}

	public void win(PanelPlayer ply){
		points[ply.index]++;
        Log.i(TAG,"Player " + ply + " wins!");
        levelUp();
    }

	/**
	 * next level
	 */
	public void levelUp(){
		if(BALL_DELAY <= 1)
			Log.i(TAG,"Hardest Level reached!");
		else BALL_DELAY--;
		World.init(++level);
	}
}
