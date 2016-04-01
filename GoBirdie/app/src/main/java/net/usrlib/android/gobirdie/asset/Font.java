package net.usrlib.android.gobirdie.asset;

import android.content.Context;
import android.graphics.Typeface;

import net.usrlib.android.gobirdie.event.LoadEvent;

public final class Font {

	public static Typeface sTypewriter = null;

	public static final void loadFonts(Context context) {
		if (sTypewriter == null) {
			sTypewriter = Typeface.createFromAsset(context.getAssets(), Path.FONT_TYPEWRITER);
			LoadEvent.FontLoaded.notifySuccess();
		}
	}

}
