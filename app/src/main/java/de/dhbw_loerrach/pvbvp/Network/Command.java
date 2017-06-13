package de.dhbw_loerrach.pvbvp.Network;

/**
 * Created by weva on 5/29/17.
 */

public enum Command {
	INI(0),
	UPD(1),

	TER(9);
	public int index;

	Command(int index) {
		this.index = index;
	}
}
