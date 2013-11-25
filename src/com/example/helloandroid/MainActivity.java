package com.example.helloandroid;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.helloandroid.animation.CardFlipActivity;
import com.example.helloandroid.animation.ViewPagerActivity;
import com.example.helloandroid.media.MP3Play;

public class MainActivity extends ListActivity {
	SimpleAdapter mAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        	android.R.layout.simple_list_item_1, Utility.mainMenu);
        setListAdapter(adapter);
    }

    
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	Log.d(Utility.DEBUG_TAG, "touch menu = " + position);
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
		case 2: // 3. Location Track Activity
			intent = new Intent(this, LocationTrackDemo.class);
			startActivity(intent);
			break;
		case 3: // 4. Map Activity
			intent = new Intent(this, MapDemo.class);
			startActivity(intent);
			break;		
		case 4: // 5. Play Mp3
			intent = new Intent(this, MP3Play.class);
			startActivity(intent);
			break;
		case 5: // 6. Animation
			intent = new Intent(this, CardFlipActivity.class);
			startActivity(intent);
			break;
		case 6: // 7. ViewPager
			intent = new Intent(this, ViewPagerActivity.class);
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
