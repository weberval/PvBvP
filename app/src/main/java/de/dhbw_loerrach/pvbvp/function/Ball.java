package de.dhbw_loerrach.pvbvp.function;

import android.util.Log;

import static de.dhbw_loerrach.pvbvp.Main.PORTAL;

/**
 * Created by weva on 04.04.2017.
 * Ball-Object extends GameObj
 * var int direction: 1, 2, 3, -1, -2, -3
 * var int player: 1, 2
 */

public class Ball extends GameObj {
	private int direction;

	private Panel panel1;
	private Panel panel2;

	private static final String TAG = "BALL";

	/**
	 * @param player set fly direction on create
	 */
	
	public Ball(int player) {
		type = GameObjType.BALL;
		x = World.PLAYGROUND_CENTER_X;
		if(player == PanelPlayer.PLAYER1.index) {
			direction = 3;
			y = 1;
		}
		if(player == PanelPlayer.PLAYER2.index){
			direction = -1;
			y = World.PLAYGROUND_HEIGHT - 2;
		}
	}
	

	public int getDir(){
		return direction;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setDir(int dir){
		direction = dir;
	}

	public void move() {
		panel1 = World.panels[0];
		panel2 = World.panels[1];
		GameObjType lo, lm, lu, mo, mu, ro, rm, ru = GameObjType.AIR;
		switch (direction) {
			//right down
			case 1:
				x += 1;
				y += 1;
				ro = World.collisionCheck(x + 1, y + 1, panel2);
				mo = World.collisionCheck(x, y + 1, panel2);
				rm = World.collisionCheck(x + 1, y, panel2);

				if (mo == GameObjType.OUTOFBOUNDY) {
					World.gameOver(panel2.getPlayer());
					return;
				}

				if(mo == GameObjType.MASTER || ro == GameObjType.MASTER) {
					World.win(panel2.getPlayer());
					return;
				}

				if (rm == GameObjType.OUTOFBOUNDX) {
					direction = 3;
				} else if (ro == GameObjType.BRICK && rm == GameObjType.BRICK) {
					direction = -1;
				} else if (mo == GameObjType.BRICK) {
					direction = -3;
				} else if (rm == GameObjType.BRICK) {
					direction = 3;
				} else if (ro == GameObjType.BRICK) {
					direction = -1;
				} else if (mo == GameObjType.PANEL || ro == GameObjType.PANEL) {
					onPanelHit(panel2,panel1);
					//direction = -3;
				}
				try {
					World.brickDestroy(x + 1, y + 1);
					World.brickDestroy(x, y + 1);
					World.brickDestroy(x + 1, y);
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.i(TAG,"brick destroy");
				}
				break;
			//down
			case 2:
				y += 1;
				mo = World.collisionCheck(x, y + 1, panel2);

				if (mo == GameObjType.OUTOFBOUNDY) {
					World.gameOver(panel2.getPlayer());
					return;
				}

				if(mo == GameObjType.MASTER){
					World.win(panel2.getPlayer());
					return;
				}

				if(mo == GameObjType.BRICK)
					direction = -2;

				if(mo == GameObjType.PANEL)
					onPanelHit(panel2,panel1);

				try {
					World.brickDestroy(x, y + 1);
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.i(TAG,"brick destroy");
				}
				break;
			//left down
			case 3:
				x -= 1;
				y += 1;
				ro = World.collisionCheck(x - 1, y + 1, panel2);
				mo = World.collisionCheck(x, y + 1, panel2);
				rm = World.collisionCheck(x - 1, y, panel2);

				if (mo == GameObjType.OUTOFBOUNDY) {
					World.gameOver(panel2.getPlayer());
					return;
				}

				if(ro == GameObjType.MASTER || rm == GameObjType.MASTER){
					World.win(panel2.getPlayer());
					return;
				}


				if (rm == GameObjType.OUTOFBOUNDX) {
					direction = 1;
				} else if (ro == GameObjType.BRICK && rm == GameObjType.BRICK) {
					direction = -1;
				} else if (mo == GameObjType.BRICK) {
					direction = -3;
				} else if (rm == GameObjType.BRICK) {
					direction = 3;
				} else if (ro == GameObjType.BRICK) {
					direction = -1;
				} else if (mo == GameObjType.PANEL || ro == GameObjType.PANEL) {
					onPanelHit(panel2,panel1);
					//direction = -1;
				}
				try {
					World.brickDestroy(x - 1, y + 1);
					World.brickDestroy(x, y + 1);
					World.brickDestroy(x - 1, y);
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.i(TAG,"brick destroy");
				}
				break;
			//left up
			case -1:
				x--;
				y--;

				GameObjType leftup = World.collisionCheck(x-1,y-1,panel1);
				GameObjType left = World.collisionCheck(x-1,y,panel1);
				GameObjType up = World.collisionCheck(x,y-1,panel1);

				//game over
				if(leftup == GameObjType.OUTOFBOUNDY){
					World.gameOver(panel1.getPlayer());
					return;
				}

				if(leftup == GameObjType.MASTER || up == GameObjType.MASTER){
					World.win(panel1.getPlayer());
					return;
				}


				//ball hits the edge of the panel, total reflection (left up -> right down)
				if(leftup == GameObjType.PANEL && (up == GameObjType.AIR || up == GameObjType.DESTUCTEDBRICK)) {
					//direction = 1;
					onPanelHit(panel1,panel2); //for now
				}
				//if the ball hits the panel, reflect it on x-axis ( left up -> left down)
				else if(up == GameObjType.PANEL || leftup == GameObjType.PANEL) {
					onPanelHit(panel1,panel2); //direction = 3; for now
				}
				//if the ball hits just bricks, reflect it totally (left up -> right down) leftup doesn't matter in this case
				else if(up == GameObjType.BRICK && left == GameObjType.BRICK)
					direction = 1;
				//ball hits edge of brick, total reflection (left up -> right down)
				else if((up == GameObjType.AIR || up == GameObjType.DESTUCTEDBRICK) && leftup == GameObjType.BRICK && (left == GameObjType.AIR || left == GameObjType.DESTUCTEDBRICK))
					direction = 1;
				//reflection on x-axis (left up -> left down) ; leftup doesn't matter
				else if(up == GameObjType.BRICK && (left == GameObjType.AIR || left == GameObjType.DESTUCTEDBRICK))
					direction = 3;
				//reflect on y-axis (left up -> right up) ; leftup doesn't matter
				else if(left == GameObjType.BRICK && (up == GameObjType.AIR || up == GameObjType.DESTUCTEDBRICK))
					direction = -3;
				//reflect on y-axis (left up -> right up)
				else if(left == GameObjType.OUTOFBOUNDX)
					direction = -3;

				try{
					World.brickDestroy(x-1,y-1);
					World.brickDestroy(x-1,y);
					World.brickDestroy(x,y-1);
				}catch(Exception e){
					Log.i(TAG,"brick destroy");
				}
				break;
			//up
			case -2:
				y--;
				GameObjType up2 = World.collisionCheck(x,y-1,panel1);

				if(up2 == GameObjType.OUTOFBOUNDY){
					World.gameOver(panel1.getPlayer());
					return;
				}

				if(up2 == GameObjType.MASTER){
					World.win(panel1.getPlayer());
					return;
				}


				//change direction in any case
				if(up2 == GameObjType.BRICK)
					direction = 2;

				if(up2 == GameObjType.PANEL)
					onPanelHit(panel1,panel2);

				try{
					World.brickDestroy(x,y-1);
				}catch (Exception e) {
					Log.i(TAG, "brick destroy");
				}
				break;
			//right up
			case -3:
				x++;
				y--;

				GameObjType up3 = World.collisionCheck(x,y-1,panel1);
				GameObjType right = World.collisionCheck(x+1,y,panel1);
				GameObjType rightup = World.collisionCheck(x+1,y-1,panel1);

				if(rightup == GameObjType.OUTOFBOUNDY){
					World.gameOver(panel1.getPlayer());
					return;
				}

				if(rightup == GameObjType.MASTER || up3 == GameObjType.MASTER){
					World.win(panel1.getPlayer());
					return;
				}


				//ball hits the edge of the panel, total reflection (right up -> left down)
				if(rightup == GameObjType.PANEL && (up3 == GameObjType.AIR || up3 == GameObjType.DESTUCTEDBRICK))
					onPanelHit(panel1,panel2); //direction = 3;
				//ball hits the panel, reflection on x-axis (right up -> right down)
				else if(up3 == GameObjType.PANEL)
					onPanelHit(panel1,panel2); //direction = 1;
				//if ball hits just bricks, total reflection (right up -> left down)
				else if(up3 == GameObjType.BRICK && right == GameObjType.BRICK)
					direction = 3;
				//ball hits edge of brick, total reflection (right up -> left down)
				else if((up3 == GameObjType.AIR || up3 == GameObjType.DESTUCTEDBRICK) && (right == GameObjType.AIR || right == GameObjType.DESTUCTEDBRICK) && rightup == GameObjType.BRICK)
					direction = 3;
				//ball hits bricks below, reflection on x-axis (right up -> right down)
				else if(up3 == GameObjType.BRICK && (right == GameObjType.AIR || right == GameObjType.DESTUCTEDBRICK))
					direction = -1;
				//ball hits brick on side, reflection on y-axis (right up -> left up)
				else if((up3 == GameObjType.AIR || up3 == GameObjType.DESTUCTEDBRICK) && right == GameObjType.BRICK)
					direction = -1;
				//reflect on y-axis (right up -> left up)
				else if(rightup == GameObjType.OUTOFBOUNDX)
					direction = -1;

				try{
					World.brickDestroy(x+1,y-1);
					World.brickDestroy(x+1,y);
					World.brickDestroy(x,y-1);
				}catch(Exception e){
					Log.i(TAG,"brick destroy");
				}
				break;
		}
	}

	public void onPanelHit(Panel p1, Panel p2){
		if(PORTAL){
			if(y == World.PLAYGROUND_HEIGHT-1)
				y = 1;
			else y = World.PLAYGROUND_HEIGHT-2;

			int xdiff = x - p1.getX();

			x = p2.getX()+xdiff;

			return;
		}
		direction = -direction;
	}
}
