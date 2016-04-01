package net.usrlib.android.gobirdie.actor;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import net.usrlib.android.gobirdie.asset.Image;
import net.usrlib.android.gobirdie.game.Stage;
import net.usrlib.android.gobirdie.object.GameObject;
import net.usrlib.android.gobirdie.util.NumUtil;

import java.util.ArrayList;
import java.util.List;

public class Fruit extends GameObject {
	private List<Drawable> fruits = new ArrayList<Drawable>();
	private static Drawable image;

//	private boolean active = true;

	public Fruit() {
		fruits.add(Image.sCherry);
		fruits.add(Image.sGrapes);
		fruits.add(Image.sOrange);
		fruits.add(Image.sPear);

		//reset();
	}

	public void reset() {
		image = getRandomFruit();
//		enable();
	}

	public void draw ( Canvas canvas ) {
		image.draw( canvas );
	}

	public void updateWith( int x, int y ) {
		image.setBounds( x, y, x + image.getMinimumWidth(), y + image.getMinimumHeight() );
	}

	private Drawable getRandomFruit() {
		Drawable fruit = fruits.get( NumUtil.getRandInt(0, 3) );

		int randomX = NumUtil.getRandInt( 0, Stage.getWidth() - fruit.getMinimumWidth() * 2 );
		int randomY = Double.valueOf( Stage.getHeight() * .0325 ).intValue();

		fruit.setBounds( randomX, randomY, randomX + fruit.getMinimumWidth(), randomY + fruit.getMinimumHeight() );
		fruit.setVisible( true, true );

		set( randomX, y, fruit.getMinimumWidth(), fruit.getMinimumHeight() );

		return fruit;
	}

//	public void disable() {
//		active = false;
//	}
//
//	public void enable() {
//		active = true;
//	}
}
