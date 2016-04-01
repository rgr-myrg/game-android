package net.usrlib.android.gobirdie.asset;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import net.usrlib.android.gobirdie.event.LoadEvent;
import net.usrlib.android.gobirdie.settings.Settings;

import java.io.IOException;

public final class Sound {

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

			sFileAlarm = assetManager.openFd(Path.SOUND_ALARM);
			sFileTone1 = assetManager.openFd(Path.SOUND_TONE_1);
			sFileTone2 = assetManager.openFd(Path.SOUND_TONE_2);
			sFilePunch = assetManager.openFd(Path.SOUND_PUNCH);
			sFileChirp = assetManager.openFd(Path.SOUND_CHIRP);
			sFileSlap  = assetManager.openFd(Path.SOUND_SLAP);

			// Use SoundPool to support API's < 23 as required by SoundPool.Builder
			sSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);

			sSoundPool.setOnLoadCompleteListener(
					new SoundPool.OnLoadCompleteListener() {
						public void onLoadComplete( SoundPool soundPool, int sampleId, int status ) {
							sIsReady = true;
							LoadEvent.SoundLoaded.notifySuccess();
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
			LoadEvent.SoundLoaded.notifyError(e.getMessage());
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

}
