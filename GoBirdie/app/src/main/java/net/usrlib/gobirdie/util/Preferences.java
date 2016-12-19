package net.usrlib.gobirdie.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rgr-myrg on 12/18/16.
 */

public class Preferences {
	private static final String sMusicKey = "music";
	private static final String sSoundKey = "sound";
	private static final String sHighScoreKey = "highscore";
	private static final String sLifeScoreKey = "lifescore";

	public static final void enableMusic(final Context context, final boolean flag) {
		setBoolean(context, sMusicKey, flag);
	}

	public static final void enableSound(final Context context, final boolean flag) {
		setBoolean(context, sSoundKey, flag);
	}

	public static final boolean isMusicEnabled(final Context context) {
		return getBoolean(context, sMusicKey);
	}

	public static final boolean isSoundEnabled(final Context context) {
		return getBoolean(context, sSoundKey);
	}

	public static final void setScore(final Context context, final int newScore) {
		final int highscore = getInt(context, sHighScoreKey);

		if (newScore > highscore) {
			setInt(context, sHighScoreKey, newScore);
		}

		setInt(context, sLifeScoreKey, getLifeScore(context) + newScore);
	}

	public static final int getHighScore(final Context context) {
		return getInt(context, sHighScoreKey);
	}

	public static final int getLifeScore(final Context context) {
		return getInt(context, sLifeScoreKey);
	}

	public static final void setBoolean(final Context context,
										final String key,
										final boolean value) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		final SharedPreferences.Editor editor = preferences.edit();

		editor.putBoolean(key, value);
		editor.commit();
	}

	public static final boolean getBoolean(final Context context, final String key) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean result = false;

		if (preferences != null) {
			result = preferences.getBoolean(key, result);
		}

		return result;
	}

	public static final void setInt(final Context context,
									final String key,
									final int value) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		final SharedPreferences.Editor editor = preferences.edit();

		editor.putInt(key, value);
		editor.commit();
	}

	public static final int getInt(final Context context, final String key) {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		int result = 0;

		if (preferences != null) {
			result = preferences.getInt(key, result);
		}

		return result;
	}
}
