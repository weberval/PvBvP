package de.dhbw_loerrach.pvbvp.function;

import android.util.Log;

/**
 * Created by weva on 04.04.2017.
 * const PLAYGROUND_WIDTH has to be odd
 * const PLAYGROUND_HEIGHT has to be even
 * const PANEL_WITH has to be even
 */

public class World {


	public static final int PLAYGROUND_WIDTH = 27;
	public static final int PLAYGROUND_HEIGHT = 16;
	public static final int PLAYGROUND_OFFSET_X = 3;
	public static final int PLAYGROUND_OFFSET_Y = 3;
	static final int PLAYGROUND_CENTER_X = PLAYGROUND_WIDTH / 2 + 1;
	static final int PLAYGROUND_CENTER_Y_FLOOR = (PLAYGROUND_HEIGHT - 1) / 2;
	private static final String TAG = "WORLD";
	private static final int PLAYGROUND_BRICK_SAFE_X = 5;
	private static final int PLAYGROUND_BRICK_SAFE_Y = 3;
	public static int brickCount = 100;

	public static GameObj[][] playground;
	public static Ball ball;
	public static Panel[] panels;

	private GameController gameController;


	public World(GameController gameController) {
		this.gameController = gameController;
	}


	/**
	 * TODO(?): depending on level, the blocks are set and where the ball spawns.
	 * (for example: all even level start with player1, all odd level start with player2)
	 *
	 * @param level
	 */
	public void init(int level) {
		playground = new GameObj[PLAYGROUND_WIDTH][PLAYGROUND_HEIGHT];
		for (int i = 0; i < PLAYGROUND_WIDTH; ++i) {
			for (int j = 0; j < PLAYGROUND_HEIGHT; ++j) {
				playground[i][j] = new Air();
			}
		}

		//add panels to world
		panels = new Panel[2];
		panels[0] = new Panel(PanelPlayer.PLAYER1);
		panels[1] = new Panel(PanelPlayer.PLAYER2);

		ball = new Ball(PanelPlayer.PLAYER1.index);

		brickCreate();
		masterBrickCreate();

	}

	private void brickCreate() {
		/**
		 * @param x line indent iterating through 0 and 1
		 */
		for (int i = PLAYGROUND_OFFSET_Y; i < PLAYGROUND_HEIGHT - PLAYGROUND_OFFSET_Y; ++i) {
			for (int j = PLAYGROUND_OFFSET_X + ((i + 1) % 2); j < PLAYGROUND_WIDTH - PLAYGROUND_OFFSET_X; j += 2) {
				double random = 0;
				if (j < PLAYGROUND_CENTER_X - PLAYGROUND_BRICK_SAFE_X || j >= PLAYGROUND_CENTER_X + PLAYGROUND_BRICK_SAFE_X || i < PLAYGROUND_CENTER_Y_FLOOR - PLAYGROUND_BRICK_SAFE_Y || i >= PLAYGROUND_CENTER_Y_FLOOR + PLAYGROUND_BRICK_SAFE_Y) {
					random = Math.random();
				}
				if (random < 0.5) {
					this.playground[j][i] = new Brick(GameObjType.BRICK, 'l');
					this.playground[j + 1][i] = new Brick(GameObjType.BRICK, 'r');
				}
			}
		}

		/**
		 * @deprecated alternative way to create blocks
		 */
        /*
        for (int i = 0; i < brickCount; ++i) {
            int x = PLAYGROUND_OFFSET_X + (int)((Math.random() - .1) * (PLAYGROUND_WIDTH - PLAYGROUND_OFFSET_X));
            int y = PLAYGROUND_OFFSET_Y + (int)((Math.random() - .1) * (PLAYGROUND_HEIGHT - PLAYGROUND_OFFSET_Y));
            switch (this.playground[x][y].getType()) {
                case AIR:
                    int offset = (x % 2) - (y % 2);
                    this.playground[x + offset][y] = new Brick(GameObjType.BRICK, 'l');
                    this.playground[x + offset + 1][y] = new Brick(GameObjType.BRICK, 'r');
                    break;
                default:
                    --i;
            }
        } */
	}

	public void masterBrickCreate() {
		this.brickDestroy(PLAYGROUND_CENTER_X, PLAYGROUND_CENTER_Y_FLOOR);
		this.brickDestroy(PLAYGROUND_CENTER_X, PLAYGROUND_CENTER_Y_FLOOR + 1);

		playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR] = new Brick(GameObjType.MASTER, 'm');
		playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR + 1] = new Brick(GameObjType.MASTER, 'm');
	}

	public void brickDestroy(int x, int y) {
		switch (this.playground[x][y].getSide()) {
			case 'l':
				this.playground[x + 1][y] = new Air();
				break;
			case 'r':
				this.playground[x - 1][y] = new Air();
				break;
		}
		if(playground[x][y].type == GameObjType.BRICK)
			playground[x][y] = new Air();
	}

	public void movePanel(int player, char dir) {
		if (player < 0 || player > 2) {
			return;
		}

		if (dir == 'l') {
			panels[player].moveLeft();
		} else if (dir == 'r') {
			panels[player].moveRight();
		}
	}

	public GameObjType collisionCheck(int x, int y, Panel panel) {
		if (y <= -1 || y >= World.PLAYGROUND_HEIGHT) {
			return GameObjType.OUTOFBOUNDY;
		}
		if (x <= -1 || x >= World.PLAYGROUND_WIDTH) {
			return GameObjType.OUTOFBOUNDX;
		}
		if (hitPanel(panel,x,y)){
			return GameObjType.PANEL;
		}
		return this.playground[x][y].getType();
	}

	/**
	 * returns true if the ball hit the panel
	 * @return
	 */
	public boolean hitPanel(Panel panel,int x, int y){
		Log.i(TAG,"hitPanel ( " + x + " " + y + ") panel from " + panel.getX() + " - " + panel.getX() + Panel.PANEL_WIDTH);
		if(panel.getY() == y){
			if(x > panel.getX() && x < panel.getX() + Panel.PANEL_WIDTH)
				return true;
		}
		return false;
	}

	/**
	 * will be called when the ball goes out of bounds on y-axis
	 * @param ply
	 */
	public void gameOver(PanelPlayer ply){
		gameController.gameOver(ply);
	}


	/**
	 * checks if the masterbrick was hit
	 *
	 * @return
	 */
	public boolean hitMasterBrick(){
		return (playground[ball.x][ball.y].type == GameObjType.MASTER ? true : false);
	}
}