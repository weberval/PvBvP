package de.dhbw_loerrach.pvbvp.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import de.dhbw_loerrach.pvbvp.R;

/**
 * Created by Renat on 07.04.2017.
 * Loading Images
 */
public class ImageLoader {
	
	private Context context;
	private int width;
	private int height;
	
	public ImageLoader(Context context) {
		this.context = context;
	}
	
	public Bitmap getImage(GameObjImage obj, int width, int height) {
		
		this.width = width;
		this.height = height;
		
		switch (obj) {
			case BALL:
				return scaleImage(R.drawable.ball);
			case BRICK_LEFT:
				return scaleImage(R.drawable.brick_left);
			case BRICK_RIGHT:
				return scaleImage(R.drawable.brick_right);
			case BRICK_DESTRUCTED_1_LEFT:
				return scaleImage(R.drawable.brick_destroy1_left);
			case BRICK_DESTRUCTED_2_LEFT:
				return scaleImage(R.drawable.brick_destroy2_left);
			case BRICK_DESTRUCTED_3_LEFT:
				return scaleImage(R.drawable.brick_destroy3_left);
			case BRICK_DESTRUCTED_1_RIGHT:
				return scaleImage(R.drawable.brick_destroy1_right);
			case BRICK_DESTRUCTED_2_RIGHT:
				return scaleImage(R.drawable.brick_destroy2_right);
			case BRICK_DESTRUCTED_3_RIGHT:
				return scaleImage(R.drawable.brick_destroy3_right);
			case MASTER:
				return scaleImage(R.drawable.master);
			case PANEL:
				return scaleImage(R.drawable.panel);
		}
		
		this.width = this.height = 0;
		return null;
	}

	public Bitmap[] getBallTrace(int width, int height){
		this.width = width;
		this.height = height;

		Bitmap[] bm = new Bitmap[GameView.numberOfTraces];
		bm[0] = scaleImage(R.drawable.ball_t3);
		bm[1] = scaleImage(R.drawable.ball_t2);
		bm[2] = scaleImage(R.drawable.ball_t1);

		return bm;

	}


	private Bitmap scaleImage(int id) {
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(), id);
		bm = Bitmap.createScaledBitmap(bm, width, height, false);
		return bm;
	}
}
