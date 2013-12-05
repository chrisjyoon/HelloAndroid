package com.example.helloandroid.common;

import com.example.helloandroid.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Utility {
	public static final String DEBUG_TAG = "HelloExample";
	
	public static String[] mainMenu = { 
		"1. Http Connection", 
		"2. Facebook Login", 
		"3. Location Track",
		"4. Map",
		"5. Mp3",
		"6. Animation",
		"7. ViewPager",
		"8. SlidingTabViewPager",
		"9. BottomUpWebView",
		"10. AlertActivity"
    };
	
	public static void logd(String logStr) {
		Log.d(DEBUG_TAG, logStr);
	}
	
	public static class AlertDialogFragment extends DialogFragment {
		private static boolean mIsCustom;
		
		public interface OnClick {
			public void onClickPositive(String tag);
		}
		public static AlertDialogFragment newInstance(int titleId, String msg) {
			mIsCustom = false;
			AlertDialogFragment af = new AlertDialogFragment();
			Bundle args = new Bundle();
			args.putInt("titleId", titleId);
			args.putString("msg", msg);
			
			af.setArguments(args);
			return af;
		}
		public static AlertDialogFragment newInstanceCustom(int titleId, String msg) {
			mIsCustom = true;
			AlertDialogFragment af = new AlertDialogFragment();
			Bundle args = new Bundle();
			args.putInt("titleId", titleId);
			args.putString("msg", msg);
			
			af.setArguments(args);
			return af;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			if (mIsCustom) return super.onCreateDialog(savedInstanceState);
			int titleId = getArguments().getInt("titleId");
			String msg = getArguments().getString("msg");
			
			AlertDialog dlg = new AlertDialog.Builder(getActivity())
			.setTitle(titleId)
			.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//((OnClick)getActivity()).onClickPositive(getTag());
				}
			})
			.create();

			if (msg != null)
				dlg.setMessage(msg);
			
			return dlg;
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			if (mIsCustom) {
				View view = inflater.inflate(R.layout.activity_popup, container, false);
				
				return view;
			} else
				return null;//super.onCreateView(inflater, container, savedInstanceState);
		}
		
	}
}
