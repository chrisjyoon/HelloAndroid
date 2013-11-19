package com.example.helloandroid.media;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.helloandroid.MainActivity;
import com.example.helloandroid.R;
import com.example.helloandroid.Utility;
import com.example.helloandroid.Utility.AlertDialogFragment;

public class MP3Play extends Activity implements WriteWatcher.OnComplete {
	MediaPlayer mPlayer;
	boolean mIsPrepared = false;
	boolean mIsRepeat = false;
	Button mBtnPlay;
	Button mBtnPause;
	Button mBtnStop;
	SeekBar mSeekBar;
	SeekUpdate mSeekUpdate;
	TextView mTvTime;
	TextView mTvLine;
	EditText mEtInput;
	private long mStartTime = 0;
	private long mEndTime = 0;
	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mp3play);
		mBtnPlay = (Button)findViewById(R.id.btn_play);
		mBtnStop = (Button)findViewById(R.id.btn_stop);
		mBtnPause = (Button)findViewById(R.id.btn_pause);
		mSeekBar = (SeekBar)findViewById(R.id.seekBar); 
				
		mPlayer = MediaPlayer.create(this, R.raw.test1);
		
		mPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer player) {
				player.seekTo(0);
				mIsPrepared = true;
			}
		});
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer player) {
				Log.d(Utility.DEBUG_TAG, "onComplete");
				
				setInit();
				
				if (mIsRepeat) {
					Log.d(Utility.DEBUG_TAG, "Pause");
					
					(new Pause()).execute(1000);	
				} else {
					mBtnStop.setClickable(false);
					mBtnPlay.setVisibility(View.VISIBLE);
					mBtnPause.setVisibility(View.GONE);
				}
			}
		});

		mBtnPause.setVisibility(View.GONE);
		mBtnStop.setClickable(false);
		int duration = mPlayer.getDuration();
		mSeekBar.setMax(duration);
		
		mTvTime = (TextView)findViewById(R.id.txt_start_time);
		((TextView)findViewById(R.id.txt_end_time)).setText(getTimeStamp(duration));
		
		mTvLine = (TextView)findViewById(R.id.txt_line);
		mEtInput = (EditText)findViewById(R.id.input_line);
		
		WriteWatcher ww = new WriteWatcher(mEtInput, mTvLine, getString(R.string.txt_test1));
		ww.setOnCompleteListener(this);
		mEtInput.addTextChangedListener(ww);
		mEtInput.setEnabled(false);
		mEtInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_DEL) 
					return true;
				return false;
			}
		});
		
		setInit();
		
		mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
		
		if (mSeekUpdate != null)
			mSeekUpdate.cancel(true);
	}
	
	@Override
	public void onSentenceComplete() {
		stopMp3(mBtnStop);
		
		mEndTime = System.currentTimeMillis();
		
		long elapsed = mEndTime - mStartTime;
		
		int sec = (int)(elapsed / 1000);
		String msg = sec + " 초";
		
		long saved = mPrefs.getLong("elapsed", 0);
		if (saved == 0 || saved > elapsed) {
			msg = "기록 경신!\n" + msg;
			mEditor = mPrefs.edit();
			mEditor.putLong("elapsed", elapsed);
			mEditor.commit();
		}
		
		AlertDialogFragment adf = AlertDialogFragment.newInstance(R.string.title_complete, msg);
		adf.show(getFragmentManager(), null);
		
		((Button)findViewById(R.id.btn_write)).setClickable(true);
	}

	public void setInit() {
		mTvTime.setText("00:00");
		mSeekBar.setProgress(0);		
	}

	public void playMp3(View v) {
		v.setVisibility(View.GONE);
		mBtnPause.setVisibility(View.VISIBLE);
		mBtnStop.setClickable(true);
		
		play();
	}
	
	public void play() {
		if (!mIsPrepared) return;
		mPlayer.start();
		mSeekUpdate = new SeekUpdate(); 
		mSeekUpdate.execute();
	}
	
	
	
	public void toggleRepeat(View v) {
		mIsRepeat = !mIsRepeat;
		if (mIsRepeat) {
			((TextView)v).setText("자동반복 ON");
			mPlayer.setLooping(true);
		}
		else {
			((TextView)v).setText("자동반복 OFF");
			mPlayer.setLooping(false);
		}
	}
	
	public void pauseMp3(View v) {
		mPlayer.pause();
		
		v.setVisibility(View.GONE);
		mBtnPlay.setVisibility(View.VISIBLE);
		mBtnStop.setClickable(true);
	}
	
	public void stopMp3(View v) {
		mPlayer.stop();
		mIsPrepared = false;
		mPlayer.prepareAsync();
		
		v.setClickable(false);
		mBtnPlay.setVisibility(View.VISIBLE);
		mBtnPause.setVisibility(View.GONE);
		setInit();
	}
	
	public void write(View v) {
		mStartTime = System.currentTimeMillis();
		mEtInput.setEnabled(true);
		v.setClickable(false);
	}
	
	public String getTimeStamp(int millisec) {
		int sec = millisec / 1000;
		int min = sec / 60;
		
		return String.format("%02d:%02d", min, sec % 60);
	}
	
	private class SeekUpdate extends AsyncTask<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			Log.d(Utility.DEBUG_TAG, "Seek Update Start ! ");
			
			while (mPlayer != null && mPlayer.isPlaying()) {
				publishProgress(mPlayer.getCurrentPosition());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... position) {
			mSeekBar.setProgress(position[0]);
			mTvTime.setText(getTimeStamp(position[0]));
		}


		@Override
		protected void onPostExecute(Void result) {
			Log.d(Utility.DEBUG_TAG, "stopped.");
			super.onPostExecute(result);
		}
	}
	
	private class Pause extends AsyncTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... args) {
			mPlayer.pause();
			try {
				Thread.sleep(args[0]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mPlayer.start();
			super.onPostExecute(result);
		}
		
	}
}
