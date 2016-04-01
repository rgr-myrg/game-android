package net.usrlib.android.gobirdie.gameview;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public interface IGameView {

	public void onCanvasDraw(Canvas canvas);
	public void onUpdate();
	public void onSurfaceCreated();
	public void onSurfaceDestroyed();
	public void onStop();
	public void onResume();
	public SurfaceHolder getHolder();

}
