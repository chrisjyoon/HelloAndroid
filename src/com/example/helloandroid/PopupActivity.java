package com.example.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PopupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_popup);
		
		int width;
		int height;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		width = displaymetrics.widthPixels - 10;
		height = displaymetrics.heightPixels - 10;
		getWindow().setLayout(width, height);
		
		loadWebView();
	}
	
	public void loadWebView() {
		/*WebView wv = (WebView)findViewById(R.id.webview);
		wv.setWebViewClient(new myWebViewClient());
		wv.loadUrl("http://www.google.com");*/
	}
	
	private class myWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d("EPUTMS", "url = " + url);
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			//mPb.setVisibility(View.INVISIBLE);
		}
		
	}
}
