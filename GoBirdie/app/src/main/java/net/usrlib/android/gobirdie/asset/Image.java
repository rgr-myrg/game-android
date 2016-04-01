package net.usrlib.android.gobirdie.asset;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import net.usrlib.android.gobirdie.R;

public class Image {
	public static Drawable sCherry;
	public static Drawable sGrapes;
	public static Drawable sOrange;
	public static Drawable sPear;

	public static Bitmap sBirdRight;
	public static Bitmap sBirdLeft;
	public static Bitmap sBirdDown;

	public static final void loadImages(Context context) {
		final Resources resources = context.getResources();

		// Use getResources() to support API 10. context.getDrawable requires API 21!
		sCherry = resources.getDrawable(R.drawable.fruit_cherry);
		sGrapes = resources.getDrawable(R.drawable.fruit_grapes);
		sOrange = resources.getDrawable(R.drawable.fruit_orange);
		sPear = resources.getDrawable(R.drawable.fruit_pear);

		sBirdRight = BitmapFactory.decodeResource(resources, R.drawable.bird_right);
		sBirdLeft = BitmapFactory.decodeResource(resources, R.drawable.bird_left);
		sBirdDown = BitmapFactory.decodeResource(resources, R.drawable.bird_down);
	}

}
