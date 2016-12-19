package net.usrlib.gobirdie.game;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

public class GameObject {
	public PositionCoordinates position = new PositionCoordinates();
	public float x;
	public float y;
	public float width;
	public float height;

	public float minX;
	public float minY;
	public float maxX;
	public float maxY;

	protected float padding = 0;
	protected AnimationFrames mAnimationFrames = new AnimationFrames();

	public GameObject() {}

	public GameObject(float x, float y, float width, float height) {
		set(x, y, width, height);
	}

	public void set( float x, float y, float width, float height ) {
		this.x = x;
		this.y = y;
		this.width  = width;
		this.height = height;

		updatePadding();
	}

	public void moveTo(float x, float y, float minX, float maxX, float minY, float maxY)  {
		this.x = x;
		this.y = y;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public void moveWithPositionCoordinates(PositionCoordinates coordinates) {
		moveTo(
				coordinates.x,
				coordinates.y,
				coordinates.minX,
				coordinates.maxX,
				coordinates.minY,
				coordinates.maxY
		);
	}
	public void setPadding( float pad ) {
		padding = pad;
	}

	public void updatePosition( float x, float y ) {
		this.x = x;
		this.y = y;

		updatePadding();
	}

	public boolean intersectsWith( GameObject gameObject) {
		if ( maxX < gameObject.minX ) return false;
		if ( minX > gameObject.maxX ) return false;
		if ( maxY < gameObject.minY ) return false;
		if ( minY > gameObject.maxY ) return false;

		return true;
	}

	public void updatePadding() {
		if ( padding > 0 ) {
			minX = (int) (x * padding * 2);
			minY = y;
			maxX = (int) (x + width * padding);
			maxY = (int) (y + height * padding * .85);
		} else {
			minX = x;
			minY = y;
			maxX = x + width;
			maxY = y + height;
		}

		position.setPositionCoordinates(x, y, minX, maxX, minY, maxY);
	}

	public void setAnimationFrameRate(final int frameRate) {
		mAnimationFrames.setFrameRate(frameRate);
	}

	public void addAnimationFrame(final Resources resources, final int drawableId) {
		mAnimationFrames.add(BitmapFactory.decodeResource(resources, drawableId));
	}

	public static class PositionCoordinates {
		public float x, y, minX, maxX, minY, maxY;

		public final void setPositionCoordinates(float x, float y, float minX, float maxX, float minY, float maxY) {
			this.x = x;
			this.y = y;
			this.minX = minX;
			this.maxX = maxX;
			this.minY = minY;
			this.maxY = maxY;
		}
	}
}

// Example
//		minX = (int) (x * 1.8);
//		minY = y;
//		maxX = (int) (x + width * .9);
//		maxY = (int) (y + height * .7);

/*
 * http://gamemath.com/2011/09/detecting-whether-two-boxes-overlap
 */
/*
bool BoxesIntersect(const Box2D &a, const Box2D &b)
{
    if (a.max.x < b.min.x) return false; // a is left of b
    if (a.min.x > b.max.x) return false; // a is right of b
    if (a.max.y < b.min.y) return false; // a is above b
    if (a.min.y > b.max.y) return false; // a is below b
    return true; // boxes overlap
}
*/