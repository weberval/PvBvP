package de.dhbw_loerrach.pvbvp.function;

/**
 * Created by weva on 04.04.2017.
 * Ball-Object extends GameObj
 * var int direction: 1, 2, 3, -1, -2, -3
 * var int player: 1, 2
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
		switch (player) {
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
	
	public void move(World world, Panel panel1, Panel panel2) {
		GameObjType lo, lm, lu, mo, mu, ro, rm, ru = GameObjType.AIR;
		switch (this.direction) {
			case 1:
				this.x += 1;
				this.y += 1;
				ro = world.collisionCheck(this.x + 1, this.y + 1, panel2);
				mo = world.collisionCheck(this.x, this.y + 1, panel2);
				rm = world.collisionCheck(this.x + 1, this.y, panel2);

				if (mo == GameObjType.OUTOFBOUNDY) {
					//TODO: reset playground
				} else if (rm == GameObjType.OUTOFBOUNDX) {
					this.direction = 3;
				} else if (ro == GameObjType.BRICK && rm == GameObjType.BRICK) {
					this.direction = -1;
				} else if (mo == GameObjType.BRICK) {
					this.direction = -3;
				} else if (rm == GameObjType.BRICK) {
					this.direction = 3;
				} else if (ro == GameObjType.BRICK) {
					this.direction = -1;
				} else if (mo == GameObjType.PANEL || ro == GameObjType.PANEL) {
					this.direction = -3;
				}
				try {
					world.brickDestroy(this.x + 1, this.y + 1);
					world.brickDestroy(this.x, this.y + 1);
					world.brickDestroy(this.x + 1, this.y);
				} catch (ArrayIndexOutOfBoundsException e) {

				}
				break;
			case 2:
				this.y += 1;
				mo = world.collisionCheck(this.x, this.y + 1, panel2);

				if (mo == GameObjType.OUTOFBOUNDY) {
					//TODO: reset playground
				} else if (mo == GameObjType.BRICK) {
					this.direction = -2;
				} else if (mo == GameObjType.PANEL) {
					this.direction = -2;
				}
				try {
					world.brickDestroy(this.x, this.y + 1);
				} catch (ArrayIndexOutOfBoundsException e) {

				}
				break;
			case 3:
				this.x -= 1;
				this.y += 1;
				ro = world.collisionCheck(this.x + 1, this.y + 1, panel2);
				mo = world.collisionCheck(this.x, this.y + 1, panel2);
				rm = world.collisionCheck(this.x + 1, this.y, panel2);

				if (mo == GameObjType.OUTOFBOUNDY) {
					//TODO: reset playground
				} else if (rm == GameObjType.OUTOFBOUNDX) {
					this.direction = 3;
				} else if (ro == GameObjType.BRICK && rm == GameObjType.BRICK) {
					this.direction = -1;
				} else if (mo == GameObjType.BRICK) {
					this.direction = -3;
				} else if (rm == GameObjType.BRICK) {
					this.direction = 3;
				} else if (ro == GameObjType.BRICK) {
					this.direction = -1;
				} else if (mo == GameObjType.PANEL || ro == GameObjType.PANEL) {
					this.direction = -3;
				}
				try {
					world.brickDestroy(this.x + 1, this.y + 1);
					world.brickDestroy(this.x, this.y + 1);
					world.brickDestroy(this.x + 1, this.y);
				} catch (ArrayIndexOutOfBoundsException e) {

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
	public void setDirection(int direction) {
		this.direction = direction;
	}

}
