package de.dhbw_loerrach.pvbvp.function;

/**
 * Created by weva on 04.04.2017.
 * super GameObj
 */
public class GameObj {
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
}
