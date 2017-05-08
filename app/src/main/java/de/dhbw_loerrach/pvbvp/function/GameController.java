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

	private static int BALL_DELAY = 3;


	private Main main;
	private World world;
	private GameView view;

	private boolean RUNNING = false;
	private int ball_counter;

	/**
	 * level of current game. higher level -> higher difficulty
	 */
	private int level;

	public GameController(Main main, GameView view) {
		this.main = main;
		this.view = view;

		level = 1;

		world = new World(this);
		world.init(level);

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
	 * In case of gameover, return to Main and handle there if a new game starts
	 */
	public void mainLoop() {
		while(RUNNING) {
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
		if(ball_counter == 0){
			world.ball.move(world,world.panels[0],world.panels[1]);
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
		world.movePanel(player, dir);
	}


	/**
	 * reset game to level 1 (?)
	 */
	public void reset(){
		level = 1;
		world.init(level);
	}

	public void gameOver(PanelPlayer ply){
		//TODO: do some player related work
		Log.i(TAG,"Game Over");
		RUNNING = false;
		main.gameOver();
	}

	/**
	 * next level
	 */
	public void levelUp(){
		world.init(++level);
	}
}
