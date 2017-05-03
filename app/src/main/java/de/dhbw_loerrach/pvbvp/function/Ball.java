package de.dhbw_loerrach.pvbvp.function;

import android.util.Log;

/**
 * Created by weva on 04.04.2017.
 * Ball-Object extends GameObj
 * var int direction: 1, 2, 3, -1, -2, -3
 * var int player: 1, 2
 */

public class Ball extends GameObj {
	private int direction;
	private int player;

	private static final String TAG = "BALL";

	/**
	 * @param player set fly direction on create
	 */
	
	public Ball(int player) {
		this.type = GameObjType.BALL;
		this.x = World.PLAYGROUND_CENTER_X;
		this.player = player;
		if(player == PanelPlayer.PLAYER1.index) {
			this.direction = 1; //down
			this.y = 1;
		}
		if(player == PanelPlayer.PLAYER2.index){
			this.direction = -1; //up
			this.y = World.PLAYGROUND_HEIGHT - 2;
		}
	}
	
	public void move(World world, Panel panel1, Panel panel2) {
		GameObjType lo, lm, lu, mo, mu, ro, rm, ru = GameObjType.AIR;
		switch (this.direction) {
			//right down
			case 1:
				this.x += 1;
				this.y += 1;
				ro = world.collisionCheck(this.x + 1, this.y + 1, panel2);
				mo = world.collisionCheck(this.x, this.y + 1, panel2);
				rm = world.collisionCheck(this.x + 1, this.y, panel2);

				if (mo == GameObjType.OUTOFBOUNDY) {
					world.gameOver(panel2.getPlayer());
					return;
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
					Log.i(TAG,"brick destroy");
				}
				break;
			//down
			case 2:
				this.y += 1;
				mo = world.collisionCheck(this.x, this.y + 1, panel2);

				if (mo == GameObjType.OUTOFBOUNDY) {
					world.gameOver(panel2.getPlayer());
					return;
				}
				if(mo == GameObjType.BRICK || mo == GameObjType.PANEL)
					direction = -2;
				try {
					world.brickDestroy(this.x, this.y + 1);
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.i(TAG,"brick destroy");
				}
				break;
			//left down
			case 3:
				this.x -= 1;
				this.y += 1;
				ro = world.collisionCheck(this.x - 1, this.y + 1, panel2);
				mo = world.collisionCheck(this.x, this.y + 1, panel2);
				rm = world.collisionCheck(this.x - 1, this.y, panel2);

				if (mo == GameObjType.OUTOFBOUNDY) {
					world.gameOver(panel2.getPlayer());
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
					world.brickDestroy(this.x - 1, this.y + 1);
					world.brickDestroy(this.x, this.y + 1);
					world.brickDestroy(this.x - 1, this.y);
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.i(TAG,"brick destroy");
				}
				break;
			//left up
			case -1:
				x--;
				y--;

				//sorry, kann mit den Abkürzungen da oben nichts anfangen, änder ich vielleicht später.
				GameObjType leftup = world.collisionCheck(x-1,y-1,panel1);
				GameObjType left = world.collisionCheck(x-1,y,panel1);
				GameObjType up = world.collisionCheck(x,y-1,panel1);

				//game over
				if(leftup == GameObjType.OUTOFBOUNDY){
					world.gameOver(panel1.getPlayer());
					return;
				}

				//ball hits the edge of the panel, total reflection (left up -> right down)
				if(leftup == GameObjType.PANEL && up == GameObjType.AIR)
					direction = 1;
				//if the ball hits the panel, reflect it on x-axis ( left up -> left down)
				else if(up == GameObjType.PANEL || leftup == GameObjType.PANEL)
					direction = 3;
				//if the ball hits just bricks, reflect it totally (left up -> right down) leftup doesn't matter in this case
				else if(up == GameObjType.BRICK && left == GameObjType.BRICK)
					direction = 1;
				//ball hits edge of brick, total reflection (left up -> right down)
				else if(up == GameObjType.AIR && leftup == GameObjType.BRICK && left == GameObjType.AIR)
					direction = 1;
				//reflection on x-axis (left up -> left down) ; leftup doesn't matter
				else if(up == GameObjType.BRICK && left == GameObjType.AIR)
					direction = 3;
				//reflect on y-axis (left up -> right up) ; leftup doesn't matter
				else if(left == GameObjType.BRICK && up == GameObjType.AIR)
					direction = -3;
				//reflect on y-axis (left up -> right up)
				else if(left == GameObjType.OUTOFBOUNDX)
					direction = -3;

				try{
					world.brickDestroy(x-1,y-1);
					world.brickDestroy(x-1,y);
					world.brickDestroy(x,y-1);
				}catch(Exception e){
					Log.i(TAG,"brick destroy");
				}
				break;
			//down
			case -2:
				y--;
				GameObjType up2 = world.collisionCheck(x,y-1,panel1);

				if(up2 == GameObjType.OUTOFBOUNDY){
					world.gameOver(panel1.getPlayer());
				}
				//change direction in any case
				if(up2 == GameObjType.BRICK || up2 == GameObjType.PANEL)
					direction = 2;

				try{
					world.brickDestroy(x,y-1);
				}catch (Exception e) {
					Log.i(TAG, "brick destroy");
				}
				break;
			//right up
			case -3:
				x++;
				y--;

				GameObjType up3 = world.collisionCheck(x,y-1,panel1);
				GameObjType right = world.collisionCheck(x+1,y,panel1);
				GameObjType rightup = world.collisionCheck(x+1,y-1,panel1);

				if(rightup == GameObjType.OUTOFBOUNDY){
					world.gameOver(panel1.getPlayer());
					return;
				}

				//ball hits the edge of the panel, total reflection (right up -> left down)
				if(rightup == GameObjType.PANEL && up3 == GameObjType.AIR)
					direction = 3;
				//ball hits the panel, reflection on x-axis (right up -> right down)
				else if(up3 == GameObjType.PANEL)
					direction = 1;
				//if ball hits just bricks, total reflection (right up -> left down)
				else if(up3 == GameObjType.BRICK && right == GameObjType.BRICK)
					direction = 3;
				//ball hits edge of brick, total reflection (right up -> left down)
				else if(up3 == GameObjType.AIR && right == GameObjType.AIR && rightup == GameObjType.BRICK)
					direction = 3;
				//ball hits bricks below, reflection on x-axis (right up -> right down)
				else if(up3 == GameObjType.BRICK && right == GameObjType.AIR)
					direction = -1;
				//ball hits brick on side, reflection on y-axis (right up -> left up)
				else if(up3 == GameObjType.AIR && right == GameObjType.BRICK)
					direction = -1;
				//reflect on y-axis (right up -> left up)
				else if(rightup == GameObjType.OUTOFBOUNDX)
					direction = -1;

				try{
					world.brickDestroy(x+1,y-1);
					world.brickDestroy(x+1,y);
					world.brickDestroy(x,y-1);
				}catch(Exception e){
					Log.i(TAG,"brick destroy");
				}
				break;
		}
	}


	public void setDirection(int direction) {
		this.direction = direction;
	}

}
