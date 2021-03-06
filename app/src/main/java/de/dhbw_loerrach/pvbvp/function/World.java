package de.dhbw_loerrach.pvbvp.function;

import android.util.Log;

import de.dhbw_loerrach.pvbvp.sound.Soundeffects;

/**
 * Created by weva on 04.04.2017. <br />
 * const PLAYGROUND_WIDTH has to be odd <br />
 * const PLAYGROUND_HEIGHT has to be even <br />
 * const PANEL_WITH has to be even
 */
public class World {

	private static final String TAG = "WORLD";

	public static final int PLAYGROUND_WIDTH = 27;
	public static final int PLAYGROUND_HEIGHT = 16;
	public static final int PLAYGROUND_OFFSET_X = 3;
	public static final int PLAYGROUND_OFFSET_Y = 3;
	static final int PLAYGROUND_CENTER_X = PLAYGROUND_WIDTH / 2 + 1;
	static final int PLAYGROUND_CENTER_Y_FLOOR = (PLAYGROUND_HEIGHT - 1) / 2;

	private static final int PLAYGROUND_BRICK_SAFE_X = 5;
	private static final int PLAYGROUND_BRICK_SAFE_Y = 3;
	public static int brickCount = 100;

	public static GameObj[][] playground;
	public static Ball ball;
	public static Panel[] panels;

	public static GameController gameController;


	public static void setController(GameController gameController_) {
		gameController = gameController_;
	}


	/**
	 * depending on level, the blocks are set and where the ball spawns. <br />
	 * (for example: all even level start with player1, all odd level start with player2)
	 * @param level
	 */
	public static void init(int level) {
		playground = new GameObj[PLAYGROUND_WIDTH][PLAYGROUND_HEIGHT];
		for (int i = 0; i < PLAYGROUND_WIDTH; ++i) {
			for (int j = 0; j < PLAYGROUND_HEIGHT; ++j) {
				playground[i][j] = new Air();
			}
		}

		panels = new Panel[2];
		panels[0] = new Panel(PanelPlayer.PLAYER1);
		panels[1] = new Panel(PanelPlayer.PLAYER2);

		if (level % 2 == 0)
			ball = new Ball(PanelPlayer.PLAYER1.index);
		else
			ball = new Ball(PanelPlayer.PLAYER2.index);

		brickCreate();
		masterBrickCreate();

	}


	/**
	 * creates almost random playground, containing brick-objects
	 * @see Brick
	 */
	private static void brickCreate() {
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
					playground[j][i] = new Brick(GameObjType.BRICK, 'l');
					playground[j + 1][i] = new Brick(GameObjType.BRICK, 'r');
				}
			}
		}

// ## alternative way to create blocks ##
//        for (int i = 0; i < brickCount; ++i) {
//            int x = PLAYGROUND_OFFSET_X + (int)((Math.random() - .1) * (PLAYGROUND_WIDTH - PLAYGROUND_OFFSET_X));
//            int y = PLAYGROUND_OFFSET_Y + (int)((Math.random() - .1) * (PLAYGROUND_HEIGHT - PLAYGROUND_OFFSET_Y));
//            switch (this.playground[x][y].getType()) {
//                case AIR:
//                    int offset = (x % 2) - (y % 2);
//                    this.playground[x + offset][y] = new Brick(GameObjType.BRICK, 'l');
//                    this.playground[x + offset + 1][y] = new Brick(GameObjType.BRICK, 'r');
//                    break;
//                default:
//                    --i;
//            }
//        } */
	}

	/**
	 * creates masterbrick-object in the center of the playground
	 * @see Brick
	 */
	public static void masterBrickCreate() {
		brickDestroy(PLAYGROUND_CENTER_X, PLAYGROUND_CENTER_Y_FLOOR);
		brickDestroy(PLAYGROUND_CENTER_X, PLAYGROUND_CENTER_Y_FLOOR + 1);

		playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR] = new Brick(GameObjType.MASTER, 'm');
		playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR + 1] = new Brick(GameObjType.MASTER, 'm');

		/* for testing
		for(int i = 0; i < 10; i ++)
			playground[i][7] = new Brick(GameObjType.MASTER,'m');
		*/
	}

	/**
	 * destructs brick at given coordinate and corresponding neighbour
	 * @see Brick
	 * @param x coordinate of the brick to destroy
	 * @param y coordinate of the brick to destroy
	 */
	public static void brickDestroy(int x, int y) {
		switch (playground[x][y].getSide()) {
			case 'l':
				if (playground[x + 1][y].type == GameObjType.BRICK)
					playground[x + 1][y].destruct();
				break;
			case 'r':
				if (playground[x - 1][y].type == GameObjType.BRICK)
					playground[x - 1][y].destruct();
				break;
		}
		if (playground[x][y].type == GameObjType.BRICK) {
			playground[x][y].destruct();
			Soundeffects.playKock();
		}
	}

	/**
	 * move the panel
	 * @param player the player the panel belongs to
	 * @param dir  ether to the [l]eft side or to the [r]ight side
	 */
	public static void movePanel(int player, char dir) {
		if (player < 0 || player > 2) {
			return;
		}

		if (dir == 'l') {
			panels[player].moveLeft();
		} else if (dir == 'r') {
			panels[player].moveRight();
		}
	}

	/**
	 *
	 * @param x coordinate
	 * @param y coordinate
	 * @param panel
	 * @return type of object which triggered the collision check
	 */
	public static GameObjType collisionCheck(int x, int y, Panel panel) {
		if (y < -1 || y > World.PLAYGROUND_HEIGHT) {
			return GameObjType.OUTOFBOUNDY;
		}
		if (x <= -1 || x >= World.PLAYGROUND_WIDTH) {
			return GameObjType.OUTOFBOUNDX;
		}
		if (hitPanel(panel, x, y)) {
			return GameObjType.PANEL;
		}
		try {
			return playground[x][y].getType();
		} catch (ArrayIndexOutOfBoundsException e) {
			return GameObjType.AIR;
		}
	}

	/**
	 * @return true if the ball hit the panel
	 */
	public static boolean hitPanel(Panel panel, int x, int y) {
		int diff = 0;
		switch (panel.getPlayer()) {
			case PLAYER1:
				diff = 1;
				break;
			case PLAYER2:
				diff = -1;
				break;
		}
		if (panel.getY() == y + diff) {
			if (x >= panel.getX() && x <= panel.getX() + Panel.PANEL_WIDTH) {
				Soundeffects.playBlink();
				return true;
			}
		}
		return false;
	}

	/**
	 * will be called when the ball goes out of bounds on y-axis
	 *
	 * @param ply
	 */
	public static void gameOver(PanelPlayer ply) {
		gameController.gameOver(ply);
	}

	/**
	 * will be call when the balls hits the masterbrick
	 *
	 * @param ply
	 */
	public static void win(PanelPlayer ply) {
		gameController.win(ply);
	}

	/**
	 * checks if the masterbrick was hit
	 * not in use now
	 *
	 * @deprecated
	 * @return
	 */
	public boolean hitMasterBrick() {
		return (playground[ball.x][ball.y].type == GameObjType.MASTER ? true : false);
	}

	public static String returnString() {
		String string = "";
		for (int i = 0; i < PLAYGROUND_WIDTH; ++i) {
			for (int j = 0; j < PLAYGROUND_HEIGHT; ++j) {
				string += playground[i][j].toString();
			}
			string += "N";
		}
		return string;
	}

	/**
	 * used for networking
	 * @param string
	 */
	public static void decode(String string) {
		playground = new GameObj[PLAYGROUND_WIDTH][PLAYGROUND_HEIGHT];
		char[] pg = string.toCharArray();
		int i, j;
		i = j = 0;
		for (char ch : pg) {
			switch (ch) {
				case 'N':
					j++;
					break;
				case 'L':
					playground[i][j] = new Brick(GameObjType.BRICK, 'l');
					i++;
					break;
				case 'R':
					playground[i][j] = new Brick(GameObjType.BRICK, 'r');
					i++;
					break;
				case 'A':
					playground[i][j] = new Air();
					i++;
					break;
				case 'M':
					playground[i][j] = new Brick(GameObjType.MASTER, 'r');
					i++;
					break;
			}
		}
	}
}