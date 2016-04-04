package net.usrlib.android.gobirdie.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public final class Settings {
	private static final String sMusicKey = "music";
	private static final String sSoundKey = "sound";
	private static final String sHighScoreKey = "highscore";
	private static final String sLifeScoreKey = "lifescore";

	private static SharedPreferences sPreferences = null;

	public static final void initWithContext(Context context) {
		if (sPreferences == null) {
			sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		}
	}

	public static final void enableMusic(final boolean flag) {
		setBoolean(sMusicKey, flag);
	}

	public static final void enableSound(final boolean flag) {
		setBoolean(sSoundKey, flag);
	}

	public static final void setScore(final int newScore) {
		final int highscore = sPreferences.getInt(sHighScoreKey, 0);
		final SharedPreferences.Editor editor = sPreferences.edit();

		if (newScore > highscore) {
			setInt(sHighScoreKey, newScore);
		}

		setInt(sLifeScoreKey, getLifeScore() + newScore);
	}

	public static final boolean isMusicEnabled() {
		Log.d("Settings", "isMusicEnabled: " + String.valueOf(sPreferences.getBoolean(sMusicKey, false)));
		return sPreferences.getBoolean(sMusicKey, false);
	}

	public static final boolean isSoundEnabled() {
		return sPreferences.getBoolean(sSoundKey, false);
	}

	public static final int getHighScore() {
		return sPreferences.getInt(sHighScoreKey, 0);
	}

	public static final int getLifeScore() {
		return sPreferences.getInt(sLifeScoreKey, 0);
	}

//	private static final void setString(final String key, final String value) {
//		final Editor editor = sPreferences.edit();
//		editor.putString(key, value);
//		editor.commit();
//	}

	private static final void setBoolean(final String key, final boolean value) {
		Log.d("Settings", "setBoolean: " + key + "=>" + String.valueOf(value));
		final Editor editor = sPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

//	private static final String getString(final String key) {
//		return sPreferences.getString(key, null);
//	}
//
	private static final void setInt(final String key, final int value) {
		final Editor editor = sPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
//
//	private static final int getInt(Activity activity, String key) {
//		return sPreferences.getInt(key, 0);
//	}
}
