package com.OutSideTheByte.truancy.CreateNewTimeTable;

import java.util.ArrayList;
import java.util.List;

import com.OutSideTheByte.truancy.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CreateNewTimeTable extends Activity {

	ActionBar actionBar;

	final static List<TextView> SUBJECT_NAME = new ArrayList<TextView>();
	final static List<TextView> SUBJECT_NAME_FINAL = new ArrayList<TextView>();

	public static int total_weeks;
	public static int bunkLimit;
	int stepTracker = 1;
	
	/** Screen Size **/
	static int Screenwidth = 0;
	static int Screenheight = 0;

	static FrameLayout container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createnewtt);

		SUBJECT_NAME.clear();
		SUBJECT_NAME_FINAL.clear();
		
		setScreenDimensions();

		// Setting Up the settings for ActionBar to disable the TitleBar.
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.hide();

		container = (FrameLayout) findViewById(R.id.createnewtt_container);

		getFragmentManager().beginTransaction()
				.add(R.id.createnewtt_container, new Fragment_Step1()).commit();

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

}