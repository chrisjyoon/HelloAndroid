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
import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	public static final String DEBUG_TAG = "HelloExample";
	EditText editText = null;
	TextView textView = null;
	SimpleAdapter mAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
    }

    
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Log.d(DEBUG_TAG, "touch menu = " + position);
		String item = (String)getListAdapter().getItem(position);
		super.onListItemClick(l, v, position, id);
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
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			Toast.makeText(this, "Do nothing...", Toast.LENGTH_SHORT).show();
			break;
		default:
				return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
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
