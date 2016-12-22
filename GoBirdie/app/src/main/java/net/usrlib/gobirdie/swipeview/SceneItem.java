package net.usrlib.gobirdie.swipeview;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import net.usrlib.gobirdie.R;

/**
 * Created by rgr-myrg on 12/21/16.
 */

@Layout(R.layout.swipe_card_view)
public class SceneItem {
	@View(R.id.sceneImageView)
	private ImageView mImageView;

	@View(R.id.sceneTextView)
	private TextView mTextView;

	private Context mContext;
	private SceneInfo mSceneInfo;
	private SwipePlaceHolderView mSwipeView;

	public SceneItem(Context mContext, SceneInfo mSceneInfo, SwipePlaceHolderView mSwipeView) {
		this.mContext   = mContext;
		this.mSceneInfo = mSceneInfo;
		this.mSwipeView = mSwipeView;
	}

	@Resolve
	private void onResolved() {
		Glide.with(mContext)
				.load(mSceneInfo.getSceneDrawableId())
				.into(mImageView);

		mTextView.setText(
				mContext.getString(mSceneInfo.getSceneTextId())
		);
	}

	@Click(R.id.sceneImageView)
	private void onClick() {
	}

	@SwipeOut
	private void onSwipedOut() {
		// Uncomment to re-add this item to the swipe view.
		// mSwipeView.addView(this);
	}

	@SwipeCancelState
	private void onSwipeCancelState() {
	}

	@SwipeIn
	private void onSwipeIn() {
	}

	@SwipeInState
	private void onSwipeInState() {
	}

	@SwipeOutState
	private void onSwipeOutState() {
	}
}
