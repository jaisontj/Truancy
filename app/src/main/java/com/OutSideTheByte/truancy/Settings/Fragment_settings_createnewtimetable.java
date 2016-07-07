package com.OutSideTheByte.truancy.Settings;

import com.OutSideTheByte.truancy.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment_settings_createnewtimetable extends Fragment {
	
	View fragmentView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragmentView = inflater.inflate(
				R.layout.fragment_settings_createnewtt, container, false);
		

		return fragmentView;
	}
}
