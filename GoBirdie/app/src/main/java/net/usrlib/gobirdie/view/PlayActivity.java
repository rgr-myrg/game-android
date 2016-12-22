package net.usrlib.gobirdie.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import net.usrlib.gobirdie.R;
import net.usrlib.gobirdie.game.Surface;
import net.usrlib.gobirdie.game.World;
import net.usrlib.gobirdie.util.AdRequestUtil;
import net.usrlib.gobirdie.util.Preferences;

/**
 * Created by rgr-myrg on 12/18/16.
 */

public class PlayActivity extends Activity implements Surface.SurfaceCallback {
	public static final String TAG = PlayActivity.class.getSimpleName();

	private RelativeLayout.LayoutParams mLayoutParams;
	private Surface mSurface;
	private CardView mScoreCardView;

	private TextView mTotalScoreView;
	private TextView mHighScoreView;
	private TextView mLifeScoreView;
	private TextView mScoreUpdateView;

	private Button mContinueButton;

	private int mContinueInterval = 2000;
	private Handler mHandler = new Handler();

	private AdView mAdView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_activity);

		mSurface = new Surface(getApplicationContext(), this);

		mLayoutParams = new RelativeLayout.LayoutParams (
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT
		);

		addContentView(mSurface, mLayoutParams);
		loadViews();
	}

	// Surface Callbacks

	@Override
	public void onSurfaceCreated() {
		Log.d(TAG, "onSurfaceCreated");
	}

	@Override
	public void onSurfaceDestroyed() {
		Log.d(TAG, "onSurfaceDestroyed");
	}

	@Override
	public void onScoreUpdate(final int score) {
		if (mScoreUpdateView != null) {
			runOnUiThread(() -> {
				mScoreUpdateView.setText(String.valueOf(score));
			});
		}
	}

	@Override
	public void onGameOver(final int score) {
		runOnUiThread(() -> {
			AdRequestUtil.makeAdRequest(mAdView);
			formatAndDisplayScore(score);
		});
	}

	@Override
	public void onReadyToContinue() {
		Log.d(TAG, "onReadyToContinue");

		mSurface.onSurfaceDestroyed();
		mSurface.setVisibility(View.GONE);

		startActivity(new Intent(this, HomeActivity.class));
	}

	private void loadViews() {
		mScoreCardView   = (CardView) findViewById(R.id.score_cardview);
		mTotalScoreView  = (TextView) findViewById(R.id.total_score_textview);
		mHighScoreView   = (TextView) findViewById(R.id.high_score_textview);
		mLifeScoreView   = (TextView) findViewById(R.id.life_score_textview);
		mScoreUpdateView = (TextView) findViewById(R.id.score_textview);
		mContinueButton  = (Button) findViewById(R.id.btnContinue);

		if (World.sTypewriter != null) {
			mContinueButton.setTypeface(World.sTypewriter);
		}

		mAdView = (AdView) findViewById(R.id.adView);
	}

	private void formatAndDisplayScore(final int score) {
		mScoreCardView.setVisibility(View.VISIBLE);

		final String totalScore = String.format(
				World.SCORE_FORMAT,
				getString(R.string.score_label),
				score
		);

		final String bestScore = String.format(
				World.SCORE_FORMAT,
				getString(R.string.score_best),
				Preferences.getHighScore(getApplicationContext())
		);

		final String lifeScore = String.format(
				World.SCORE_FORMAT,
				getString(R.string.score_lifetime),
				Preferences.getLifeScore(getApplicationContext())
		);

		mTotalScoreView.setText(totalScore);
		mHighScoreView.setText(bestScore);
		mLifeScoreView.setText(lifeScore);

		mHandler.postDelayed(() -> {
			mContinueButton.setVisibility(android.view.View.VISIBLE);
			mSurface.playerReadyToContinue();
		}, mContinueInterval);
	}
}

//	public static final void setBackgroundDayTime() {
//		setGameBackground(R.drawable.bg_game_1);
//	}
//
//	public static final void setBackgroundNightTime() {
//		setGameBackground(R.drawable.bg_game_2);
//	}
//
//	public static final void setGameBackground(final int id) {
//		sActivity.runOnUiThread(() -> {
//sGameLayout = (View) activity.findViewById(R.id.activity_game_layout);
//			sGameLayout.setBackgroundResource(id);
//		});
//	}