package com.example.helloandroid.common;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.helloandroid.R;

public class WebViewFragment extends Fragment {
	private View mView;
	private ProgressBar mPb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_webview, container, false);
		mPb = (ProgressBar)mView.findViewById(R.id.progress);
		loadWebView();
		return mView;
	}



	public void loadWebView() {
		WebView wv = (WebView)mView.findViewById(R.id.webview);
		wv.setWebViewClient(new myWebViewClient());
		wv.loadUrl("http://www.google.com");
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
			mPb.setVisibility(View.INVISIBLE);
		}
		
	}
}
