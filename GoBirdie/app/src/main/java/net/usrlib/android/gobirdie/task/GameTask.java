package net.usrlib.android.gobirdie.task;

import android.graphics.Canvas;
import android.util.Log;

import net.usrlib.android.gobirdie.game.Surface;

public class GameTask implements Runnable {
	// 1 millisecond = 1000000 nanoseconds
	private static final int DELAY_NANO_SECONDS = 100000;

	private Surface mSurfaceView;
	private volatile boolean mIsRunning;

	public GameTask(Surface surface) {
		mSurfaceView = surface;
	}

	@Override
	public void run() {
		long startTime = System.nanoTime();

		do {
			mSurfaceView.onUpdate();

			/*
			1 microsecond = 1000 nanoseconds
			1 millisecond = 1000 microseconds
			Target 60 FPS (1000/60) ~ 16.666 milliseconds
			16.666ms = 16666666 nanoseconds.
			1 ms = 1000000 ns
			*/

			long elapsed = System.nanoTime() - startTime;

			if (elapsed > DELAY_NANO_SECONDS) {
				mSurfaceView.onUpdate();
				//Log.d("GameTask", "elapsed: " + String.valueOf(elapsed));
				//mSurfaceView.onUpdateWithDelta((int) elapsed / DELAY_NANO_SECONDS);
			}

			Canvas canvas = mSurfaceView.getHolder().lockCanvas();

			if (canvas != null) {
				synchronized (mSurfaceView.getHolder()) {
					mSurfaceView.onCanvasDraw(canvas);
				}

				mSurfaceView.getHolder().unlockCanvasAndPost(canvas);
			}

			startTime = System.nanoTime();
		} while (mIsRunning);
	}

	public synchronized boolean isRunning() {
		return mIsRunning;
	}

	public void init() {
		mIsRunning = true;
	}

	public synchronized void stopTask() {
		mIsRunning = false;
		Thread.currentThread().interrupt();
	}
}
