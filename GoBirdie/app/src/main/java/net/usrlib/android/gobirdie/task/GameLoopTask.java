package net.usrlib.android.gobirdie.task;

import android.graphics.Canvas;

import net.usrlib.android.gobirdie.gameview.IGameView;

public class GameLoopTask implements Runnable {

	public static final long FPS = 1000 / 60;

	private IGameView mGameView;
	private boolean mIsRunning;

	public GameLoopTask(IGameView gameView) {
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

}
