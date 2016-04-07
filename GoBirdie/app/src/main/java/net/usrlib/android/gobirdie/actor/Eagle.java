package net.usrlib.android.gobirdie.actor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import net.usrlib.android.gobirdie.R;
import net.usrlib.android.gobirdie.game.World;
import net.usrlib.android.gobirdie.object.GameObject;
import net.usrlib.android.gobirdie.util.NumUtil;

public class Eagle extends GameObject {
	private final int DEFAULT_VELOCITY = 3;
	private final int sAnimationFrameRate = 15;
	private int xRightBoundary;
	private int yTopBoundary;

	private int velocity = DEFAULT_VELOCITY;
	private int offsetX, offsetY;

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
			velocity = 5;
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
	
		y = (int) (y + offsetY);

		if ( x > World.getWidth() ) {
			offsetX = ( -velocity );
		} else if ( x < xRightBoundary ) {
			offsetX = velocity;
		}

		x = x + offsetX;

		updatePosition( x, y );
	}

	public void draw ( Canvas canvas ) {
		canvas.drawBitmap(mAnimationFrames.getCurrentFrame(), x, y, null);
	}
}
