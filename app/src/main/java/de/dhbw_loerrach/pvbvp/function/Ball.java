package de.dhbw_loerrach.pvbvp.function;

/**
 * Created by weva on 04.04.2017.
 * Ball-Object extends GameObj
 *  var int direction: 1, 2, 3, -1, -2, -3
 *  var int player: 1, 2
 */

public class Ball extends GameObj {
    private int direction;
    private int player;

    /**
     * @param player set fly direction on create
     */

    public Ball(int player) {
        this.type = GameObjType.BALL;
        this.x = World.PLAYGROUND_CENTER_X;
        this.player = player;
        switch (player){
            case 1:
                this.direction = 1;
                this.y = 1;
                break;
            case 2:
                this.direction = -1;
                this.y = World.PLAYGROUND_HEIGHT - 1;
                break;
        }
    }

    public void move(World world) {
        switch (this.direction) {
            case 1:
                switch (world.collisionCheck(this.x + 1, this.y + 1)) {
                    case BRICK:
                        // TODO: insert destruction of brick and reflect Ball
                        break;
                    case PANEL:
                        // TODO: reflect Ball
                        break;
                    case MASTER:
                        // TODO: end level
                        break;
                    case OUTOFBOUNDX:
                        // TODO: reflect Ball
                        break;
                    case OUTOFBOUNDY:
                        // TODO: reset playground
                        break;
                }
                break;
            case 2:
                switch (world.collisionCheck(this.x + 1, this.y + 1)) {
                    case BRICK:
                    // TODO: insert destruction of brick and reflect Ball
                    break;
                case PANEL:
                    // TODO: reflect Ball
                    break;
                case MASTER:
                    // TODO: end level
                    break;
                case OUTOFBOUNDX:
                    // TODO: reflect Ball
                    break;
                case OUTOFBOUNDY:
                    // TODO: reset playground
                    break;
                }
                break;
            case 3:
                switch (world.collisionCheck(this.x + 1, this.y + 1)) {
                    case BRICK:
                    // TODO: insert destruction of brick and reflect Ball
                    break;
                case PANEL:
                    // TODO: reflect Ball
                    break;
                case MASTER:
                    // TODO: end level
                    break;
                case OUTOFBOUNDX:
                    // TODO: reflect Ball
                    break;
                case OUTOFBOUNDY:
                    // TODO: reset playground
                    break;
                }
                break;
            case -1:
                switch (world.collisionCheck(this.x + 1, this.y + 1)) {
                    case BRICK:
                    // TODO: insert destruction of brick and reflect Ball
                    break;
                case PANEL:
                    // TODO: reflect Ball
                    break;
                case MASTER:
                    // TODO: end level
                    break;
                case OUTOFBOUNDX:
                    // TODO: reflect Ball
                    break;
                case OUTOFBOUNDY:
                    // TODO: reset playground
                    break;
                }
                break;
            case -2:
                switch (world.collisionCheck(this.x + 1, this.y + 1)) {
                    case BRICK:
                    // TODO: insert destruction of brick and reflect Ball
                    break;
                case PANEL:
                    // TODO: reflect Ball
                    break;
                case MASTER:
                    // TODO: end level
                    break;
                case OUTOFBOUNDX:
                    // TODO: reflect Ball
                    break;
                case OUTOFBOUNDY:
                    // TODO: reset playground
                    break;
                }
                break;
            case -3:
                switch (world.collisionCheck(this.x + 1, this.y + 1)) {
                    case BRICK:
                    // TODO: insert destruction of brick and reflect Ball
                case PANEL:
                    // TODO: reflect Ball
                    break;
                case MASTER:
                    // TODO: end level
                    break;
                case OUTOFBOUNDX:
                    // TODO: reflect Ball
                    break;
                case OUTOFBOUNDY:
                    // TODO: reset playground
                    break;
                }
                break;
        }
    }

}
