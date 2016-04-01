package net.usrlib.android.gobirdie.object;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class AnimationFrames {
	private List<Bitmap> mFrames = new ArrayList<Bitmap>();

	private int mFrameRate = 10;
	private int mFrameCount = 0;
	private int mCurrentFrame = 0;

	public AnimationFrames() {}

	public void setFrameRate(final int frameRate) {
		mFrameRate = frameRate;
	}

	public void add(final Bitmap bitmap) {
		mFrames.add(bitmap);
	}

	public Bitmap getTopFrame() {
		return mFrames.get(0);
	}

	public Bitmap getCurrentFrame() {
		mFrameCount++;
	
		if (mFrameCount > mFrameRate) {
			mFrameCount = 0;
			mCurrentFrame++;

			if (mCurrentFrame > mFrames.size() - 1) {
				mCurrentFrame = 0;
			}
		}

		return mFrames.get(mCurrentFrame);
	}

}
