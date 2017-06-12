package de.dhbw_loerrach.pvbvp.function;

/**
 * Created by weva on 06.04.2017.
 * <p>
 * Ball-Object extends GameObj
 * var char side: 'l', 'r', 'm'
 * var boolean master
 */
public class Brick extends GameObj {
	
	private char side;
	private int destructionStage = 0;
	/**
	 * @param side select side (left / right) of Block, also sets master true if master block
	 */
	public Brick(GameObjType type, char side) {
		this.type = type;
		this.side = side;
	}
	
	public char getSide() {
		return side;
	}

	public void destruct() {
		this.destructionStage = 3;
		this.type = GameObjType.DESTUCTEDBRICK;
	}

	public int getDestructionStage() {
		if (this.destructionStage > 0) {
			return this.destructionStage--;
		}
		else {
			this.type = GameObjType.AIR;
			return 0;
		}
	}

	public String toString() {
		return ""+this.getTypefixedString()+this.getXfixedString()+this.getYfixedString()+side;
	}
}
