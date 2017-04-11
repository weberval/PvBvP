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

    static final int PLAYGROUND_CENTER_X = PLAYGROUND_WIDTH / 2 + 1;
    static final int PLAYGROUND_CENTER_Y_FLOOR = PLAYGROUND_HEIGHT / 2;

    private static final int PLAYGROUND_BRICK_SAFE_X = 5;
    private static final int PLAYGROUND_BRICK_SAFE_Y = 3;
    private static final int PANEL_WITH_P_SIDE = 3;
    public static int brickCount = 100;

    //Added ball and array of panels as new public attributes, since the playground will only contain bricks / masters
    public static GameObj[][] playground;
    public static Ball ball;
    public static Panel[] panels;

    private GameController gameController;


    public World(GameController gameController) {
        this.gameController = gameController;
    }

    public void init() {
        playground = new GameObj[PLAYGROUND_WIDTH][PLAYGROUND_HEIGHT];
        for (int i = 0; i < PLAYGROUND_WIDTH; ++i) {
            for (int j = 0; j < PLAYGROUND_HEIGHT; ++j) {
                playground[i][j] = new Air();
            }
        }

        //add panels to world
        panels = new Panel[2];
        panels[0] = new Panel(1);
        panels[1] = new Panel(2);

        brickCreate();
        masterBrickCreate();
        Panel player1 = new Panel(1);
        Panel player2 = new Panel(2);
        debug();
    }

    private void brickCreate() {
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
                    this.playground[j][i] = new Brick(GameObjType.BRICK,'l');
                    this.playground[j + 1][i] = new Brick(GameObjType.BRICK,'r');
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
        switch (this.playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR].getSide()) {
            case 'l':
                this.brickDestroy(PLAYGROUND_CENTER_X + 1, PLAYGROUND_CENTER_Y_FLOOR);
                break;
            case 'r':
                this.brickDestroy(PLAYGROUND_CENTER_X - 1, PLAYGROUND_CENTER_Y_FLOOR);
                break;
        }
        switch (this.playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR - 1].getSide()) {
            case 'l':
                this.playground[PLAYGROUND_CENTER_X + 1][PLAYGROUND_CENTER_Y_FLOOR - 1] = new Air();
                break;
            case 'r':
                this.brickDestroy(PLAYGROUND_CENTER_X - 1, PLAYGROUND_CENTER_Y_FLOOR - 1);
                break;
        }
        //fix
        playground[PLAYGROUND_CENTER_X+2][PLAYGROUND_CENTER_Y_FLOOR-1] = new Air();

        playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR] = new Brick(GameObjType.MASTER,'l');
        playground[PLAYGROUND_CENTER_X+1][PLAYGROUND_CENTER_Y_FLOOR] = new Brick(GameObjType.MASTER,'r');
        playground[PLAYGROUND_CENTER_X][PLAYGROUND_CENTER_Y_FLOOR - 1] = new Brick(GameObjType.MASTER,'l');
        playground[PLAYGROUND_CENTER_X+1][PLAYGROUND_CENTER_Y_FLOOR - 1] = new Brick(GameObjType.MASTER,'r');
    }

    public void brickDestroy(int x, int y){
        this.playground[x][y] = new Air();
    }

    public void movePanel(int player, char dir){
        if(player < 0 || player > 2)
            return;

        if(dir == 'l') {
            panels[player-1].moveLeft();
        }
        else if(dir == 'r'){
            panels[player-1].moveRight();
        }
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

    public GameObjType collisionCheck(int x, int y) {
        switch (x){
            case -1:
                return GameObjType.OUTOFBOUNDX;
            case World.PLAYGROUND_WIDTH:
                return GameObjType.OUTOFBOUNDX;
        }
        switch (y) {
            case -1:
                return GameObjType.OUTOFBOUNDY;
            case World.PLAYGROUND_HEIGHT:
                return GameObjType.OUTOFBOUNDY;
        }
        return this.playground[x][y].getType();
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