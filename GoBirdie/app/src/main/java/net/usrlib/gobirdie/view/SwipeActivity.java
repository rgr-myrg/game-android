package net.usrlib.gobirdie.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
	private TextView mSwipeArrow = null;
	private TextView mSkipIntro  = null;

	private Handler mHandler = new Handler();

	private int mHandlerInterval = 1000;
	private int mMaxHandlerCount = 6;
	private int mHandlerCount = 0;

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
		loadGameAssets();
		showSkipIntroText();
		showSwipeArrow(true);
	}

	protected void initSwipeView() {
		final int bottomMargin = ScreenMetrics.dpToPx(160);
		final Point windowSize = ScreenMetrics.getDisplaySize(getWindowManager());

		mSwipeArrow = (TextView) findViewById(R.id.swipe_arrow);
		mSkipIntro  = (TextView) findViewById(R.id.skip_intro);
		mSwipeView  = (SwipePlaceHolderView) findViewById(R.id.swipe_holder_view);

		mSwipeView.getBuilder()
				.setDisplayViewCount(3)
				.setHeightSwipeDistFactor(10)
				.setWidthSwipeDistFactor(5)
				.setSwipeType(SwipePlaceHolderView.SWIPE_TYPE_DEFAULT)
				.setSwipeDecor(new SwipeDecor()
						.setViewWidth(windowSize.x)
						.setViewHeight(windowSize.y - bottomMargin)
						.setViewGravity(Gravity.TOP)
						//.setPaddingTop(20)
						.setRelativeScale(0.01f)
				);

		for (SceneInfo sceneInfo : mSceneInfo) {
			mSwipeView.addView(
					new SceneItem(getApplicationContext(), sceneInfo, mSwipeView)
			);
		}
	}

	protected void loadGameAssets() {
		final Context context = getApplicationContext();

		new Thread(() -> {
			World.loadFonts(context, typeface -> {
				runOnUiThread(() -> {
					((Button) findViewById(R.id.btnStart)).setTypeface(typeface);
					mSkipIntro.setTypeface(typeface);
				});
			});

			World.loadMusic(context);
		}).start();
	}

	protected void showSwipeArrow(final boolean isVisible) {
		mHandler.postDelayed(() -> {
			if (mHandlerCount < mMaxHandlerCount) {
				mSwipeArrow.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
				mHandlerCount++;
				showSwipeArrow(!isVisible);
			}
		}, mHandlerInterval);
	}

	protected void showSkipIntroText() {
		mHandler.postDelayed(() -> {
			mSkipIntro.setVisibility(View.VISIBLE);
		}, 5000);
	}

	public void startHomeActivity(View view) {
		startActivity(new Intent(this, HomeActivity.class));
	}
}