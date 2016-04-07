package net.usrlib.android.gobirdie;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import net.usrlib.android.gobirdie.game.Surface;
import net.usrlib.android.gobirdie.game.World;

public class GameActivity extends Activity {
	private RelativeLayout.LayoutParams mLayoutParams;
	private Surface mSurface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		World.loadContentView(this);

		mSurface = new Surface(getApplicationContext());

		mLayoutParams = new RelativeLayout.LayoutParams (
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT
		);

		addContentView(mSurface, mLayoutParams);
	}
}
