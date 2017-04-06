package de.dhbw_loerrach.pvbvp.function;

import de.dhbw_loerrach.pvbvp.Main;
import de.dhbw_loerrach.pvbvp.gui.GameView;

/**
 * Created by weva on 04.04.2017.
 *  const PLAYGROUND_WIDTH has to be odd
 *  const PLAYGROUND_HEIGHT has to be even
 *  const PANEL_WITH has to be even
 */

public class World {
    public static final int PLAYGROUND_WIDTH = 27;
    public static final int PLAYGROUND_HEIGHT = 16;
    public static final int PLAYGROUND_OFFSET_X = 3;
    public static final int PLAYGROUND_OFFSET_Y = 3;

    private static final int PLAYGROUND_CENTER_X = PLAYGROUND_WIDTH / 2 + 1;
    private static final int PLAYGROUND_CENTER_Y_FLOOR = PLAYGROUND_HEIGHT / 2;
    private static final int PLAYGROUND_BRICK_SAFE_X = 5;
    private static final int PLAYGROUND_BRICK_SAFE_Y = 3;
    private static final int PANEL_WITH_P_SIDE = 3;
    public static int brickCount;
    public static GameObj[][] playground;

    private GameController gameController;


    public World(GameController gameController){
        this.gameController = gameController;
    }

    public void init() {
        playground = new GameObj[PLAYGROUND_WIDTH][PLAYGROUND_HEIGHT];
        for (int i = 0; i < PLAYGROUND_WIDTH; ++i) {
            for (int j = 0; j < PLAYGROUND_HEIGHT; ++j) {
                playground[i][j] = new Air();
            }
        }
        brickCreate();
        panelCreate(1);
        panelCreate(2);
        masterBrickCreate();
    }

    private static void brickCreate() {
        /**
         * @param x line indent iterating through 0 and 1
         */
        for (int i = PLAYGROUND_OFFSET_Y; i < PLAYGROUND_HEIGHT - PLAYGROUND_OFFSET_Y; ++i) {
            for (int j = PLAYGROUND_OFFSET_X + ((i + 1) % 2); j < PLAYGROUND_WIDTH - PLAYGROUND_OFFSET_X; j += 2) {
                double random = 0;
                if (j < PLAYGROUND_CENTER_X - PLAYGROUND_BRICK_SAFE_X ||
                        j >= PLAYGROUND_CENTER_X + PLAYGROUND_BRICK_SAFE_X ||
                        i < PLAYGROUND_CENTER_Y_FLOOR - PLAYGROUND_BRICK_SAFE_Y ||
                        i >= PLAYGROUND_CENTER_Y_FLOOR + PLAYGROUND_BRICK_SAFE_Y) {
                    random = Math.random();
                }
                if (random < 0.5) {
                    playground[j][i] = new Brick('l');
                    playground[j + 1][i] = new Brick('r');
                }
            }
        }
    }

    public static void masterBrickCreate() {
        switch (playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR].getSide()) {
            case 'l':
                playground[PLAYGROUND_CENTER_X + 1][PLAYGROUND_CENTER_Y_FLOOR] = new Air();
                break;
            case 'r':
                playground[PLAYGROUND_CENTER_X - 1][PLAYGROUND_CENTER_Y_FLOOR] = new Air();
                break;
        }
        switch (playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR - 1].getSide()) {
            case 'l':
                playground[PLAYGROUND_CENTER_X + 1][PLAYGROUND_CENTER_Y_FLOOR - 1] = new Air();
                break;
            case 'r':
                playground[PLAYGROUND_CENTER_X - 1][PLAYGROUND_CENTER_Y_FLOOR - 1] = new Air();
                break;
        }
        playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR] = new Brick('m');
        playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR - 1] = new Brick('m');
    }

    public static void panelCreate(int player) {
        int x = -1;
        switch (player) {
            case 1:
                x = 0;
                break;
            case 2:
                x = PLAYGROUND_HEIGHT - 1;
                break;
        }
        for (int i = PLAYGROUND_CENTER_X - PANEL_WITH_P_SIDE; i <= PLAYGROUND_CENTER_X + PANEL_WITH_P_SIDE; ++i) {
            playground[i][x] = new Panel(player);
        }
    }

    public static void panelMove(int player, char direction) {
        int x;
        int y;
        switch (player) {
            case 1:
                y = 0;
                break;
            case 2:
                y = PLAYGROUND_HEIGHT - 1;
                break;
        }
    }

    public static int[] findCoords(int rangeXmin, int rangeYmin, int rangeXmax, int rangeYmax, String objType) {
        int[] coords = new int[2];
        for (int i = rangeYmin; i < rangeYmax; ++i) {
            for (int j = rangeXmin; i < rangeXmax; ++j) {
                if (playground[j][i].getType() == objType) {
                    coords[0] = j;
                    coords[1] = i;
                    return coords;
                }
            }
        }
        return null;
    }
}