package com.example.helloandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class Utility {
	public static final String DEBUG_TAG = "HelloExample";
	
	static String[] mainMenu = { 
		"1. Http Connection", 
		"2. Facebook Login", 
		"3. Location Track",
		"4. Map",
		"5. Mp3",
		"6. Animation",
		"7. ViewPager"
    };
	
	public static class AlertDialogFragment extends DialogFragment {
		public interface OnClick {
			public void onClickPositive(String tag);
		}
		public static AlertDialogFragment newInstance(int titleId, String msg) {
			AlertDialogFragment af = new AlertDialogFragment();
			Bundle args = new Bundle();
			args.putInt("titleId", titleId);
			args.putString("msg", msg);
			
			af.setArguments(args);
			return af;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
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
	}
}
