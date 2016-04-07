package net.usrlib.android.gobirdie.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Surface extends SurfaceView {
	private boolean mHasSurfaceCreated;
	private boolean mIsGameOver;

	public Surface(Context context) {
		super(context);

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
					}

					@Override
					public void surfaceDestroyed(SurfaceHolder holder) {
						onSurfaceDestroyed();
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
		canvas.drawColor( 0, PorterDuff.Mode.CLEAR );

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
			// Restart Game
			mHasSurfaceCreated = false;

			onSurfaceCreated();
			return true;
		}

		World.startGameLoop();
		World.sBird.onTap();

		return true;
	}

	public void onCanvasDraw(final Canvas canvas) {
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		World.drawActors(canvas);
	}

	public void onCanvasDrawWithDelta(final Canvas canvas, float delta) {
		Log.d("SURFACE", "delta: " + String.valueOf(delta));

		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		World.drawActors(canvas);
	}

	public void onUpdate() {
//		new Thread(
//				new Runnable() {
//					@Override
//					public void run() {
//						updateActors();
//					}
//				}
//		).start();

		updateActors();
	}

	private void updateActors() {
		World.sBird.update();
		World.sEagle.update();
		World.sSnake.update();
		World.sMonkey.update();

		if (mIsGameOver) {
			return;
		}

		if (World.sBird.intersectsWith(World.sFruit) && !World.sBird.hasFruit()) {
			World.updateScore();

			World.playTone1();
			World.sBird.setHasFruit(true);
		}

		if (World.sBird.hasFruit()) {
			World.sFruit.updateWith(World.sBird.x, World.sBird.y + World.sBird.height);
		}

		if (World.sBird.intersectsWith(World.sBirdhouse) && World.sBird.hasFruit()) {
			World.updateScore();

			World.playTone2();
			//birdhouse.disable();
			World.sBird.setHasFruit(false);
			World.sFruit.reset();
		}

		if ( World.sBird.intersectsWith(World.sMonkey)) {

			World.playSlap();
			World.sBird.onBump();
		}

		if ( World.sBird.intersectsWith(World.sSnake)) {

			World.playChirp();
			World.sBird.onTap();
		}

		if ( World.sBird.intersectsWith(World.sEagle)) {
			World.sBird.onHit();

			World.playAlarm();
			//birdhouse.disable();
		}

		if (World.sBird.hasCrashed()) {

			mIsGameOver = true;
			World.playPunch();
			//Facade.displayScoreTable();
		}
	}
}
