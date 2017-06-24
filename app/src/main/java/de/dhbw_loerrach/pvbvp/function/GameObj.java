package de.dhbw_loerrach.pvbvp.function;

import java.io.Serializable;

/**
 * Created by weva on 04.04.2017. <br />
 *
 */
public class GameObj implements Serializable {
	protected GameObjType type;
	protected int x;
	protected int y;
	
	/**
	 * deny creation of GameObj
	 */
	
	protected GameObj() {
	}

	/**
	 * @return side of a brick
	 */
	public char getSide() {
		return (char) 0;
	}

	/**
	 * use to perform the destruct-animation
	 * @return destructionStage
	 * @see de.dhbw_loerrach.pvbvp.gui.GameView
	 */
	public int getDestructionStage(){
		return 0;
	}

	/**
	 * use to destruct a brick
	 * @see World
	 */
	public void destruct(){};

	/**
	 * @return number of player
	 * @see Panel
	 */
	public PanelPlayer getPlayer() {
		return null;
	}

	/**
	 * @return type of the opject
	 * @see GameObjType
	 */
	public GameObjType getType() {
		return type;
	}

	/**
	 * @return X-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return Y-coordinate
	 */
	public int getY() {
		return y;
	}


	/**
	 * for network support
	 * @return
	 */
	public String toString() {
		switch (this.type){
			case AIR:
				return "A";
			case BRICK:
				return "B";
			case MASTER:
				return "M";
			default:
				return null;
		}
	}
}