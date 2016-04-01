package net.usrlib.android.gobirdie.actor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import net.usrlib.android.gobirdie.R;
import net.usrlib.android.gobirdie.game.Stage;
import net.usrlib.android.gobirdie.object.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Bird extends GameObject {
	private static final int VELOCITY_X = 5;
	private static final int VELOCITY_Y = 7;
	private static final int FALL_VELOCITY  = 15;
	private static final int JUMP_DISTANCE  = 25;
	private static final int ROTATE_DEGREES = 45;

	private int xOffset = VELOCITY_X;

	private boolean flying;
	private boolean directionLeft;
	private boolean directionDown;
	private boolean isFalling;
	private boolean isCrashed;
	private boolean isBumped;
	private boolean hasFruit;

	private double angle = 0.0;
	private double arc = 0.1;

	private int degrees = 0;
	private int count = 0;

	private int xRightBoundary;
	private int floorBoundary;
	private int halfScreenBoundary;

	private final Matrix mirrorMatrix = new Matrix();

	private final List<BirdBitMap> mBitMaps = new ArrayList<BirdBitMap>();

	private BirdBitMap mBitMap;

	public Bird(Context context) {
		loadBitMaps(context);
	}

	public void reset() {
		xRightBoundary = Stage.getWidth() - mBitMap.right.getWidth() - xOffset;
		floorBoundary  = Stage.getHeight() - mBitMap.down.getHeight();
		halfScreenBoundary = Stage.getHeight() / 2;

		set(
				Stage.getWidth() / 2 - mBitMap.right.getWidth() / 2,
				Stage.getHeight() - mBitMap.right.getHeight() * 4,
				mBitMap.right.getWidth(),
				mBitMap.right.getHeight()
		);

		degrees = 0;
		isFalling = false;
		isCrashed = false;
		isBumped  = false;
		hasFruit  = false;
		directionDown = false;
	}

	public void update() {
		if ( directionDown ) {
			return;
		}

		if ( isBumped ) {
			y = y + FALL_VELOCITY;
			if ( y > halfScreenBoundary ) {
				isBumped = false;
			}
		}

		if ( isFalling ) {
			if ( y < floorBoundary ) {
				y = y + FALL_VELOCITY;
			} else {
				isCrashed = true;
			}

			return;
		}

		if ( x > xRightBoundary ) {
			xOffset = (-VELOCITY_X);
			flying = false;
			directionLeft = true;
		} else if ( x + xOffset < 0 ) {
			xOffset = VELOCITY_X;
			flying = false;
			directionLeft = false;
		}

		x = x + xOffset;

		if ( y < 0 ) {
			y = 0;
			flying = false;
		} else if ( y > floorBoundary ) {
			onTap();
		}

		if ( !flying ) {
			y = (int) (y + VELOCITY_Y);
		} else {
			if ( directionLeft ) {
				x = x - (int)( JUMP_DISTANCE * Math.sin(angle) );
				y = y - (int)( JUMP_DISTANCE * Math.cos(angle) );
			} else {
				x = x + (int)( JUMP_DISTANCE * Math.sin(angle) );
				y = y - (int)( JUMP_DISTANCE * Math.cos(angle) );
			}

			angle += arc;
			count++;

			if ( count == JUMP_DISTANCE ) {
				flying = false;
				count = 0;
				angle = 0;
			}
		}

		updatePosition( x, y );
	}
	 
	public void draw(Canvas canvas) {
		if ( isFalling ) {
			if ( directionLeft ) {
				mirrorMatrix.setRotate( (degrees < ROTATE_DEGREES ? degrees++ : degrees) );
			} else {
				mirrorMatrix.setRotate( (degrees > -ROTATE_DEGREES ? degrees-- : degrees) );
			}

			mirrorMatrix.postTranslate( x, y );

			canvas.drawBitmap( mBitMap.down, mirrorMatrix, Stage.sPaint);
		} else {
			if ( directionLeft ) {
				canvas.drawBitmap( mBitMap.left, x , y, null );
			} else {
				canvas.drawBitmap( mBitMap.right, x , y, null );
			}
		}
	}

	public void onTap(){
		flying = true;
		count  = 0;
		angle  = 0;
	}

	public void onHit() {
		isFalling = true;
	}

	public void onBump() {
		isBumped = true;
	}

	public boolean hasCrashed() {
		return isCrashed;
	}

	public void setHasFruit( boolean flag ) {
		hasFruit = flag;
	}

	public boolean hasFruit() {
		return hasFruit;
	}

	public void stop() {
		directionDown = true;
	}

	private void loadBitMaps(Context context) {
		mBitMaps.add(
				new BirdBitMap(
						R.drawable.bird_right,
						R.drawable.bird_left,
						R.drawable.bird_down,
						context
				)
		);
		mBitMaps.add(
				new BirdBitMap(
						R.drawable.bird_blue_right,
						R.drawable.bird_blue_left,
						R.drawable.bird_blue_down,
						context
				)
		);
		mBitMaps.add(
				new BirdBitMap(
						R.drawable.bird_pink_right,
						R.drawable.bird_pink_left,
						R.drawable.bird_pink_down,
						context
				)
		);
		mBitMaps.add(
				new BirdBitMap(
						R.drawable.bird_purple_right,
						R.drawable.bird_purple_left,
						R.drawable.bird_purple_down,
						context
				)
		);
		mBitMaps.add(
				new BirdBitMap(
						R.drawable.bird_red_right,
						R.drawable.bird_red_left,
						R.drawable.bird_red_down,
						context
				)
		);

		// Grab a bitmap for placing on stage.
		// Go up incrementally depending on achievement to swap into a new actor.
		mBitMap = mBitMaps.get( 0 );
	}

	private static class BirdBitMap {
		public final Bitmap right, left, down;

		public BirdBitMap( int right, int left, int down, Context view ) {
			this.right = BitmapFactory.decodeResource(view.getResources(), right);
			this.left  = BitmapFactory.decodeResource(view.getResources(), left);
			this.down  = BitmapFactory.decodeResource(view.getResources(), down);
		}

	}
}
