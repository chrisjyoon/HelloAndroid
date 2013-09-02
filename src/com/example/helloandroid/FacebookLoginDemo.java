package com.example.helloandroid;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class FacebookLoginDemo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.facebook);
		
		// start facebook session
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				// TODO Auto-generated method stub
				if (session.isOpened()) {
					Request.newMeRequest(session, new Request.GraphUserCallback() {
						
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								Log.d(MainActivity.DEBUG_TAG, "name = " + user.getName());
								Log.d(MainActivity.DEBUG_TAG, "userName = " + user.getUsername());
								Log.d(MainActivity.DEBUG_TAG, "userId = " + user.getId());
								
								TextView tv = (TextView)findViewById(R.id.textViewFBName);
								tv.setText("name = " + user.getName());
								
								tv = (TextView)findViewById(R.id.textViewFBUserName);
								tv.setText("userName = " + user.getUsername());
							}
						}
					}).executeAsync();
					
				}
			}
		});
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
