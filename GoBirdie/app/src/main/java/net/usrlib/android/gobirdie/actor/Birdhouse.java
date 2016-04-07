package net.usrlib.android.gobirdie.actor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import net.usrlib.android.gobirdie.game.World;
import net.usrlib.android.gobirdie.object.GameObject;

public class Birdhouse extends GameObject {
//	private boolean active = true;
	private static int mBoxBounds = 50;

	public Birdhouse() {
	}

	public void reset() {
		if ( World.getWidth() > 520 ) {
			mBoxBounds = 100;
		}

		set(
				World.getWidth() / 2 - mBoxBounds / 2,
				World.getHeight() - World.getHeight() / 5,
				mBoxBounds,
				mBoxBounds
		);
	}
//	public void disable() {
//		active = false;
//	}
//
//	public void enable() {
//		active = true;
//	}

	public void draw( Canvas canvas ) {
//		Makes Collider Visible. Testing Only
		Rect r = new Rect( minX, minY, maxX, maxY);
		World.sPaint.setStyle(Paint.Style.STROKE);
		World.sPaint.setColor(Color.RED);
		canvas.drawRect(r, World.sPaint);
	}
}
