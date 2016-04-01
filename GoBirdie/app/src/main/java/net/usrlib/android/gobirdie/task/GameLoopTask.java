package net.usrlib.android.gobirdie.task;

import android.graphics.Canvas;

import net.usrlib.android.gobirdie.game.Surface;

public class GameLoopTask implements Runnable {

	private Surface mGameView;
	private boolean mIsRunning;

	public GameLoopTask(Surface gameView) {
		mGameView = gameView;
	}

	@Override
	public void run() {
		mIsRunning = true;

		while (mIsRunning) {
			Canvas canvas = mGameView.getHolder().lockCanvas();

			if (canvas != null) {
				synchronized (mGameView.getHolder()) {
					mGameView.onCanvasDraw(canvas);
				}

				mGameView.getHolder().unlockCanvasAndPost(canvas);
			}

			mGameView.onUpdate();
		}
	}

	public boolean isRunning() {
		return mIsRunning;
	}

	public void stopTask() {
		mIsRunning = false;
	}

}
