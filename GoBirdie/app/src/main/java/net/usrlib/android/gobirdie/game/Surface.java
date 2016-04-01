package net.usrlib.android.gobirdie.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.usrlib.android.gobirdie.asset.Sound;

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

		Stage.initWithSurfaceView(this);
		Actor.resetActors();

		Canvas canvas = getHolder().lockCanvas();
		canvas.drawColor( 0, PorterDuff.Mode.CLEAR );

		Actor.sBird.draw(canvas);
		Actor.sFruit.draw(canvas);

		getHolder().unlockCanvasAndPost(canvas);

		mHasSurfaceCreated = true;
		mIsGameOver = false;
	}

	public void onSurfaceDestroyed() {
		Log.i("SURFACE", "onSurfaceDestroyed!!!");

	}

	public boolean onTouchEvent( MotionEvent event ) {
		performClick();

		Log.d("SURFACE", "onTouchEvent");
		if (mIsGameOver) {
			// Restart Game
			mHasSurfaceCreated = false;

//			Facade.GAMELOOP.stop();
//			Facade.setGameBackground();

			onSurfaceCreated();
			return true;
		}

//		if ( !Facade.GAMELOOP.isRunning() ) {
//			Facade.startGameLoop();
//		}

		Actor.sBird.onTap();

		return true;
	}

	public void onCanvasDraw(Canvas canvas) {
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		Actor.drawActors(canvas);

//		Actor.sBird.draw(canvas);
//		Actor.sFruit.draw(canvas);
//		Actor.sEagle.draw(canvas);
//		Actor.sSnake.draw(canvas);
//		Actor.sMonkey.draw(canvas);
	}

	public void onUpdate() {
		new Thread(
				new Runnable() {
					public void run() {
						Actor.sBird.update();
						Actor.sEagle.update();
						Actor.sSnake.update();
						Actor.sMonkey.update();

						if (mIsGameOver) {
							return;
						}

						if (Actor.sBird.intersectsWith( Actor.sFruit) && !Actor.sBird.hasFruit()) {
//						Facade.SCORE++;
//						Facade.updateScore();

							Sound.playTone1();
							Actor.sBird.setHasFruit( true );
						}

						if (Actor.sBird.hasFruit()) {
							Actor.sFruit.updateWith(Actor.sBird.x, Actor.sBird.y + Actor.sBird.height);
						}

						if (Actor.sBird.intersectsWith(Actor.sBirdhouse) && Actor.sBird.hasFruit()) {
//						Facade.SCORE++;
//						Facade.updateScore();

							Sound.playTone2();
							//birdhouse.disable();
							Actor.sBird.setHasFruit(false);
							Actor.sFruit.reset();
						}

						if ( Actor.sBird.intersectsWith(Actor.sMonkey)) {

							Sound.playSlap();
							Actor.sBird.onBump();
						}

						if ( Actor.sBird.intersectsWith(Actor.sSnake)) {

							Sound.playChirp();
							Actor.sBird.onTap();
						}

						if ( Actor.sBird.intersectsWith(Actor.sEagle)) {
							Actor.sBird.onHit();

							Sound.playAlarm();
							//birdhouse.disable();
						}

						if (Actor.sBird.hasCrashed()) {

							mIsGameOver = true;
							Sound.playPunch();
							//Facade.displayScoreTable();
						}
					}
				}
		).start();
	}

}
