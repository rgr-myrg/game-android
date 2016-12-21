package net.usrlib.gobirdie.util;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by rgr-myrg on 12/20/16.
 */

public class ScreenMetrics {
	public static final Point getDisplaySize(final WindowManager windowManager) {
		try {
			if (Build.VERSION.SDK_INT > 16) {
				final Display display = windowManager.getDefaultDisplay();
				final DisplayMetrics displayMetrics = new DisplayMetrics();

				display.getMetrics(displayMetrics);

				return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
			} else {
				return new Point(0, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Point(0, 0);
		}
	}

	public static final int dpToPx(final int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}
}
