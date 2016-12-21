package net.usrlib.gobirdie.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdView;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import net.usrlib.gobirdie.R;
import net.usrlib.gobirdie.game.World;
import net.usrlib.gobirdie.swipeview.SceneInfo;
import net.usrlib.gobirdie.swipeview.SceneItem;
import net.usrlib.gobirdie.util.AdRequestUtil;
import net.usrlib.gobirdie.util.ScreenMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rgr-myrg on 12/21/16.
 */

public class SwipeActivity extends AppCompatActivity {
	private SwipePlaceHolderView mSwipeView = null;

	private List<SceneInfo> mSceneInfo = new ArrayList<>(Arrays.asList(
			new SceneInfo(R.drawable.scene_1, R.string.scene1Text),
			new SceneInfo(R.drawable.scene_2, R.string.scene2Text),
			new SceneInfo(R.drawable.scene_3, R.string.scene3Text),
			new SceneInfo(R.drawable.scene_4, R.string.scene4Text),
			new SceneInfo(R.drawable.scene_5, R.string.scene5Text),
			new SceneInfo(R.drawable.scene_6, R.string.scene6Text),
			new SceneInfo(R.drawable.scene_7, R.string.scene7Text),
			new SceneInfo(R.drawable.scene_8, R.string.scene8Text)
	));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swipe_activity);

		AdRequestUtil.makeAdRequest((AdView) findViewById(R.id.adView));

		initSwipeView();
	}

	protected void initSwipeView() {
		final int bottomMargin = ScreenMetrics.dpToPx(160);
		final Point windowSize = ScreenMetrics.getDisplaySize(getWindowManager());

		mSwipeView = (SwipePlaceHolderView)findViewById(R.id.swipe_holder_view);
		mSwipeView.getBuilder()
				.setDisplayViewCount(3)
				.setHeightSwipeDistFactor(10)
				.setWidthSwipeDistFactor(5)
				.setSwipeType(SwipePlaceHolderView.SWIPE_TYPE_DEFAULT)
				.setSwipeDecor(new SwipeDecor()
						.setViewWidth(windowSize.x)
						.setViewHeight(windowSize.y - bottomMargin)
						.setViewGravity(Gravity.TOP)
						.setPaddingTop(20)
						.setRelativeScale(0.01f)
				);

		for (SceneInfo sceneInfo : mSceneInfo) {
			mSwipeView.addView(
					new SceneItem(getApplicationContext(), sceneInfo, mSwipeView)
			);
		}

		loadGameAssets();
	}

	protected void loadGameAssets() {
		final Context context = getApplicationContext();

		new Thread(() -> {
			World.loadFonts(context, typeface -> {
				runOnUiThread(() -> {
					((Button) findViewById(R.id.btnStart)).setTypeface(typeface);
				});
			});

			World.loadImages(context);
			World.loadSounds(context);
			World.loadMusic(context);
			World.loadActors(context);
		}).start();
	}

	public void startHomeActivity(View view) {
		startActivity(new Intent(this, HomeActivity.class));
	}
}