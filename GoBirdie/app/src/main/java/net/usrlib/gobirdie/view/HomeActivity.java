package net.usrlib.gobirdie.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdView;

import net.usrlib.gobirdie.R;
import net.usrlib.gobirdie.game.World;
import net.usrlib.gobirdie.util.AdRequestUtil;
import net.usrlib.gobirdie.util.Preferences;

/**
 * Created by rgr-myrg on 12/18/16.
 */

public class HomeActivity extends AppCompatActivity {
	private Button mMusicButton = null;
	private Button mSoundButton = null;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);

		AdRequestUtil.makeAdRequest((AdView) findViewById(R.id.adView));

		mMusicButton = (Button) findViewById(R.id.btnMusicSettings);
		mSoundButton = (Button) findViewById(R.id.btnSoundSettings);

		setMusicButtonText(Preferences.isMusicEnabled(getApplicationContext()));
		setSoundButtonText(Preferences.isSoundEnabled(getApplicationContext()));

		loadGameAssets();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void loadGameAssets() {
		final Context context = getApplicationContext();

		new Thread(() -> {
			World.loadFonts(context, typeface -> {
				runOnUiThread(() -> {
					setButtonsTypeface(typeface);
				});
			});

			World.loadImages(context);
			World.loadSounds(context);
			World.loadMusic(context);
			World.loadActors(context);
		}).start();
	}

	private void setButtonsTypeface(final Typeface typeface) {
		((Button) findViewById(R.id.btnPlay)).setTypeface(typeface);
		((Button) findViewById(R.id.btnAbout)).setTypeface(typeface);

		mMusicButton.setTypeface(typeface);
		mSoundButton.setTypeface(typeface);
	}

	private void setMusicButtonText(final boolean isMusicEnabled) {
		if (isMusicEnabled) {
			World.resumeMediaPlayer(getApplicationContext());
		} else {
			World.pauseMediaPlayer();
		}

		mMusicButton.setText(
				isMusicEnabled ? "Music ON" : "Music OFF"
		);
	}

	private void setSoundButtonText(final boolean isSoundEnabled) {
		mSoundButton.setText(
				isSoundEnabled ? "Sound ON" : "Sound OFF"
		);
	}

	public void startPlayActivity(View view) {
		World.playTone1(getApplicationContext());
		startActivity(new Intent(this, PlayActivity.class));
	}

	public void startAboutActivity(View view) {
		World.playChirp(getApplicationContext());
	}

	public void toggleMusicSettings(final View view) {
		final boolean isMusicEnabled = Preferences.isMusicEnabled(getApplicationContext());

		Preferences.enableMusic(getApplicationContext(), !isMusicEnabled);
		setMusicButtonText(!isMusicEnabled);
	}

	public void toggleSoundSettings(View view) {
		final boolean isSoundEnabled = Preferences.isSoundEnabled(getApplicationContext());

		Preferences.enableSound(getApplicationContext(), !isSoundEnabled);
		setSoundButtonText(!isSoundEnabled);
	}
}
