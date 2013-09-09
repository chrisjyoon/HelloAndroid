package com.example.helloandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationActivity extends Activity implements 
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener,
	LocationListener {

	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private boolean mUpdatesRequested = false;
	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;
	
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final long UPDATE_INTERVAL = 5000;
	private static final long FASTEST_INTERVAL = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.location_track);
		
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
	
		mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        // Get a SharedPreferences editor
        mEditor = mPrefs.edit();

        mLocationClient = new LocationClient(this, this, this);
        mUpdatesRequested = false;
        
        Log.d(MainActivity.DEBUG_TAG, "LocationActivity::onCreate");
	}
	
	@Override
	protected void onPause() {
		mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
		mEditor.commit();
		super.onPause();
		
		Log.d(MainActivity.DEBUG_TAG, "LocationActivity::onPause");
	}


	@Override
	protected void onResume() {
		super.onResume();
		if (mPrefs.contains("KEY_UPDATES_ON")) {
            mUpdatesRequested =
                    mPrefs.getBoolean("KEY_UPDATES_ON", false);

        // Otherwise, turn off location updates
        } else {
            mEditor.putBoolean("KEY_UPDATES_ON", false);
            mEditor.commit();
        }
		
		Log.d(MainActivity.DEBUG_TAG, "LocationActivity::onResume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!mLocationClient.isConnected()) 
			mLocationClient.connect();
		
		Log.d(MainActivity.DEBUG_TAG, "LocationActivity::onStart");
	}



	@Override
	protected void onStop() {
		// If the client is connected
        /*if (mLocationClient.isConnected()) {
            
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             
        	stopPeriodicUpdates();
        }
		mLocationClient.disconnect();*/
		super.onStop();
		
		Log.d(MainActivity.DEBUG_TAG, "LocationActivity::onStop");
	}
	
	
	
    @Override
	protected void onDestroy() {
    	// If the client is connected
        if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
        	stopPeriodicUpdates();
        }
		mLocationClient.disconnect();
		
		super.onDestroy();
		
		Log.d(MainActivity.DEBUG_TAG, "LocationActivity::onDestroy");
	}

	@Override
	public void onLocationChanged(Location location) {
		String msg = "Updated Location: " +
	                Double.toString(location.getLatitude()) + "," +
	                Double.toString(location.getLongitude());
//		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		
		Log.d(MainActivity.DEBUG_TAG, msg);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(this, "Connected.", Toast.LENGTH_SHORT).show();
		// If already requested, start periodic updates
        if (mUpdatesRequested) {
        	startPeriodicUpdates();
        }
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	/**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {
    	Toast.makeText(this, "start Location Update", Toast.LENGTH_SHORT).show();
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
    	Toast.makeText(this, "stop Location Update", Toast.LENGTH_SHORT).show();
        mLocationClient.removeLocationUpdates(this);
    }
    
    public Location getLocation() {
    	Location currentLocation = null;
    	// If Google Play Services is available
        if (servicesConnected()) {
            // Get the current location
            currentLocation = mLocationClient.getLastLocation();
        }
        return currentLocation;
    }
    
    public void startUpdates() {
    	mUpdatesRequested = true;
    	
    	if (servicesConnected()) {
    		startPeriodicUpdates();
        }
    }
    
    public void stopUpdates() {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
    }

	 private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode =
		        GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
		    // In debug mode, log the status
		    Log.d(MainActivity.DEBUG_TAG, "Google Play services is available.");
		    // Continue
		    return true;
		// Google Play services was not available for some reason
	    } else {
	    	showErrorDialog(resultCode);
	        return false;
	    }
	}
	 
    @SuppressLint("NewApi")
	private void showErrorDialog(int errorCode) {
    	// Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
        		errorCode,
                this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            // Create a new DialogFragment for the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);
            // Show the error dialog in the DialogFragment
            errorFragment.show(getFragmentManager(), "Location Updates");
        }
    }

	// Define a DialogFragment that displays the error dialog
    @SuppressLint("NewApi")
	public static class ErrorDialogFragment extends DialogFragment {
    	private Dialog mDlg;
    	
    	public ErrorDialogFragment() {
    		super();
    		mDlg = null;
    	}
    	
    	public void setDialog(Dialog dlg) {
    		mDlg = dlg;
    	}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDlg;
		}
    	
    }

}
