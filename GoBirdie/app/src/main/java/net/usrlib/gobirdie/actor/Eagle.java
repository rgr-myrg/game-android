package net.usrlib.gobirdie.actor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import net.usrlib.gobirdie.R;
import net.usrlib.gobirdie.game.GameObject;
import net.usrlib.gobirdie.game.World;
import net.usrlib.gobirdie.util.NumUtil;

public class Eagle extends GameObject {
	private static final float DEFAULT_VELOCITY = 3.0f;
	private static final float MAX_VELOCITY = 30;

	private final int sAnimationFrameRate = 15;
	private int xRightBoundary;
	private int yTopBoundary;

	private float velocity = DEFAULT_VELOCITY;
	private float offsetX, offsetY;

	public Eagle(Context context) {
		loadAnimationFrames(context.getResources());

		xRightBoundary = (-mAnimationFrames.getTopFrame().getWidth());
		yTopBoundary = (-mAnimationFrames.getTopFrame().getHeight() * 2);

		//reset();
	}

	public void loadAnimationFrames(Resources resources) {
		setAnimationFrameRate(sAnimationFrameRate);

		addAnimationFrame(resources, R.drawable.eagle_frame_1);
		addAnimationFrame(resources, R.drawable.eagle_frame_2);
		addAnimationFrame(resources, R.drawable.eagle_frame_3);
		addAnimationFrame(resources, R.drawable.eagle_frame_2);
	}

	public void reset() {
		//Reduce Collider GameObject by 85%
		//setPadding( .85f );

		final Bitmap topFrame = mAnimationFrames.getTopFrame();

		set(
				NumUtil.getRandInt(0, World.getWidth() - topFrame.getWidth() * 2),
				0 - topFrame.getHeight() * 2,
				topFrame.getWidth(),
				topFrame.getHeight()
		);

		if ( World.getWidth() > 520 ) {
			//velocity = 5;
		}

		offsetX = velocity;
		offsetY = velocity;
	}

	public void update() {
		if ( y > World.getHeight() - mAnimationFrames.getTopFrame().getHeight() - offsetY ) {
			offsetY = ( -velocity );
		} else if ( y < yTopBoundary ) {
			offsetY = velocity;
		}
	
		y = y + offsetY;

		if ( x > World.getWidth() ) {
			offsetX = ( -velocity );
		} else if ( x < xRightBoundary ) {
			offsetX = velocity;
		}

		x = x + offsetX;

		updatePosition( x, y );
		// Reset velocity
		// velocity = DEFAULT_VELOCITY;
	}

	public void updateWithDelta(int delta) {
		velocity = delta > DEFAULT_VELOCITY ? delta - DEFAULT_VELOCITY : DEFAULT_VELOCITY - delta;

		if (velocity > MAX_VELOCITY) {
			velocity = MAX_VELOCITY;
		}

		Log.d("Eagle", "start velocity: " + String.valueOf(velocity));

		//velocity = velocity + delta;
		//update();
//		velocity = delta < mLastDelta ? DEFAULT_VELOCITY : delta;
//		Log.d("Eagle", "end velocity: " + String.valueOf(velocity));
//		mLastDelta = delta;
	}

	public void draw ( Canvas canvas ) {
		canvas.drawBitmap(mAnimationFrames.getCurrentFrame(), x, y, null);
	}
}
