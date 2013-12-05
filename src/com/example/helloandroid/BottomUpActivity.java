package com.example.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

public class BottomUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bottomup);
		overridePendingTransition(R.anim.slide_up, R.anim.zoom_out);
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(0, R.anim.slide_down);
	}
	
}
