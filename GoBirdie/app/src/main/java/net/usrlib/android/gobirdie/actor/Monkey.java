package net.usrlib.android.gobirdie.actor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import net.usrlib.android.gobirdie.R;
import net.usrlib.android.gobirdie.game.World;
import net.usrlib.android.gobirdie.object.GameObject;
import net.usrlib.android.gobirdie.util.NumUtil;

public class Monkey extends GameObject {
	private static final int DEFAULT_VELOCITY = 5;
	private static final int TIME_OFFSET = 100;
	private final int sAnimationFrameRate = 10;

	private int velocity = 5;
	private int xOffset = DEFAULT_VELOCITY;

	private int[] evenNums = new int[4];

	private int iterationCount = 0;
	private int maxIterations;
	private int maxElapsed;
	private int xRightBoundary;

	private boolean isOffStage;

	private int elapsed = 0;

	public Monkey(Context context) {
		loadAnimationFrames(context.getResources());

		evenNums[0] = 1;
		evenNums[1] = 2;
		evenNums[2] = 4;

		//reset();
	}

	public void loadAnimationFrames(Resources resources) {
		setAnimationFrameRate(sAnimationFrameRate);

		addAnimationFrame(resources, R.drawable.monkey_frame_1);
		addAnimationFrame(resources, R.drawable.monkey_frame_2);
		addAnimationFrame(resources, R.drawable.monkey_frame_3);
		addAnimationFrame(resources, R.drawable.monkey_frame_2);
	}

	public void reset() {
		xRightBoundary = World.getWidth() - mAnimationFrames.getTopFrame().getWidth();
		isOffStage     = false;
		iterationCount = 0;
		maxIterations  = NumUtil.getRandIntFromList(evenNums);
		maxElapsed     = NumUtil.getRandInt( 1, 3 ) * TIME_OFFSET;
		elapsed = 0;

		final Bitmap topFrame = mAnimationFrames.getTopFrame();

		set(
				World.getWidth() + topFrame.getWidth(),
				0,
				topFrame.getWidth(),
				topFrame.getHeight()
		);
	}

	public void update() {
		// Prevent Calculations while off stage
		if ( !isOffStage ) {
			if ( iterationCount >= maxIterations ) {
				xOffset = velocity;
	
			} else {
				if ( x > xRightBoundary ) {
					xOffset = ( -velocity);
				} else if (x + xOffset < 0) {
					xOffset = velocity;
					iterationCount++;
				}
			}
	
			x = x + xOffset;

			if ( x > World.getWidth() + mAnimationFrames.getTopFrame().getWidth() ) {
				isOffStage = true;
			}
		} else {
			elapsed++;
			if ( elapsed >= maxElapsed ) {
				reset();
			}
		}

		updatePosition(x, y);
	}

	public void draw ( Canvas canvas ) {
		if ( isOffStage ) return;
		canvas.drawBitmap(mAnimationFrames.getCurrentFrame(), x, y, null);
	}
}
