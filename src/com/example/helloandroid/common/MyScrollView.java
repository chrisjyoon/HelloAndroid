package com.example.helloandroid.common;

import com.example.helloandroid.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	Rect mRect;
	LinearLayout mLlTab;
	LinearLayout mLlTab1;
	int mHeight;
	
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRect = new Rect();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		getLocalVisibleRect(mRect);
		
		Utility.logd("mRect = " + mRect);
		
		int[] location = new int[2];
		mLlTab1.getLocationOnScreen(location);
		
		Utility.logd("111 mRect = " + location[1]);
		
		if ((mHeight - mRect.height()) >= location[1]) {
			mLlTab.setVisibility(View.VISIBLE);
		} else {
			mLlTab.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setTabStopPos(LinearLayout llTab, LinearLayout llTab1, int windowHeight) {
		mLlTab = llTab;
		mLlTab1 = llTab1;
		mHeight = windowHeight;

		LayoutInflater li = (LayoutInflater)(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		li.inflate(R.layout.top_tab, mLlTab, true);
	}

}
