package com.example.helloandroid.animation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.example.helloandroid.R;
import com.example.helloandroid.common.Utility;

public class SlidingTabViewPagerActivity extends FragmentActivity {
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;
	private static final int MAX_TAB = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidingtab_viewpager);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		
		pager = (ViewPager) findViewById(R.id.pager);
		
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		
		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		
		tabs.setViewPager(pager);
		/*tabs.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int position) {
				position %= MAX_TAB;
				Utility.logd("OnCreate.onPageSelected : " + position);
				if (position < MAX_TAB) {
					pager.setCurrentItem(position + MAX_TAB, false);
				} else if (position >= MAX_TAB * 2) {
					pager.setCurrentItem(position - MAX_TAB, false);
				} else {
					position -= MAX_TAB;
				}
			}
			
		});
		*/
		//pager.setCurrentItem(MAX_TAB);
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Categories", "Home", "Top Paid", "Top Free", "Top Grossing" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position % MAX_TAB];
		}

		@Override
		public int getCount() {
			return TITLES.length;// * 3;
		}

		@Override
		public Fragment getItem(int position) {
			Utility.logd("getItem : position = " + position);
			return CardFragment.newInstance(position % MAX_TAB);
		}
		
		@Override
		public void destroyItem(ViewGroup pager, int position, Object view) {
			super.destroyItem(pager, position, view);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {			
			return super.instantiateItem(container, position);
		}
		
	}
}
