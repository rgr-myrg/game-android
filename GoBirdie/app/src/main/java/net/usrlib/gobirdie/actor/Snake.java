package net.usrlib.gobirdie.actor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import net.usrlib.gobirdie.R;
import net.usrlib.gobirdie.game.GameObject;
import net.usrlib.gobirdie.game.World;
import net.usrlib.gobirdie.util.NumUtil;

public class Snake extends GameObject {
	private static final float DEFAULT_VELOCITY = 1.0f;
	private static final int TIME_OFFSET = 100;

	private final int sAnimationFrameRate = 10;

	private int xRightBoundary;
	private int xLeftBoundary;

	private float velocity = DEFAULT_VELOCITY;
	private float xOffset = velocity;
	private int maxElapsed;

	private boolean isTurningLeft;
	private boolean isOffStage;

	private Matrix mirrorMatrix = new Matrix();

	private int elapsed = 0;

	public Snake(Context context) {
		loadAnimationFrames(context.getResources());

		//reset();
	}

	public void loadAnimationFrames(Resources resources) {
		setAnimationFrameRate(sAnimationFrameRate);

		addAnimationFrame(resources, R.drawable.snake_frame_2);
		addAnimationFrame(resources, R.drawable.snake_frame_3);
		addAnimationFrame(resources, R.drawable.snake_frame_4);
		addAnimationFrame(resources, R.drawable.snake_frame_3);
	}

	public void reset() {
		xRightBoundary = World.getWidth() + mAnimationFrames.getTopFrame().getWidth() * 2;
		xLeftBoundary  = ( -mAnimationFrames.getTopFrame().getWidth() * 2 );

		isTurningLeft = false;
		isOffStage    = false;
		maxElapsed    = NumUtil.getRandInt(1, 3) * TIME_OFFSET;
		elapsed = 0;

		final Bitmap topFrame = mAnimationFrames.getTopFrame();

		set(
				(-topFrame.getWidth() * 2),
				World.getHeight() - topFrame.getHeight(),
				topFrame.getWidth(),
				topFrame.getHeight()
		);
	}

	public void update() {
		if ( !isOffStage ) {
			if ( x > xRightBoundary ) {
				//crossed right boundary
				xOffset = ( -velocity);
				isTurningLeft = true;

			} else if ( x < xLeftBoundary ) {
				//crossed left boundary
				xOffset = velocity;
				isTurningLeft = false;
				isOffStage = true;
			}

			x = x + xOffset;
		} else {
			elapsed++;
			if ( elapsed >= maxElapsed ) {
				reset();
			}
		}

		updatePosition(x, y);
	}

	public void draw( Canvas canvas ) {
		if ( isOffStage ) return;

		if ( isTurningLeft ) {
			mirrorMatrix.setScale( -1, 1 );
			mirrorMatrix.postTranslate( x, y );

			canvas.drawBitmap( mAnimationFrames.getCurrentFrame(), mirrorMatrix, null );
		} else {
			canvas.drawBitmap( mAnimationFrames.getCurrentFrame(), x , y, null );
		}
	}
}
