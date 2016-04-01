package net.usrlib.android.gobirdie.actor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import net.usrlib.android.gobirdie.R;
import net.usrlib.android.gobirdie.game.Stage;
import net.usrlib.android.gobirdie.object.GameObject;
import net.usrlib.android.gobirdie.util.NumUtil;

public class Snake extends GameObject {
	private static final int VELOCITY_X  = 2;
	private static final int TIME_OFFSET = 100;

	private final int sAnimationFrameRate = 10;

	private int xRightBoundary;
	private int xLeftBoundary;

	private int xOffset = VELOCITY_X;
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
		xRightBoundary = Stage.getWidth() + mAnimationFrames.getTopFrame().getWidth() * 2;
		xLeftBoundary  = ( -mAnimationFrames.getTopFrame().getWidth() * 2 );

		isTurningLeft = false;
		isOffStage    = false;
		maxElapsed    = NumUtil.getRandInt(1, 3) * TIME_OFFSET;
		elapsed = 0;

		final Bitmap topFrame = mAnimationFrames.getTopFrame();

		set(
				( -topFrame.getWidth() * 2 ),
				Stage.getHeight() - topFrame.getHeight(),
				topFrame.getWidth(),
				topFrame.getHeight()
		);
	}

	public void update() {
		if ( !isOffStage ) {
			if ( x > xRightBoundary ) {
				//crossed right boundary
				xOffset = ( -VELOCITY_X );
				isTurningLeft = true;

			} else if ( x < xLeftBoundary ) {
				//crossed left boundary
				xOffset = VELOCITY_X;
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

		updatePosition( x, y );
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
