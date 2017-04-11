package de.dhbw_loerrach.pvbvp.function;

import android.util.Log;
import de.dhbw_loerrach.pvbvp.Main;
import de.dhbw_loerrach.pvbvp.gui.GameView;

/**
 * Created by Renat on 06.04.2017.
 * Class that manages the game
 */
public class GameController extends Thread {
	
	private static final String TAG = "GameController";
	
	/**
	 * The pause between each mainLoop routine in milliseconds
	 */
	private static final int WAIT = 150;
	
	
	private Main main;
	private World world;
	private GameView view;
	
	public GameController(Main main, GameView view) {
		this.main = main;
		this.view = view;
		
		world = new World(this);
		world.init();
		view.update(world.playground, world.ball, world.panels);
		
		
		//world.panelMove(2, 'l');
		//view.update(world.playground);
	}
	
	/**
	 * starting the thread
	 */
	public void run() {
		mainLoop();
	}
	
	
	/**
	 * mainloop of the game.
	 */
	public void mainLoop() {
		
		for (; ; ) {
			
			action();
			wait_();
			view.update(world.playground, world.ball, world.panels);
		}
	}
	
	
	/**
	 * This method waits WAIT milliseconds between each routine
	 */
	public void wait_() {
		try {
			sleep(WAIT);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	
	/**
	 * This method checks for any event happening ingame.
	 * Collisions and removal of bricks and the winning condition : hitting the master brick
	 * Also changes levels and other
	 */
	public void action() {
	
	}
	
	/**
	 * moves the panel on world
	 *
	 * @param player
	 * @param dir
	 */
	public void movePanel(int player, char dir) {
		world.movePanel(player, dir);
	}
	
}
