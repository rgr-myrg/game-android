package net.usrlib.android.gobirdie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import net.usrlib.android.gobirdie.asset.Actor;
import net.usrlib.android.gobirdie.asset.Font;
import net.usrlib.android.gobirdie.asset.Image;
import net.usrlib.android.gobirdie.asset.Music;
import net.usrlib.android.gobirdie.asset.Sound;
import net.usrlib.android.gobirdie.event.LoadEvent;
import net.usrlib.android.gobirdie.settings.Settings;
import net.usrlib.pattern.TinyEvent;

public class MainActivity extends AppCompatActivity {

	private Button mMusicButton = null;
	private Button mSoundButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Init Settings
		Settings.initWithContext(getApplicationContext());

		// Cache buttons for setting text dynamically
		mMusicButton = (Button) findViewById(R.id.btnMusicSettings);
		mSoundButton = (Button) findViewById(R.id.btnSoundSettings);

		toggleMusicBtnText(Settings.isMusicEnabled());
		toggleSoundBtnText(Settings.isSoundEnabled());

		addListeners();
		loadAssets();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	private void addListeners() {
		LoadEvent.FontLoaded.addListenerOnce(
				new TinyEvent.Listener() {
					@Override
					public void onSuccess(Object data) {
						setButtonsTypeface();
					}
				}
		);
	}

	private void loadAssets() {
		Image.loadImages(getApplicationContext());
		Font.loadFonts(getApplicationContext());
		Sound.loadSounds(getApplicationContext());
		Music.loadMusic(getApplicationContext());
		Actor.loadActors(getApplicationContext());
	}

	// Invoked when LoadEvent.FontLoaded.notifySuccess is triggered
	private void setButtonsTypeface() {
		((Button) findViewById(R.id.btnPlay)).setTypeface(Font.sTypewriter);
		((Button) findViewById(R.id.btnAbout)).setTypeface(Font.sTypewriter);

		mMusicButton.setTypeface(Font.sTypewriter);
		mSoundButton.setTypeface(Font.sTypewriter);
	}

	public void startGameActivity(View view) {
		Log.d("MAIN", "startGameActivity");
		Sound.playTone1();
	}

	public void startAboutActivity(View view) {
		Log.d("MAIN", "startOptionsActivity");
		Sound.playChirp();
	}

	public void toggleMusicSettings(final View view) {
		final boolean isMusicEnabled = Settings.isMusicEnabled();

		Settings.enableMusic(!isMusicEnabled);
		toggleMusicBtnText(!isMusicEnabled);

		Log.d("MAIN", "toggleMusicSettings: " + String.valueOf(Settings.isMusicEnabled()));
	}

	public void toggleSoundSettings(View view) {
		final boolean isSoundEnabled = Settings.isSoundEnabled();

		Settings.enableSound(!isSoundEnabled);
		toggleSoundBtnText(!isSoundEnabled);
	}

	private void toggleMusicBtnText(final boolean flag) {
		if (flag) {
			Music.resumeMediaPlayer();
		} else {
			Music.pauseMediaPlayer();
		}

		mMusicButton.setText(
				flag ? "Music ON" : "Music OFF"
		);
	}

	private void toggleSoundBtnText(final boolean flag) {
		mSoundButton.setText(
				flag ? "Sound ON" : "Sound OFF"
		);
	}

}
