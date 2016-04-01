package net.usrlib.android.gobirdie.game;

import android.app.Activity;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.view.View;

import net.usrlib.android.gobirdie.R;

public final class Stage {

	public static Paint sPaint = new Paint();

	private static Activity sActivity;
	private static View mGameLayout;

	private static int sScore;
	private static int sWidth;
	private static int sHeight;

	public static final void loadContentView(Activity activity) {
		mGameLayout = (View) activity.findViewById(R.id.activity_game_layout);
		sActivity = activity;
	}

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

	public static final void setBackgroundDayTime() {
		setGameBackground(R.drawable.bg_game_1);
	}

	public static final void setBackgroundNightTime() {
		setGameBackground(R.drawable.bg_game_2);
	}

	public static final void setGameBackground(final int id) {
		sActivity.runOnUiThread(
				new Runnable() {
					public void run() {
						mGameLayout.setBackgroundResource(id);
					}
				}
		);
	}

}
