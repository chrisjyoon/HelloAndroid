package com.example.helloandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class HttpConnDemo extends ActionBarActivity {
	EditText editText = null;
	TextView textView = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.actionbar);
		
		editText = (EditText)findViewById(R.id.inputUri);
		textView = (TextView)findViewById(R.id.textMessage);
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    
		return super.onCreateOptionsMenu(menu);
	}



	public void myClickHandler(View view) {
    	String stringUrl = editText.getText().toString();
    	ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo ni = cm.getActiveNetworkInfo();
    	
    	if (ni != null && ni.isConnected()) {
    		textView.setText("Start downloading..");
    		new DownloadTask().execute(stringUrl);
    	} else {
    		textView.setText("No network connection available");
    	}
    }
	
	private class DownloadTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			try {
				return downloadURL(arg0[0]);
			} catch (IOException e) {
				return "Unable to connect web page. Check the url again";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			textView.setText(result);
		}
    }
    
    private String downloadURL(String myUrl) throws IOException {
    	InputStream is = null;
    	int len = 1500;
    	
    	try {
    		URL url = new URL(myUrl);
    		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    		conn.setReadTimeout(10000);
    		conn.setConnectTimeout(10000);
    		conn.setRequestMethod("GET");
    		conn.setDoInput(true);
    		conn.connect();
    		int response = conn.getResponseCode();
    		Log.d(Utility.DEBUG_TAG, "response code : " + response);
    		is = conn.getInputStream();
    		
    		String contentAsString = readIt(is, len);
    		return contentAsString;
    	} finally {
    		if (is != null)
    			is.close();
    	}
    }
    
    public String readIt(InputStream is, int len) throws IOException, UnsupportedEncodingException {
    	Reader reader = null;
    	reader = new InputStreamReader(is, "UTF-8");
    	char[] buf = new char[len];
    	reader.read(buf);
    	return new String(buf);
    }
}
