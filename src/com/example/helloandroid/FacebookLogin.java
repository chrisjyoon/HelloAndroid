package com.example.helloandroid;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FacebookLogin extends Fragment {
	private ProfilePictureView profilePictureView;
	private TextView tvName = null;
	private TextView tvUserName = null;
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = 
		new Session.StatusCallback() {
		    @Override
		    public void call(Session session, 
		            SessionState state, Exception exception) {
		        onSessionStateChange(session, state, exception);
		    }
		};
	private static final int REAUTH_ACTIVITY_CODE = 100;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.facebook_login, container, false);
		
		profilePictureView = (ProfilePictureView)view.findViewById(R.id.profilePic);
		profilePictureView.setCropped(true);
		tvName = (TextView)view.findViewById(R.id.textViewFBName);
		tvUserName = (TextView)view.findViewById(R.id.textViewFBUserName);
	
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		
		// Check for an open session
	    Session session = Session.getActiveSession();
	    if (session != null && session.isOpened()) {
	        // Get the user's data
	        makeMeRequest(session);
	    }
		
		return view;
	}

	public void makeMeRequest(final Session session) {
		Request req = Request.newMeRequest(session, new Request.GraphUserCallback() {
			
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					Log.d(Utility.DEBUG_TAG, "name = " + user.getName());
					Log.d(Utility.DEBUG_TAG, "userName = " + user.getUsername());
					Log.d(Utility.DEBUG_TAG, "userId = " + user.getId());
					
					profilePictureView.setProfileId(user.getId());
					tvName.setText(user.getName());
					tvUserName.setText("userName = " + user.getUsername());
					
					//https://graph.facebook.com/mike.shaver/picture
				}
			}
		});
		req.executeAsync();
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
	        // Get the user's data.
	        makeMeRequest(session);
	    }
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REAUTH_ACTIVITY_CODE) {
	        uiHelper.onActivityResult(requestCode, resultCode, data);
	    }
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
}
