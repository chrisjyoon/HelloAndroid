package com.example.helloandroid;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	public static final String DEBUG_TAG = "HelloExample";

	SimpleAdapter mAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[] { "1. Http Connection", "2. Facebook Login" };
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
    }

    
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Log.d(DEBUG_TAG, "touch menu = " + position);
		//String item = (String)getListAdapter().getItem(position);
		Intent intent = null;
		switch (position) {
		case 0: // 1. HttpConn Activity
			intent = new Intent(this, HttpConnDemo.class);
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
