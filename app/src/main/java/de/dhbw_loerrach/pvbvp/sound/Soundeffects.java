package de.dhbw_loerrach.pvbvp.sound;


import android.media.MediaPlayer;
import android.media.MediaTimestamp;

import de.dhbw_loerrach.pvbvp.Main;
import de.dhbw_loerrach.pvbvp.R;

/**
 * Created by weva on 6/12/17.
 */

public class Soundeffects {

	/**
	 * This class provides sound to other classes. <br />
	 * Currently available are a knock, rrrrt, tick and blink-sounds. <br />
	 * A music track is also available. (see licence)
	 */

	private static float volumeMusic = 1;
	private static float volumeEffects = 0.5f;

	private static MediaPlayer knock;
	private static MediaPlayer rrrrt;
	private static MediaPlayer tick;
	private static MediaPlayer blink;

	private static MediaPlayer music;

	public static void init(Main main){

		/**
		 * setting music file if not already done
		 */
		if (music == null) {
			music = MediaPlayer.create(main, R.raw.headknocker);
			music.setLooping(true);
			music.setVolume(volumeMusic, volumeMusic);
			music.start();
		}


		/**
		 * setting audio effect files  if not already done
		 */
		if (knock == null)
			knock = MediaPlayer.create(main, R.raw.knock);
		if (rrrrt == null)
			rrrrt = MediaPlayer.create(main, R.raw.rrrrt);
		if (tick == null)
			tick = MediaPlayer.create(main, R.raw.tick);
		if (blink == null)
			blink = MediaPlayer.create(main, R.raw.blink);

		/**
		 * setting sound effects volume
		 */
		knock.setVolume(volumeEffects, volumeEffects);
		rrrrt.setVolume(volumeEffects, volumeEffects);
		tick.setVolume(volumeEffects, volumeEffects);
		blink.setVolume(volumeEffects, volumeEffects);
	}

	public static void setVolumeEffects(float volume) {
		Soundeffects.volumeEffects = volume;
	}

	public static void setVolumeMusic(float volume) {
		Soundeffects.volumeMusic = volume;
	}

	public static void playKock() {
		knock.start();
	}

	public static void playRrrrt() {
		rrrrt.start();
	}

	public static void playTick() {
		tick.start();
	}

	public static void playBlink() {
		blink.start();
	}

	public static void startMusic(){
		music.start();
	}

	public static void stopMusic(){
		music.stop();
	}
}