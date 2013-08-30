package com.example.helloandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String DEBUG_TAG = "HelloExample";
	EditText editText = null;
	TextView textView = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        editText = (EditText)findViewById(R.id.inputUri);
        textView = (TextView)findViewById(R.id.textMessage);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    		Log.d(DEBUG_TAG, "response code : " + response);
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
