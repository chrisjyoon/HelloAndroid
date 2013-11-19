package com.example.helloandroid.media;

import java.util.Locale;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.helloandroid.Utility;

public class WriteWatcher implements TextWatcher {
	private EditText mEtInput;
	private TextView mTvLine;
	private String mSentence;
	private String[] mWords;
	private int mWordPos;
	private int mPosition;
	private String mCorrectWord;
	private StringBuilder mText = new StringBuilder();
	private OnComplete mCallback = null;
	
	public interface OnComplete {
		public void onSentenceComplete();
	}
	
	public WriteWatcher(EditText e, TextView t, String sentence) {
		mEtInput = e;
		mTvLine = t;
		mSentence = sentence.toLowerCase(Locale.ENGLISH);
		mWords = mSentence.split(" ");
		init();
	}
	
	public void init() {
		mWordPos = 0;
		mPosition = 0;
		mCorrectWord = "";
	}
	
	public void setOnCompleteListener(OnComplete l) {
		mCallback = l;
	}
	
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.d(Utility.DEBUG_TAG, "mPosition = " + mPosition + ", start = " + start);
		if (start < before) return;
		
		Log.d(Utility.DEBUG_TAG, "s = " + s + ", mWords = " + mWords[mWordPos]);
		Log.d(Utility.DEBUG_TAG, "start = " + start);
		Log.d(Utility.DEBUG_TAG, "before = " + before);
		Log.d(Utility.DEBUG_TAG, "count = " + count);
		
		if (s.length() <= 0) return;
		
		String input = s.toString();
		String word = mWords[mWordPos];
		if (isCorrect(input.toLowerCase(Locale.ENGLISH), word)) {
			mCorrectWord += word.charAt(mPosition);
			mPosition ++;
			
			if (nextPunctuation(word)) {
				mCorrectWord += word.charAt(mPosition);
				mPosition ++;
			}
			
			if (mPosition == (word.length())) {
				mPosition = 0;
				mWordPos ++;
				mText.append(mCorrectWord).append(' ');
				
				mCorrectWord = "";
				mEtInput.setText(mCorrectWord);
				mTvLine.setText(mText);
				
				if (mWordPos == mWords.length) {
					mEtInput.setEnabled(false);
					mCallback.onSentenceComplete();
					mText.delete(0, mText.length());
					mTvLine.setText("");
					init();
				}
			}
		} else {
			Log.d(Utility.DEBUG_TAG, "mmCorrectWord = " + mCorrectWord);
			mEtInput.setText(mCorrectWord);
			mEtInput.setSelection(mPosition);
		}
	}
	
	public boolean isCorrect(String input, String org) {
		if (input.charAt(input.length()-1) == org.charAt(mPosition)) {
			return true;
		}
		return false;
	}
	
	public boolean nextPunctuation(String org) {
		if (mPosition < org.length()) {
			if (org.charAt(mPosition) == '.' || 
					org.charAt(mPosition) == ',' ||
					org.charAt(mPosition) == '\'' ||
					org.charAt(mPosition) == '?' ||
					org.charAt(mPosition) == '!') 
				return true;
		}
		return false;
	}

}
