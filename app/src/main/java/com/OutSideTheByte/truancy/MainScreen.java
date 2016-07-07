package com.OutSideTheByte.truancy;

import com.OutSideTheByte.truancy.CreateNewTimeTable.*;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainScreen extends FragmentActivity {

	/** Global Initializations **/
	static CustomViewPager viewPager;
	static ActionBar actionBar;
	static int leftPage_num = 1;

	/** Shared Prefs **/
	public static SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String hasRunBefore = "hasRunBefore";
	public static final String setNotifOn = "SetNotif";

	public static Boolean isNotifOn = true;
	static Boolean isMainShowing = true;
	public static Boolean newtimetableCreated = false;
	

	/** Screen Size **/
	static int Screenwidth = 0;
	static int Screenheight = 0;

	int currentPage = 1;

	static Button b_top;
	static RelativeLayout ll_middlePageBox;


	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		// For Transparent ActionBar.
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		setContentView(R.layout.activity_mainscreen);

		b_top = (Button) findViewById(R.id.button_mainbg_top);

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		Intent intent = new Intent(this, CreateNewTimeTable.class);
		startActivity(intent);

		if (sharedpreferences.getBoolean(hasRunBefore, false)) {
			if ((sharedpreferences.getBoolean(setNotifOn, true))) {
				isNotifOn = true;
			} else
				isNotifOn = false;
		} else {



		}

		setScreenDimensions();

		ViewGroup.LayoutParams params = b_top.getLayoutParams();
		if (currentPage == 1) {
			if (isMainShowing)
				params.height = Screenheight / 2;
			else
				params.height = Screenheight;
		} else {
			params.height = 35 * Screenheight / 100;
		}
		b_top.setLayoutParams(params);

		// Setting Up the settings for ActionBar to disable the TitleBar.
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.hide();

		// Setting the ViewPager.
		viewPager = (CustomViewPager) findViewById(R.id.Vpager);

		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

				ViewGroup.LayoutParams params = b_top.getLayoutParams();

				if (currentPage == 1 && arg1 != 0 && arg0 == 0) {
					if (isMainShowing)
						params.height = (int) (((50 - (1 - arg1) * 35) * Screenheight) / 100);
					else
						params.height = (int) (((100 - (1 - arg1) * 85) * Screenheight) / 100);

				} else if (currentPage == 1 && arg1 != 0 && arg0 == 1) {
					if (isMainShowing)
						params.height = (int) (((50 - arg1 * 35) * Screenheight) / 100);
					else
						params.height = (int) (((100 - arg1 * 85) * Screenheight) / 100);

				} else if (currentPage == 0 && arg1 != 0 && arg0 == 0) {

					if (isMainShowing)
						params.height = (int) (((50 - (1 - arg1) * 35) * Screenheight) / 100);
					else
						params.height = (int) (((100 - (1 - arg1) * 85) * Screenheight) / 100);

				} else if (currentPage == 2 && arg1 != 0 && arg0 == 1) {
					if (isMainShowing)
						params.height = (int) (((50 - arg1 * 35) * Screenheight) / 100);
					else
						params.height = (int) (((100 - arg1 * 85) * Screenheight) / 100);

				}

				b_top.setLayoutParams(params);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		// Setting the Page to be Displayed On Startup.
		viewPager.setCurrentItem(currentPage);

	}

	/** Class For the ViewPager : The Fragment Page Adapter **/
	class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {

			Fragment fragment = null;
			if (arg0 == 0) {

				fragment = new Fragment_LeftPage();

			}
			if (arg0 == 1) {

				fragment = new Fragment_MiddlePage();
			}
			if (arg0 == 2) {

				fragment = new Fragment_RightPage();
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

	}

	/** To get the Screen Dimensions **/
	public void setScreenDimensions() {

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		Screenheight = displaymetrics.heightPixels;
		Screenwidth = displaymetrics.widthPixels;

		Log.i("ScreenHeight:", "" + Screenheight);
		Log.i("ScreenWidth:", "" + Screenwidth);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i("onPause", "called");
		super.onPause();
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		Log.i("onPostresume", "called");
		super.onPostResume();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i("onResume", "called");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.i("onStart", "called");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.i("onStop", "called");
		super.onStop();
	}
}