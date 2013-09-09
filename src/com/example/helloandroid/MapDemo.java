package com.example.helloandroid;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapDemo extends LocationActivity {

	private GoogleMap mMap;
	private LatLng lastPos;
	private LatLng currPos;
	private PolylineOptions rectOptions;
	
	private Handler mHandler = new Handler();
	private long startTime; 
	private long elapsedTime; 
	private final int REFRESH_RATE = 1000; 
	private String hours,minutes,seconds; 
	private long secs,mins,hrs;
	private boolean stopped = false;
	private Location mLastLocation = null;
	
	private static final int lineColor = Color.argb(75, 0, 170, 180);
	private static final float lineWidth = 20;
	private static final double moveDiff = 0.00003;
	private static final double defaultLat = 37.57; 
	private static final double defaultLon = 126.98;
	private static final float defaultZoom = 15; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		setUpMapIfNeeded();

	}
	
	
	@Override
	public void onConnected(Bundle arg0) {
		Location mLoc = getLocation();
		
		if (mLoc != null) {
			currPos = new LatLng(mLoc.getLatitude(), mLoc.getLongitude());
			lastPos = currPos;
			
			mMap.moveCamera(CameraUpdateFactory.newLatLng(currPos));
			
			mMap.addMarker(new MarkerOptions()
	        .position(currPos)
	        .title("Hello world"));
			
			mLastLocation = mLoc;
			
		} else {
			Toast.makeText(this, "Can't get location!", Toast.LENGTH_SHORT).show();
		}
	
		super.onConnected(arg0);
		
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		super.onLocationChanged(location);
		
		currPos = new LatLng(location.getLatitude(), location.getLongitude());
		Log.d(MainActivity.DEBUG_TAG, "lastPos.latitude = " + lastPos.latitude);
		Log.d(MainActivity.DEBUG_TAG, "lastPos.longitude = " + lastPos.longitude);
		
		Log.d(MainActivity.DEBUG_TAG, "currPos.latitude = " + currPos.latitude);
		Log.d(MainActivity.DEBUG_TAG, "currPos.longitude = " + currPos.longitude);
		
		if (isMoved(location)) {
			Toast.makeText(this, "MOVED !", Toast.LENGTH_SHORT).show();
			Log.d(MainActivity.DEBUG_TAG, "MOVED!!!!!!!!!!!!!");
			
			drawLine();
			
			lastPos = currPos;
			mLastLocation = location;
		}
		
	}
	
	public void drawLine() {
		rectOptions = new PolylineOptions()
	        .add(lastPos)
	        .add(currPos);
		rectOptions.color(lineColor);
		rectOptions.width(lineWidth);
		
		// Get back the mutable Polyline
		Polyline polyline = mMap.addPolyline(rectOptions);
	}

	public boolean isMoved(Location location) {
		if (mLastLocation != null) {
			float dist = location.distanceTo(mLastLocation);
			Log.d(MainActivity.DEBUG_TAG, "dist = " + dist);
			if (dist > 3) {
				return true;
			}
		}
		
		return false;
	}

	@SuppressLint("NewApi")
	private void setUpMapIfNeeded() {
   	
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	                            .getMap();
	      
	    	// Check if we were successful in obtaining the map.
	        if (mMap != null) {
	        	mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	        	mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(defaultLat, defaultLon), defaultZoom));
	            // The Map is verified. It is now safe to manipulate the map.
	        }
	    }
	}
	
	public void startTrack(View view) { 
		showStopButton(); 
		if(stopped) { 
			startTime = System.currentTimeMillis() - elapsedTime; 
		} else { 
			startTime = System.currentTimeMillis(); 
		} 
		
		mHandler.removeCallbacks(startTimer); 
		mHandler.postDelayed(startTimer, 0);
		
		startUpdates();
	}
	
	public void stopTrack(View view) {
		hideStopButton();
		mHandler.removeCallbacks(startTimer);
		stopped = true;
		stopUpdates();
	}
	
	public void showStopButton() {
		((Button)findViewById(R.id.btn_start)).setVisibility(View.GONE);
		((Button)findViewById(R.id.btn_stop)).setVisibility(View.VISIBLE);
	}
	
	public void hideStopButton() {
		((Button)findViewById(R.id.btn_start)).setVisibility(View.VISIBLE);
		((Button)findViewById(R.id.btn_stop)).setVisibility(View.GONE);
	}
	
	private Runnable startTimer = new Runnable() { 
		public void run() { 
			elapsedTime = System.currentTimeMillis() - startTime; 
			updateTimer(elapsedTime); 
			mHandler.postDelayed(this,REFRESH_RATE); 
			
/*			Location mLoc = getLocation();
			
			mLoc.setLatitude(lastPos.latitude+moveDiff);
			mLoc.setLongitude(lastPos.longitude+moveDiff);
			onLocationChanged(mLoc);*/
		} 
	};

	private void updateTimer (float time) { 
		secs = (long)(time/1000); 
		mins = (long)((time/1000)/60); 
		hrs = (long)(((time/1000)/60)/60); 
		/* Convert the seconds to String * and format to ensure it has * a leading zero when required */ 
		secs = secs % 60; 
		seconds=String.valueOf(secs); 
		if(secs == 0){ seconds = "00"; } 
		if(secs <10 && secs > 0){ seconds = "0"+seconds; }
		/* Convert the minutes to String and format the String */ 
		mins = mins % 60; 
		minutes=String.valueOf(mins); 
		if(mins == 0){ minutes = "00"; } 
		if(mins <10 && mins > 0){ minutes = "0"+minutes; } 
		/* Convert the hours to String and format the String */ 
		hours=String.valueOf(hrs); 
		if(hrs == 0){ hours = "00"; } 
		if(hrs <10 && hrs > 0){ hours = "0"+hours; } 
		/* Setting the timer text to the elapsed time */ 
		((TextView)findViewById(R.id.timer)).setText(hours + ":" + minutes + ":" + seconds); 
		 
	}
}
