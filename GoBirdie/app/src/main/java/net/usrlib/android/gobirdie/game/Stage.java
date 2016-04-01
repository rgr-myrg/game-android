package net.usrlib.android.gobirdie.game;

import android.app.Activity;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.usrlib.android.gobirdie.R;
import net.usrlib.android.gobirdie.asset.Font;
import net.usrlib.android.gobirdie.task.GameLoopTask;

public final class Stage {

	public static Paint sPaint = new Paint();

	private static Activity sActivity;
	private static View sGameLayout;
	private static TextView sScoreView;
	private static GameLoopTask sGameLoopTask;

	private static int sScore = 0;
	private static int sWidth;
	private static int sHeight;

	public static final void loadContentView(Activity activity) {
		sGameLayout = (View) activity.findViewById(R.id.activity_game_layout);
		sScoreView = (TextView) activity.findViewById(R.id.score_textview);
		sScoreView.setText(String.valueOf(sScore));
		sScoreView.setTypeface(Font.sTypewriter);

		sActivity = activity;
	}

	public static final void initWithSurfaceView(final Surface view) {
		sScore = 0;
		sWidth = view.getWidth();
		sHeight = view.getHeight();

		sPaint.setDither( true );
		sPaint.setFilterBitmap(true);

		sGameLoopTask = new GameLoopTask(view);
		Log.i("STAGE", "initWithSurfaceView " + String.valueOf(getWidth()) + "x" + String.valueOf(getHeight()));
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
						sGameLayout.setBackgroundResource(id);
					}
				}
		);
	}

	public static final void startGameLoop() {
		if (sGameLoopTask == null || sGameLoopTask.isRunning()) {
			return;
		}

		new Thread(sGameLoopTask).start();
	}

	public static final void stopGameLoop() {
		sGameLoopTask.stopTask();
	}

	public static final void updateScore() {
		sScore++;
		sActivity.runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						sScoreView.setText(String.valueOf(sScore));
					}
				}
		);
	}

}
