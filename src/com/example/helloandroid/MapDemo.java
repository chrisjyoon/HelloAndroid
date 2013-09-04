package com.example.helloandroid;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
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
		
		if (!currPos.equals(lastPos)) {
			Toast.makeText(this, "MOVED !", Toast.LENGTH_SHORT).show();
		}
		
		rectOptions = new PolylineOptions()
	        .add(lastPos)
	        .add(currPos);
		// Get back the mutable Polyline
		Polyline polyline = mMap.addPolyline(rectOptions);
		
		lastPos = currPos;
	}


	@SuppressLint("NewApi")
	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (mMap == null) {
	        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	                            .getMap();
	        // Check if we were successful in obtaining the map.
	        if (mMap != null) {
	            // The Map is verified. It is now safe to manipulate the map.
	        	
	        }
	    }
	}
	
}
