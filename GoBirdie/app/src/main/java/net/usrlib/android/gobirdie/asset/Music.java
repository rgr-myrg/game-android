package net.usrlib.android.gobirdie.asset;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import net.usrlib.android.gobirdie.settings.Settings;
import net.usrlib.android.gobirdie.task.MediaPlayerTask;

import java.io.IOException;

public final class Music {

	private static AssetFileDescriptor sFileTrack;
	private static MediaPlayerTask sMediaPlayerTask;

	public static final void loadMusic(Context context) {
		try {

			final AssetManager assetManager = context.getAssets();
			sFileTrack = assetManager.openFd(Path.SOUND_TRACK);
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

}
