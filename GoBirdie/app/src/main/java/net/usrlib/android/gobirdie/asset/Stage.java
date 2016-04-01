package net.usrlib.android.gobirdie.asset;

import android.graphics.Paint;
import android.view.SurfaceView;

public final class Stage {

	public static Paint sPaint = new Paint();

	private static int sScore;
	private static int sWidth;
	private static int sHeight;

	public static final void initWithSurfaceView(final SurfaceView view) {
		sScore = 0;
		sWidth = view.getWidth();
		sHeight = view.getHeight();

		sPaint.setDither( true );
		sPaint.setFilterBitmap( true );
	}

	public static final int getWidth() {
		return sWidth;
	}

	public static final int getHeight() {
		return sHeight;
	}

}
