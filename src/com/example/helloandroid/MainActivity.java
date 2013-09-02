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
import android.content.Intent;
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

	SimpleAdapter mAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[] { "1. ActionBar", "2. Facebook Login" };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
    }

    
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Log.d(DEBUG_TAG, "touch menu = " + position);
		String item = (String)getListAdapter().getItem(position);
		Intent intent = null;
		switch (position) {
		case 0: // 1. ActionBar Activity
			intent = new Intent(this, ActionBarDemo.class);
			startActivity(intent);
			break;
		case 1: // 2. Facebook Login Activity
			intent = new Intent(this, FacebookLoginDemo.class);
			startActivity(intent);
			break;
		}
		super.onListItemClick(l, v, position, id);
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

	
}
