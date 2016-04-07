package net.usrlib.android.gobirdie.game;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.usrlib.android.gobirdie.R;
import net.usrlib.android.gobirdie.actor.Bird;
import net.usrlib.android.gobirdie.actor.Birdhouse;
import net.usrlib.android.gobirdie.actor.Eagle;
import net.usrlib.android.gobirdie.actor.Fruit;
import net.usrlib.android.gobirdie.actor.Monkey;
import net.usrlib.android.gobirdie.actor.Snake;
import net.usrlib.android.gobirdie.event.GameEvent;
import net.usrlib.android.gobirdie.settings.Settings;
import net.usrlib.android.gobirdie.task.GameTask;
import net.usrlib.android.gobirdie.task.MediaPlayerTask;

import java.io.IOException;

public class World {
	public static final String FONT_TYPEWRITER = "font/typewriter.ttf";
	public static final String SOUND_TRACK  = "sound/silvadito_96kbps.mp3";
	public static final String SOUND_ALARM  = "sound/alarm.mp3";
	public static final String SOUND_TONE_1 = "sound/tone1.mp3";
	public static final String SOUND_TONE_2 = "sound/tone2.mp3";
	public static final String SOUND_PUNCH  = "sound/punch.mp3";
	public static final String SOUND_CHIRP  = "sound/chirp.mp3";
	public static final String SOUND_SLAP   = "sound/slap.mp3";

	//* ========== Game Objects ========== *//

	public static Bird sBird;
	public static Birdhouse sBirdhouse;
	public static Eagle sEagle;
	public static Fruit sFruit;
	public static Monkey sMonkey;
	public static Snake sSnake;

	public static final void loadActors(final Context context) {
		sBird = new Bird(context);
		sBirdhouse = new Birdhouse();
		sEagle = new Eagle(context);
		sFruit = new Fruit();
		sMonkey = new Monkey(context);
		sSnake = new Snake(context);
	}

	public static final void resetActors() {
		sBird.reset();
		sBirdhouse.reset();
		sEagle.reset();
		sFruit.reset();
		sMonkey.reset();
		sSnake.reset();
	}

	public static final void drawActors(final Canvas canvas) {
		sBird.draw(canvas);
		sEagle.draw(canvas);
		sFruit.draw(canvas);
		sMonkey.draw(canvas);
		sSnake.draw(canvas);
	}

	//* ============== Fonts ============= *//

	public static Typeface sTypewriter = null;

	public static final void loadFonts(Context context) {
		if (sTypewriter == null) {
			sTypewriter = Typeface.createFromAsset(context.getAssets(), FONT_TYPEWRITER);
			GameEvent.FontLoaded.notifySuccess();
		}
	}

	//* ============= Images ============= *//

	public static Drawable sCherry;
	public static Drawable sGrapes;
	public static Drawable sOrange;
	public static Drawable sPear;

	public static Bitmap sBirdRight;
	public static Bitmap sBirdLeft;
	public static Bitmap sBirdDown;

	public static final void loadImages(Context context) {
		final Resources resources = context.getResources();

		// Use getResources() to support API 10. context.getDrawable requires API 21!
		sCherry = resources.getDrawable(R.drawable.fruit_cherry);
		sGrapes = resources.getDrawable(R.drawable.fruit_grapes);
		sOrange = resources.getDrawable(R.drawable.fruit_orange);
		sPear = resources.getDrawable(R.drawable.fruit_pear);

		sBirdRight = BitmapFactory.decodeResource(resources, R.drawable.bird_right);
		sBirdLeft = BitmapFactory.decodeResource(resources, R.drawable.bird_left);
		sBirdDown = BitmapFactory.decodeResource(resources, R.drawable.bird_down);
	}

	//* ========== Music Player ========== *//

	private static AssetFileDescriptor sFileTrack;
	private static MediaPlayerTask sMediaPlayerTask;

	public static final void loadMusic(Context context) {
		try {

			final AssetManager assetManager = context.getAssets();
			sFileTrack = assetManager.openFd(SOUND_TRACK);
			sMediaPlayerTask = new MediaPlayerTask(sFileTrack);

			if (Settings.isMusicEnabled()) {
				startMediaPlayer();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final void startMediaPlayer() {
		if (sMediaPlayerTask == null) {
			return;
		}

		if ( !sMediaPlayerTask.isRunning() && Settings.isMusicEnabled() ) {
			new Thread( sMediaPlayerTask ).start();
		}
	}

	public static final void resumeMediaPlayer() {
		if (sMediaPlayerTask == null) {
			return;
		}

		if ( !sMediaPlayerTask.isRunning()  && Settings.isMusicEnabled() ) {
			startMediaPlayer();
		}

		if ( Settings.isMusicEnabled()) {
			sMediaPlayerTask.start();
		}
	}

	public static final void pauseMediaPlayer() {
		if (sMediaPlayerTask == null) {
			return;
		}

		sMediaPlayerTask.pause();
	}

	//* ============= Sound ============== *//

	private static final int MAX_STREAMS = 5;
	private static final int MAX_PLAY_COUNT = 5;

	private static AssetFileDescriptor sFileAlarm;
	private static AssetFileDescriptor sFileTone1;
	private static AssetFileDescriptor sFileTone2;
	private static AssetFileDescriptor sFilePunch;
	private static AssetFileDescriptor sFileChirp;
	private static AssetFileDescriptor sFileSlap;

	private static SoundPool sSoundPool;

	private static int sAlarmId;
	private static int sTone1Id;
	private static int sTone2Id;
	private static int sPunchId;
	private static int sChirpId;
	private static int sSlapId;
	private static int sSoundCount;

	private static boolean sIsReady;

	public static final void loadSounds(Context context) {
		try {

			final AssetManager assetManager = context.getAssets();

			sFileAlarm = assetManager.openFd(SOUND_ALARM);
			sFileTone1 = assetManager.openFd(SOUND_TONE_1);
			sFileTone2 = assetManager.openFd(SOUND_TONE_2);
			sFilePunch = assetManager.openFd(SOUND_PUNCH);
			sFileChirp = assetManager.openFd(SOUND_CHIRP);
			sFileSlap  = assetManager.openFd(SOUND_SLAP);

			// Use SoundPool to support API's < 23 as required by SoundPool.Builder
			sSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);

			sSoundPool.setOnLoadCompleteListener(
					new SoundPool.OnLoadCompleteListener() {
						public void onLoadComplete( SoundPool soundPool, int sampleId, int status ) {
							sIsReady = true;
							GameEvent.SoundLoaded.notifySuccess();
						}
					}
			);

			sAlarmId = sSoundPool.load(sFileAlarm, 1);
			sTone1Id = sSoundPool.load(sFileTone1, 1);
			sTone2Id = sSoundPool.load(sFileTone2, 1);
			sPunchId = sSoundPool.load(sFilePunch, 1);
			sChirpId = sSoundPool.load(sFileChirp, 1);
			sSlapId  = sSoundPool.load(sFileSlap, 1);

		} catch (IOException e) {

			sIsReady = false;
			GameEvent.SoundLoaded.notifyError(e.getMessage());
			e.printStackTrace();

		}
	}

	public static final void playAlarm() {
		sSoundCount++;

		// Avoid jittery playback
		if ( sSoundCount >= MAX_PLAY_COUNT ) {
			playSound(sAlarmId);
			sSoundCount = 0;
		}
	}

	public static final void playTone1() {
		playSound(sTone1Id);
	}

	public static final void playTone2() {
		playSound(sTone2Id);
	}

	public static final void playPunch() {
		playSound(sPunchId);
	}

	public static final void playChirp() {
		playSound(sChirpId);
	}

	public static final void playSlap() {
		sSoundCount++;

		// Avoid jittery playback
		if ( sSoundCount >= MAX_PLAY_COUNT ) {
			playSound( sSlapId );
			sSoundCount = 0;
		}
	}

	public static final void releaseSoundPool() {
		if (sSoundPool == null) {
			return;
		}

		sSoundPool.release();
		sSoundPool = null;
		sIsReady = false;
		sSoundCount = 0;
	}

	private static final void playSound( final int soundId ) {
		if (sSoundPool == null || !sIsReady || !Settings.isSoundEnabled()) {
			return;
		}

		new Thread(
				new Runnable(){
					public void run() {
						sSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
					}
				}
		).start();
	}

	//* ============= Stage ============== *//

	public static Paint sPaint = new Paint();

	private static Activity sActivity;
	private static View sGameLayout;
	private static TextView sScoreView;
	private static GameTask sGameTask;

	private static int sScore = 0;
	private static int sWidth;
	private static int sHeight;

	public static final void loadContentView(Activity activity) {
		sGameLayout = (View) activity.findViewById(R.id.activity_game_layout);
		sScoreView = (TextView) activity.findViewById(R.id.score_textview);
		sScoreView.setText(String.valueOf(sScore));
		sScoreView.setTypeface(sTypewriter);

		sActivity = activity;
	}

	public static final void initWithSurfaceView(final Surface view) {
		sScore = 0;
		sWidth = view.getWidth();
		sHeight = view.getHeight();

		sPaint.setDither(true);
		sPaint.setFilterBitmap(true);

		sGameTask = new GameTask(view);

		Log.i("STAGE", "initWithSurfaceView " + String.valueOf(getWidth()) + "x" + String.valueOf(getHeight()));
	}

	public static final int getWidth() {
		return sWidth;
	}

	public static final int getHeight() {
		return sHeight;
	}

	public static final void setBackgroundDayTime() {
		setGameBackground(R.drawable.bg_game_1);
	}

	public static final void setBackgroundNightTime() {
		setGameBackground(R.drawable.bg_game_2);
	}

	public static final void setGameBackground(final int id) {
		sActivity.runOnUiThread(
				new Runnable() {
					public void run() {
						sGameLayout.setBackgroundResource(id);
					}
				}
		);
	}

	public static final void startGameLoop() {
		if (sGameTask == null || sGameTask.isRunning()) {
			return;
		}

		sGameTask.init();
		new Thread(sGameTask).start();
	}

	public static final void stopGameLoop() {
		sGameTask.stopTask();
	}

	public static final void updateScore() {
		sScore++;
		sActivity.runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						sScoreView.setText(String.valueOf(sScore));
					}
				}
		);
	}
}
