package com.example.helloandroid.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.helloandroid.BottomUpActivity;
import com.example.helloandroid.R;

public class ScrollSlideActivity extends Activity {
	private View mView;
	private MyScrollView mScrollView;
	private static final float zoomScale = 0.8f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		mView = LayoutInflater.from(this).inflate(R.layout.activity_scroll_slide, null);
		
		setContentView(mView);
		
		getActionBar().hide();
		
		LinearLayout llTab = (LinearLayout)findViewById(R.id.ll_tab);
		LinearLayout llTab1 = (LinearLayout)findViewById(R.id.ll_tab1);
		
		mScrollView = (MyScrollView)findViewById(R.id.scrollview);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		mScrollView.setTabStopPos(llTab, llTab1, displaymetrics.heightPixels);
		
		/*
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 
				WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);*/
	}
		

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Utility.logd("ONACTIVITY RESULT");
		super.onActivityResult(requestCode, resultCode, data);
		
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
		mView.startAnimation(animation);
	}

	
	public void goWebView(View view) {
		Intent i = new Intent(this, BottomUpActivity.class);
		startActivityForResult(i, 0);
		/*LayoutParams params = (LayoutParams)mView.getLayoutParams();
		params.leftMargin = 100; 
		params.rightMargin = 100;
		mView.requestLayout();*/
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
		animation.setFillAfter(true);
		
		mView.startAnimation(animation);
	}	
}
