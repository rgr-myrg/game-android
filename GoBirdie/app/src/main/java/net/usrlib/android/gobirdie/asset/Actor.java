package net.usrlib.android.gobirdie.asset;

import android.content.Context;

import net.usrlib.android.gobirdie.actor.Bird;
import net.usrlib.android.gobirdie.actor.Birdhouse;
import net.usrlib.android.gobirdie.actor.Eagle;
import net.usrlib.android.gobirdie.actor.Fruit;
import net.usrlib.android.gobirdie.actor.Monkey;
import net.usrlib.android.gobirdie.actor.Snake;

public final class Actor {
	public static Bird sBird;
	public static Birdhouse sBirdhouse;
	public static Eagle sEagle;
	public static Fruit sFruit;
	public static Monkey sMonkey;
	public static Snake sSnake;

	public static final void loadActors(Context context) {
		sBird = new Bird(context);
		sBirdhouse = new Birdhouse();
		sEagle = new Eagle(context);
		sFruit = new Fruit();
		sMonkey = new Monkey(context);
		sSnake = new Snake(context);
	}

}
