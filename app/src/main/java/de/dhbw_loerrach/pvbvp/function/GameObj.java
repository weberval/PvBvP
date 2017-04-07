package de.dhbw_loerrach.pvbvp.function;

/**
 * Created by weva on 04.04.2017.
 * super GameObj
 */
public class GameObj {
    protected String type;

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

    public String getType() {
        return type;
    }
}
