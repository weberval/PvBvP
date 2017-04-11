package de.dhbw_loerrach.pvbvp.function;

/**
 * Created by weva on 04.04.2017.
 * Panel-Object extends GameObj
 * var int player
 */
public class Panel extends GameObj {

    public static final int PANEL_WIDTH = 7;
    private PanelPlayer player;

    /**
     * @param player sets player for panel
     */

    public Panel(PanelPlayer player) {
        this.type = GameObjType.PANEL;
        this.player = player;
        this.x = World.PLAYGROUND_CENTER_X - (PANEL_WIDTH / 2);
        switch (player) {
            case PLAYER1:
                this.y = 0;
                break;
            case PLAYER2:
                this.y = World.PLAYGROUND_HEIGHT - 1;
                break;
        }
    }

    public void moveRight() {
        if (this.x < World.PLAYGROUND_WIDTH - PANEL_WIDTH) {
            this.x++;
        }
    }

    public void moveLeft() {
        if (this.x > 1) {
            this.x--;
        }
    }

    public PanelPlayer getPlayer() {
        return player;
    }
}
