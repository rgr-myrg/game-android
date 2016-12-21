package net.usrlib.gobirdie.swipeview;

/**
 * Created by rgr-myrg on 12/21/16.
 */

public class SceneInfo {
	private int mSceneDrawableId;
	private int mSceneTextId;

	public SceneInfo(int sceneDrawableId, int sceneTextId) {
		this.mSceneDrawableId = sceneDrawableId;
		this.mSceneTextId = sceneTextId;
	}

	public int getSceneTextId() {
		return mSceneTextId;
	}

	public int getSceneDrawableId() {
		return mSceneDrawableId;
	}
}
