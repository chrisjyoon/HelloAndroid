package com.example.helloandroid;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationTrackDemo extends FragmentActivity	implements 
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener,
	LocationListener {
	
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final long UPDATE_INTERVAL = 5000;
	private static final long FASTEST_INTERVAL = 1000;
	
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private boolean mUpdatesRequested = false;
	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;

	private TextView textLatLon;
	private TextView textAddr;
	private TextView textState;
    private ProgressBar progActivityIndicator;
    	
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
        
        textLatLon = (TextView)findViewById(R.id.textLatLon);
        textAddr = (TextView)findViewById(R.id.textAddr);
        textState = (TextView)findViewById(R.id.textState);
        progActivityIndicator = (ProgressBar)findViewById(R.id.address_progress);
	}
	
	
	@Override
	protected void onPause() {
		mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
		mEditor.commit();
		super.onPause();
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
	}



	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.connect();
	}



	@Override
	protected void onStop() {
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
		super.onStop();
	}



	@Override
	public void onConnected(Bundle bundle) {
		Toast.makeText(this, "Connected.", Toast.LENGTH_SHORT).show();
		// If already requested, start periodic updates
        if (mUpdatesRequested) {
        	startPeriodicUpdates();
        }
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
	}

	
	@Override
	public void onLocationChanged(Location location) {
		String msg = "Updated Location: " +
	                Double.toString(location.getLatitude()) + "," +
	                Double.toString(location.getLongitude());
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		
		textState.setText(R.string.location_updated);
		textLatLon.setText(msg);
		
		Log.d(Utility.DEBUG_TAG, msg);
	}


	// Define a DialogFragment that displays the error dialog
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			switch (resultCode) {
			case Activity.RESULT_OK:
				// retry?
				break;
			}
			break;
		}
	}

	 private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(Utility.DEBUG_TAG, "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
        	showErrorDialog(resultCode);
            return false;
        }
    }

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
            errorFragment.show(getSupportFragmentManager(), "Location Updates");
        }
    }
    
    public void getLocation(View v) {
    	// If Google Play Services is available
        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();
            String msg = "Current Location: " +
	                Double.toString(currentLocation.getLatitude()) + "," +
	                Double.toString(currentLocation.getLongitude());
            // Display the current location in the UI
            textLatLon.setText(msg);
            //mLatLng.setText(LocationUtils.getLatLng(this, currentLocation));
        }
    }
    
    @SuppressLint("NewApi")
	public void getAddress(View v) {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            // No geocoder is present. Issue an error message
            Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (servicesConnected()) {
            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            // Turn the indefinite activity indicator on
            progActivityIndicator.setVisibility(View.VISIBLE);

            // Start the background task
            (new GetAddressTask(this)).execute(currentLocation);
        }
    }

    public void startUpdates(View v) {
    	mUpdatesRequested = true;

        if (servicesConnected()) {
            startPeriodicUpdates();
        }
    }
    
    public void stopUpdates(View v) {
    	mUpdatesRequested = false;
    	
    	if (servicesConnected()) {
    		stopPeriodicUpdates();
    	}
    }
    
    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
        textState.setText(R.string.location_requested);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
        textState.setText(R.string.location_updates_stopped);
    }

    
    /**
     * An AsyncTask that calls getFromLocation() in the background.
     * The class uses the following generic types:
     * Location - A {@link android.location.Location} object containing the current location,
     *            passed as the input parameter to doInBackground()
     * Void     - indicates that progress units are not used by this subclass
     * String   - An address passed to onPostExecute()
     */
    protected class GetAddressTask extends AsyncTask<Location, Void, String> {

        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;

        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context) {

            // Required by the semantics of AsyncTask
            super();

            // Set a Context for the background task
            localContext = context;
        }

        /**
         * Get a geocoding service instance, pass latitude and longitude to it, format the returned
         * address, and return the address to the UI thread.
         */
        @Override
        protected String doInBackground(Location... params) {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

            // Get the current location from the input parameter list
            Location location = params[0];

            // Create a list to contain the result address
            List <Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
                addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1
                );

                // Catch network or other I/O problems.
                } catch (IOException exception1) {

                    // Log an error and return an error message
                    Log.e(Utility.DEBUG_TAG, getString(R.string.IO_Exception_getFromLocation));

                    // print the stack trace
                    exception1.printStackTrace();

                    // Return an error message
                    return (getString(R.string.IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
                } catch (IllegalArgumentException exception2) {

                    // Construct a message containing the invalid arguments
                    String errorString = getString(
                            R.string.illegal_argument_exception,
                            location.getLatitude(),
                            location.getLongitude()
                    );
                    // Log the error and print the stack trace
                    Log.e(Utility.DEBUG_TAG, errorString);
                    exception2.printStackTrace();

                    //
                    return errorString;
                }
                // If the reverse geocode returned an address
                if (addresses != null && addresses.size() > 0) {

                    // Get the first address
                    Address address = addresses.get(0);

                    // Format the first line of address
                    String addressText = getString(R.string.address_output_string,

                            // If there's a street address, add it
                            address.getMaxAddressLineIndex() > 0 ?
                                    address.getAddressLine(0) : "",

                            address.getSubLocality(),

                            // Locality is usually a city
                            address.getLocality(),
                            
                            
                            // The country of the address
                            address.getCountryName()
                    );

                    // Return the text
                    return addressText;

                // If there aren't any addresses, post a message
                } else {
                  return getString(R.string.no_address_found);
                }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(String address) {

            // Turn off the progress bar
            progActivityIndicator.setVisibility(View.GONE);

            // Set the address in the UI
            textAddr.setText(address);
        }
    }
}
