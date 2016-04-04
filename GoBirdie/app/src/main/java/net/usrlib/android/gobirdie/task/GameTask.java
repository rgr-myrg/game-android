package net.usrlib.android.gobirdie.task;

import android.graphics.Canvas;

import net.usrlib.android.gobirdie.game.Surface;

public class GameTask implements Runnable {
	private Surface mSurfaceView;
	private volatile boolean mIsRunning;

	public GameTask(Surface surface) {
		mSurfaceView = surface;
	}

	@Override
	public void run() {
		do {
			//mSurfaceView.onUpdate();

			Canvas canvas = mSurfaceView.getHolder().lockCanvas();
			if (canvas != null) {
				synchronized (mSurfaceView.getHolder()) {
					mSurfaceView.onCanvasDraw(canvas);
				}

				mSurfaceView.getHolder().unlockCanvasAndPost(canvas);
			}

			mSurfaceView.onUpdate();

		} while (mIsRunning);
	}

	public boolean isRunning() {
		return mIsRunning;
	}

	public void init() {
		mIsRunning = true;
	}

	public void stopTask() {
		mIsRunning = false;
		Thread.currentThread().interrupt();
	}
}
