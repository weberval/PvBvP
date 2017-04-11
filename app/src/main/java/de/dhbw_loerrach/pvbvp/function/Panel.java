package de.dhbw_loerrach.pvbvp.function;

import android.widget.TwoLineListItem;

/**
 * Created by weva on 04.04.2017.
 * Panel-Object extends GameObj
 *  var int player
 */
public class Panel extends GameObj {

    public static final int PANEL_WIDTH = 7;
    private int player;
    private int panelWidth = PANEL_WIDTH;

    /**
     *
     * @param player sets player for panel
     */

    public Panel(int player) {
        this.type = GameObjType.PANEL;
        this.player = player;
        this.x = World.PLAYGROUND_CENTER_X;
        switch (player) {
            case 1:
                this.y = 0;
                break;
            case 2:
                this.y = World.PLAYGROUND_HEIGHT - 1;
                break;
        }
    }

    public void moveRight() {
        if (this.x < World.PLAYGROUND_WIDTH - this.panelWidth) {
            this.x++;
        }
    }

    public void moveLeft(){
        if (this.x > 1) {
            this.x--;
        }
    }

    public int getPlayer() {
        return player;
    }
}
