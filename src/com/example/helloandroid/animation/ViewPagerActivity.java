package com.example.helloandroid.animation;

import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.helloandroid.R;
import com.example.helloandroid.common.Utility;

public class ViewPagerActivity extends FragmentActivity implements ActionBar.TabListener {
	private static int MAX_SECTION = 5;
	private boolean mIsSwiped = false;
	
	SectionsPagerAdapter mSectionsPagerAdapter;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		
		PagerAdapter wrappedAdapter = new InfinitePagerAdapter(mSectionsPagerAdapter);
		
		mViewPager.setAdapter(wrappedAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				Log.d(Utility.DEBUG_TAG, "onPageSelected mIsSwiped = " + mIsSwiped); 
				Log.d(Utility.DEBUG_TAG, "onPageSelected position = " + position);
				mIsSwiped = true;

				position = position % MAX_SECTION;
				Log.d(Utility.DEBUG_TAG, "onPageSelected position = " + position);
				
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		for (int i = 0; i < MAX_SECTION; i++) {
			addTab(actionBar, i);
		}
	}
	
	public void addTab(ActionBar actionBar, int position) {
		Tab tab = actionBar.newTab()
				.setCustomView(R.layout.tab)
				.setTabListener(this);
		TextView tv = (TextView)(tab.getCustomView().findViewById(R.id.tab_title));
		tv.setText(mSectionsPagerAdapter.getPageTitle(position));
		actionBar.addTab(tab);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int screenWidth = displaymetrics.widthPixels;
		View tabContainerView = (View)(tab.getCustomView().getParent());
		int tabPadding = tabContainerView.getPaddingLeft() + tabContainerView.getPaddingRight();
		tv.setWidth(screenWidth / (MAX_SECTION-1) - tabPadding);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (!mIsSwiped) {
			Log.d(Utility.DEBUG_TAG, "tab.getPosition() = " + tab.getPosition());
			Log.d(Utility.DEBUG_TAG, "mIsSwiped = " + mIsSwiped);
			
			mViewPager.setCurrentItem(tab.getPosition());
		}
			
		mIsSwiped = false;
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		Fragment[] fragments = new Fragment[MAX_SECTION];
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Utility.logd("SectionsPagerAdapter : getItem");
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			//Fragment fragment = new DummySectionFragment();
			fragments[position] = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragments[position].setArguments(args);
			return fragments[position];
		}

		@Override
		public int getCount() {
			return MAX_SECTION;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return "AAA";
			case 4:
				return "BBB";
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static final class DummySectionFragment extends Fragment {
		private View mView = null;
		private int mId = 0;
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}
		

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			mId = getArguments().getInt(ARG_SECTION_NUMBER);
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			//if (mView == null) {
				mView = inflater.inflate(R.layout.fragment_main_dummy,
						container, false);
				TextView dummyTextView = (TextView) mView.findViewById(R.id.section_label);
				//dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
				dummyTextView.setText("YES ID : " + mId);
				Utility.logd("mView = " + mView);
				Utility.logd("mId = " + mId);
			//}
			
			return mView;
		}
	}
}
