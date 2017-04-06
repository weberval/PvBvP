package de.dhbw_loerrach.pvbvp.function;

/**
 * Created by weva on 04.04.2017.
 * Ball-Object extends GameObj
 *  var int direction: 1, 2, 3, -1, -2, -3
 */

public class Ball extends GameObj {
    private int direction;

    /**
     *
     * @param player set fly direction on create
     */

    public Ball(int player) {
        this.type = "ball";
        if (player == 1) {
            this.direction = 1;
        }
        else if (player == 2) {
            this.direction = -1;
        }
    }
}
