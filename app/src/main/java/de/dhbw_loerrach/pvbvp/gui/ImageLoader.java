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
            case MASTER:
                return scaleImage(R.drawable.master);
            case PANEL:
                return scaleImage(R.drawable.panel);
            case AIR:
                return scaleImage(R.drawable.air);
        }

        this.width = this.height = 0;
        return null;
    }

    private Bitmap scaleImage(int id) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), id);
        bm = Bitmap.createScaledBitmap(bm, width, height, false);
        return bm;
    }

}
