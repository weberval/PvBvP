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
    public static final int PLAYGROUND_WIDTH = 31;
    public static final int PLAYGROUND_HEIGHT = 16;
    public static final int PLAYGROUND_OFFSET_X = 5;
    public static final int PLAYGROUND_OFFSET_Y = 2;

    private static final int PLAYGROUND_CENTER_X = PLAYGROUND_WIDTH / 2;
    private static final int PLAYGROUND_CENTER_Y_FLOOR = PLAYGROUND_HEIGHT / 2;
    private static final int PANEL_WITH_P_SIDE = 2;
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
        masterBrickCreate();
        panelCreate(1);
        panelCreate(2);
    }

    private static void brickCreate() {
        /**
         * @param x line indent iterating through 0 and 1
         */
        for (int i = PLAYGROUND_OFFSET_Y - 1; i < PLAYGROUND_HEIGHT - PLAYGROUND_OFFSET_Y - 1; ++i) {
            for (int j = PLAYGROUND_OFFSET_X + (i % 2) - 1; j < PLAYGROUND_WIDTH - PLAYGROUND_OFFSET_X - 1; j += 2) {
                playground[j][i] = new Brick('l');
                playground[j+1][i] = new Brick('r');
            }
        }
    }

    public static void masterBrickCreate() {
        playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR] = new Brick('m');
        playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR + 1] = new Brick('m');

        playground[PLAYGROUND_CENTER_X + 1][PLAYGROUND_CENTER_Y_FLOOR] = new Brick('m');
        playground[PLAYGROUND_CENTER_X + 1][PLAYGROUND_CENTER_Y_FLOOR + 1] = new Brick('m');

        playground[PLAYGROUND_CENTER_X - 1][PLAYGROUND_CENTER_Y_FLOOR] = new Brick('m');
        playground[PLAYGROUND_CENTER_X - 1][PLAYGROUND_CENTER_Y_FLOOR + 1] = new Brick('m');
    }

    public static void panelCreate(int player) {
        int x = -1;
        if (player == 1) {
            x = 0;
        }
        else if (player == 2) {
            x = PLAYGROUND_HEIGHT - 1;
        }
        for (int i = PLAYGROUND_CENTER_X - PANEL_WITH_P_SIDE; i < PLAYGROUND_CENTER_X + PANEL_WITH_P_SIDE; ++i) {
            playground[i][x] = new Panel(player);
        }
    }
}