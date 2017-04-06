package de.dhbw_loerrach.pvbvp.function;

/**
 * Created by weva on 06.04.2017.
 *
 * Ball-Object extends GameObj
 *  var char side: 'l', 'r', 'm'
 *  var boolean master
 */
public class Brick extends GameObj {

    private char side;
    /**
     * @param side select side (left / right) of Block, also sets master true if master block
     */

    public Brick(char side) {
        if (side == 'm') {
            this.type = "master";
        }
        else {
            this.side = side;
            this.type = "brick";
        }
    }

    public char getSide() {
        return side;
    }
}
