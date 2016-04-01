package net.usrlib.android.gobirdie.gameview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.usrlib.android.gobirdie.asset.Actor;
import net.usrlib.android.gobirdie.asset.Sound;

// ToDo: Replace Facade Methods!!! Commenting out for now.

public class GameView extends SurfaceView implements IGameView {
	private boolean hasSurfaceCreated, isGameOver;

	public GameView( Context context ) {
		super( context );

		setZOrderOnTop( true );
		getHolder().setFormat( PixelFormat.TRANSPARENT );
		addHolderCallback();
	}

	@Override
	public boolean onTouchEvent( MotionEvent event ) {
		performClick();

		if ( isGameOver ) {
			// Restart Game
			hasSurfaceCreated = false;

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

	@Override
	public void onCanvasDraw(Canvas canvas) {
		canvas.drawColor( 0, PorterDuff.Mode.CLEAR );

		Actor.sBird.draw( canvas );
		Actor.sFruit.draw( canvas );
		Actor.sEagle.draw( canvas );
		Actor.sSnake.draw( canvas );
		Actor.sMonkey.draw( canvas );

		// For testing GameObject borders
		//Facade.HOUSE.draw( canvas );
	}

	@Override
	public void onUpdate() {
		new Thread(
			new Runnable() {
				public void run() {

					Actor.sBird.update();
					Actor.sEagle.update();
					Actor.sSnake.update();
					Actor.sMonkey.update();

					if ( isGameOver ) {
						return;
					}

					if ( Actor.sBird.intersectsWith( Actor.sFruit) && !Actor.sBird.hasFruit() ) {
//						Facade.SCORE++;
//						Facade.updateScore();

						Sound.playTone1();
						Actor.sBird.setHasFruit( true );
					}

					if ( Actor.sBird.hasFruit() ) {
						Actor.sFruit.updateWith( Actor.sBird.x, Actor.sBird.y + Actor.sBird.height );
					}

					if ( Actor.sBird.intersectsWith( Actor.sBirdhouse ) && Actor.sBird.hasFruit() ) {
//						Facade.SCORE++;
//						Facade.updateScore();

						Sound.playTone2();
						//birdhouse.disable();
						Actor.sBird.setHasFruit( false );
						Actor.sFruit.reset();
					}

					if ( Actor.sBird.intersectsWith( Actor.sMonkey ) ) {

						Sound.playSlap();
						Actor.sBird.onBump();
					}

					if ( Actor.sBird.intersectsWith( Actor.sSnake ) ) {

						Sound.playChirp();
						Actor.sBird.onTap();
					}

					if ( Actor.sBird.intersectsWith( Actor.sEagle ) ) {
						Actor.sBird.onHit();

						Sound.playAlarm();
						//birdhouse.disable();
					}

					if ( Actor.sBird.hasCrashed() ) {

						isGameOver = true;
						//Facade.SOUND_POOL.playBirdPunch();
						Sound.playPunch();
						//Facade.displayScoreTable();
					}

				}
			}
		).start();
	}

	@Override
	public void onSurfaceCreated() {
		if ( hasSurfaceCreated ) return;

//		Facade.setSurfaceView( this );
//		Facade.hideScoreTable();

		Canvas canvas = getHolder().lockCanvas();
		canvas.drawColor( 0, PorterDuff.Mode.CLEAR );

		Actor.sBird.draw( canvas );
		Actor.sFruit.draw( canvas );

		getHolder().unlockCanvasAndPost( canvas );

		hasSurfaceCreated = true;
		isGameOver = false;
	}

	@Override
	public void onSurfaceDestroyed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		//Facade.GAMELOOP.stop();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean performClick(){
		return super.performClick();
	}

	private void addHolderCallback() {
		getHolder().addCallback(
			new SurfaceHolder.Callback() {
				@Override
				public void surfaceChanged( SurfaceHolder holder, int format, int width, int height ) {
				}
				@Override
				public void surfaceCreated( SurfaceHolder holder ) {
					onSurfaceCreated();
				}
				@Override
				public void surfaceDestroyed( SurfaceHolder holder ) {
					onSurfaceDestroyed();
				}
			}
		);
	}
}
