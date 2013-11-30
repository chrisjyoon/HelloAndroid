package com.example.helloandroid.animation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.example.helloandroid.R;
import com.example.helloandroid.Utility;

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
		
		//PagerAdapter wrappedAdapter = new InfinitePagerAdapter(adapter);
		
		//pager.setAdapter(wrappedAdapter);
		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
		pager.setCurrentItem(MAX_TAB);
		
		//tabs.setViewPager(pager);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

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
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Categories", "Home", "Top Paid", "Top Free", "Top Grossing" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length * 3;
		}

		@Override
		public Fragment getItem(int position) {
			Utility.logd("getItem : position = " + position);
			return CardFragment.newInstance(position);
		}
		
		@Override
		public void destroyItem(ViewGroup pager, int position, Object view) {
			//super.destroyItem(pager, position, view);
			//((ViewPager)pager).removeView((View)view);
			
			Utility.logd("destroyItem : position = " + position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Utility.logd("instantiateItem : position = " + position);
			position %= TITLES.length;
			return super.instantiateItem(container, position);
		}

		
	}
}
