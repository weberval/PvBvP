package de.dhbw_loerrach.pvbvp.function;

/**
 * Created by weva on 04.04.2017.
 * super GameObj
 */
public class GameObj {
    protected GameObjType type;
    protected int x;
    protected int y;

    /**
     * deny creation of GameObj
     */

    protected GameObj() {
    }

    public char getSide(){
        return (char)0;
    }

    public int getPlayer(){
        return 0;
    }

    public GameObjType getType() {
        return type;
    }
}
