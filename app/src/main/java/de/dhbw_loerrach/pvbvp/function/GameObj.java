package de.dhbw_loerrach.pvbvp.function;

import java.io.Serializable;

/**
 * Created by weva on 04.04.2017.
 * super GameObj
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
	
	public char getSide() {
		return (char) 0;
	}

	public int getDestructionStage(){
		return 0;
	}

	public void destruct(){};
	
	public PanelPlayer getPlayer() {
		return null;
	}
	
	public GameObjType getType() {
		return type;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public String getXfixedString(){
		String output = this.x + "";
		for (int i = 0; i < 3 - output.length();i++){
			output += "_";
		}
		return output;
	}

	public String getYfixedString(){
		String output = this.y + "";
		for (int i = 0; i < 3 - output.length();i++){
			output += "_";
		}
		return output;
	}

	public String getTypefixedString() {
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
	// TODO: write decoder for GameObj
}