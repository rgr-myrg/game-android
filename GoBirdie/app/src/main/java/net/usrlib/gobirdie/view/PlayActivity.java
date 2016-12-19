package net.usrlib.gobirdie.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import net.usrlib.gobirdie.R;
import net.usrlib.gobirdie.game.Surface;
import net.usrlib.gobirdie.game.World;

/**
 * Created by rgr-myrg on 12/18/16.
 */

public class PlayActivity extends Activity {
	private RelativeLayout.LayoutParams mLayoutParams;
	private Surface mSurface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_activity);

		World.loadContentView(this);

		mSurface = new Surface(getApplicationContext());

		mLayoutParams = new RelativeLayout.LayoutParams (
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT
		);

		addContentView(mSurface, mLayoutParams);
	}
}
