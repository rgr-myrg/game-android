package net.usrlib.gobirdie.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Surface extends SurfaceView {
	private boolean mHasSurfaceCreated = false;
	private boolean mIsGameOver = false;
	private boolean mIsContinue = false;

	private SurfaceCallback callback = null;

	public interface SurfaceCallback {
		void onSurfaceCreated();
		void onSurfaceDestroyed();
		void onScoreUpdate(int score);
		void onGameOver(int score);
		void onReadyToContinue();
	}

	public Surface(Context context, SurfaceCallback callback) {
		super(context);

		this.callback = callback;

		setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.TRANSPARENT);

		getHolder().addCallback(
				new SurfaceHolder.Callback() {
					@Override
					public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
					}

					@Override
					public void surfaceCreated(SurfaceHolder holder) {
						onSurfaceCreated();
						callback.onSurfaceCreated();
					}

					@Override
					public void surfaceDestroyed(SurfaceHolder holder) {
						onSurfaceDestroyed();
						callback.onSurfaceDestroyed();
					}
				}
		);
	}

	public void onSurfaceCreated() {
		if (mHasSurfaceCreated) {
			return;
		}

		World.initWithSurfaceView(this);
		World.resetActors();

		Canvas canvas = getHolder().lockCanvas();
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);

		World.sBird.draw(canvas);
		World.sFruit.draw(canvas);

		getHolder().unlockCanvasAndPost(canvas);

		mHasSurfaceCreated = true;
		mIsGameOver = false;
	}

	public void onSurfaceDestroyed() {
		Log.i("SURFACE", "onSurfaceDestroyed!!!");
		World.stopGameLoop();
	}

	public boolean onTouchEvent( MotionEvent event ) {
		performClick();

		if (mIsGameOver) {
			// Signal PlayActivity the user has tapped on the screen
			// and is ready to continue.
			if (mIsContinue) {
				callback.onReadyToContinue();
			}

			return false;
		}

		if (!World.isGameLoopRunning()) {
			World.startGameLoop();
		}

		World.sBird.onTap();

		return true;
	}

	public void onCanvasDraw(final Canvas canvas) {
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		World.drawActors(canvas);
	}

	public void onUpdate() {
		World.sBird.update();
		World.sEagle.update();
		World.sSnake.update();
		World.sMonkey.update();

		// take from the queue
		if (mIsGameOver) {
			return;
		}

		if (World.sBird.intersectsWith(World.sFruit) && !World.sBird.hasFruit()) {
			World.updateScore();
			callback.onScoreUpdate(World.getScore());

			World.playTone1(getContext());
			World.sBird.setHasFruit(true);
		}

		if (World.sBird.hasFruit()) {
			World.sFruit.updateWith((int)World.sBird.x, (int)World.sBird.y + (int)World.sBird.height);
		}

		if (World.sBird.intersectsWith(World.sBirdhouse) && World.sBird.hasFruit()) {
			World.updateScore();
			callback.onScoreUpdate(World.getScore());

			World.playTone2(getContext());
			//birdhouse.disable();
			World.sBird.setHasFruit(false);
			World.sFruit.reset();
		}

		if (World.sBird.intersectsWith(World.sMonkey)) {

			World.playSlap(getContext());
			World.sBird.onBump();
		}

		if (World.sBird.intersectsWith(World.sSnake)) {

			World.playChirp(getContext());
			World.sBird.onTap();
		}

		if (World.sBird.intersectsWith(World.sEagle)) {
			World.sBird.onHit();

			World.playAlarm(getContext());
			//birdhouse.disable();
		}

		if (World.sBird.hasCrashed()) {
			mIsGameOver = true;
			onGameOver();
		}
	}

	public void playReadyToContinue() {
		mIsContinue = true;
	}

	private void onGameOver() {
		World.playPunch(getContext());
		World.saveScoreToPreferences(getContext());

		callback.onGameOver(World.getScore());
	}
}
