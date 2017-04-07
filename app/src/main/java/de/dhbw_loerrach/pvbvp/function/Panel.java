package de.dhbw_loerrach.pvbvp.function;

import android.widget.TwoLineListItem;

/**
 * Created by weva on 04.04.2017.
 * Panel-Object extends GameObj
 *  var int player
 */
public class Panel extends GameObj {

    private int player;

    /**
     *
     * @param player sets player for panel
     */

    public Panel(int player) {
        this.type = GameObjType.PANEL;
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }
}
