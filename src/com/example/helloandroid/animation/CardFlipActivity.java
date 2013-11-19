package com.example.helloandroid.animation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.helloandroid.R;
import com.example.helloandroid.Utility;

public class CardFlipActivity extends Activity {
	private static final int MENU_EXERCISE = 0x00;
	private static final int MENU_DAILY = 0x01;
	private boolean[] mIsBack = {false, false, false, false};
	private Fragment[] mFrag = new Fragment[2];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardflip);
		
	}
	
	public void flip(int menuId) {
		Log.d(Utility.DEBUG_TAG, "FLIP");
		
		int viewId = 0;
		Fragment frag = null;
		
		if (mIsBack[menuId]) {
			mIsBack[menuId] = false;
			mFrag[0] = new ExerciseFrontFragment();
			mFrag[1] = new DailyFrontFragment();
		} else {
			mIsBack[menuId] = true;
			mFrag[0] = new ExerciseBackFragment();
			mFrag[1] = new DailyBackFragment();
		}
		
		switch (menuId) {
		case MENU_EXERCISE:
			viewId = R.id.frag_exercise;
			break;
		case MENU_DAILY:
			viewId = R.id.frag_daily;
			break;
		}

		frag = mFrag[menuId];
		
		getFragmentManager()
        .beginTransaction()
        .setCustomAnimations(
                R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                R.animator.card_flip_left_in, R.animator.card_flip_left_out)
        .replace(viewId, frag)
        .commit();
	}

	public static class ExerciseFrontFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_exercise_front, container, false);
			Log.d(Utility.DEBUG_TAG, "ExerciseFrontFragment fm = " + getFragmentManager());
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(Utility.DEBUG_TAG, "CLICK!!!");
					((CardFlipActivity)getActivity()).flip(MENU_EXERCISE);
				}
			});
			return view;
		}

	}
	
	public static class ExerciseBackFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_exercise_back, container, false);
		}
	}
	
	public static class DailyFrontFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_daily_front, container, false);
			Log.d(Utility.DEBUG_TAG, "DailyFrontFragment fm = " + getFragmentManager());
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(Utility.DEBUG_TAG, "daily CLICK!!!");
					((CardFlipActivity)getActivity()).flip(MENU_DAILY);
				}
			});
			return view;
		}

	}
	
	public static class DailyBackFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_daily_back, container, false);
		}
	}
}
