package net.usrlib.android.gobirdie.task;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class MediaPlayerTask implements Runnable {

	public static final String NAME = MediaPlayerTask.class.getSimpleName();

	private MediaPlayer mMediaPlayer = null;
	private AssetFileDescriptor mFileDescriptor = null;

	private boolean mIsPlayerReady;
	private boolean mIsPlaying;
	private boolean mIsRunning;

	public MediaPlayerTask(final AssetFileDescriptor fileDescriptor) {
		Log.d(NAME, "Constructor");
		mMediaPlayer = new MediaPlayer();
		mFileDescriptor = fileDescriptor;
	}

	public void start() {
		Log.d(NAME, "start()");
		if (mMediaPlayer != null && mIsPlayerReady && !mIsPlaying) {
			Log.d(NAME, "invoking mMediaPlayer.start");
			mIsPlaying = true;
			mMediaPlayer.start();
		}
	}

	public void stop() {
		if (mIsPlayerReady && mIsPlaying) {
			mIsPlayerReady = false;
			mIsPlaying = false;

			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
			}
		}
	}

	public void pause() {
		if (mIsPlayerReady && mIsPlaying) {
			mIsPlaying = false;

			if (mMediaPlayer != null) {
				mMediaPlayer.pause();
			}
		}
	}

	public boolean isRunning() {
		return mIsRunning;
	}

	@Override
	public void run() {
		Log.d(NAME, "run()");
		if (mIsPlayerReady || mFileDescriptor == null) {
			return;
		}

		try {
			mIsRunning = true;
			Log.d(NAME, "mMediaPlayer.setDataSource()");
			mMediaPlayer.setDataSource(
					mFileDescriptor.getFileDescriptor(),
					mFileDescriptor.getStartOffset(),
					mFileDescriptor.getLength()
			);

			mMediaPlayer.setOnPreparedListener(
					new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mp) {
							mIsPlayerReady = true;
							Log.d(NAME, "mMediaPlayer.onPrepared()");
							start();
						}
					}
			);

			mMediaPlayer.setOnErrorListener(
					new MediaPlayer.OnErrorListener() {
						@Override
						public boolean onError(MediaPlayer mp, int id, int extra) {
							Log.d(NAME, "mMediaPlayer.onError()");
							stop();
							return false;
						}
					}
			);

			mMediaPlayer.setLooping(true);
			mMediaPlayer.prepareAsync();

			mIsRunning = false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
