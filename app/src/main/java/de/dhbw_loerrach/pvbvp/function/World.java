package de.dhbw_loerrach.pvbvp.function;

import android.util.Log;

import de.dhbw_loerrach.pvbvp.Main;
import de.dhbw_loerrach.pvbvp.gui.GameView;

/**
 * Created by weva on 04.04.2017.
 *  const PLAYGROUND_WIDTH has to be odd
 *  const PLAYGROUND_HEIGHT has to be even
 *  const PANEL_WITH has to be even
 */

public class World {


    private static final String TAG = "WORLD";

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

        debug();
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
                    playground[j][i] = new Brick(GameObjType.BRICK,'l');
                    playground[j + 1][i] = new Brick(GameObjType.BRICK,'r');
                }
            }
        }
    }

    public static void masterBrickCreate() {
        //BUG: Brick around the master bricks are not complete
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

        playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR] = new Brick(GameObjType.MASTER,'l');
        playground[PLAYGROUND_CENTER_X+1][PLAYGROUND_CENTER_Y_FLOOR] = new Brick(GameObjType.MASTER,'r');
        playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR - 1] = new Brick(GameObjType.MASTER,'l');
        playground[PLAYGROUND_CENTER_X+1][PLAYGROUND_CENTER_Y_FLOOR - 1] = new Brick(GameObjType.MASTER,'r');
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
        int[] foo = findCoords(0, 0, PLAYGROUND_WIDTH, PLAYGROUND_HEIGHT, "panel", player);
        if (direction == 'l') {
            for (int i = foo[0] + PANEL_WITH_P_SIDE * 2 + 1; i >= foo[0]; --i) {
                playground[i + 1][foo[1]] = playground[i][foo[1]];
                playground[i][foo[1]] = new Air();
            }
        } else if (direction == 'r') {
            for (int i = foo[0] + PANEL_WITH_P_SIDE * 2 + 1; i >= foo[0]; --i) {
                playground[i + 1][foo[1]] = playground[i][foo[1]];
                playground[i][foo[1]] = new Air();
            }
        }
    }

    public static int[] findCoords(int rangeXmin, int rangeYmin, int rangeXmax, int rangeYmax, String objType, int player) {
        int[] coords = new int[2];
        for (int i = rangeYmin; i < rangeYmax; ++i) {
            for (int j = rangeXmin; j < rangeXmax; ++j) {
                if (playground[j][i].getType().equals(objType) && playground[j][i].getPlayer() == player) {
                    coords[0] = j;
                    coords[1] = i;
                    return coords;
                }
            }
        }
        return null;
    }

    //TEST
    public void debug(){
        String str = "";
        for(int i=0; i < playground.length; i ++){
            for(int j = 0; j < playground[i].length; j ++){
                str += " | " + debug_helper(playground[i][j].type,playground[i][j].getSide()) + " | ";
            }
            Log.i(TAG,str);
            str = "";
        }
    }

    //TEST
    private String debug_helper(GameObjType t,char s){
        switch (t){
            case BRICK: return "B"+s;
            case MASTER: return "M"+s;
            case PANEL: return "P"+s;
            case BALL: return "B"+s;
            case AIR: return "A"+s;
        }
        return null;
    }
}