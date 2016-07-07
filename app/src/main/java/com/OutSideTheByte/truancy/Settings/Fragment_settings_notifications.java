package com.OutSideTheByte.truancy.Settings;

import java.util.ArrayList;

import com.OutSideTheByte.truancy.Fragment_MiddlePage;
import com.OutSideTheByte.truancy.MainScreen;
import com.OutSideTheByte.truancy.R;
import com.OutSideTheByte.truancy.Database.Database_Timetable;

import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class Fragment_settings_notifications extends Fragment {

	View fragmentView;
	Switch notificationSwitch;
	TextView tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragmentView = inflater.inflate(
				R.layout.fragment_settings_notification, container, false);

		tv = (TextView) fragmentView
				.findViewById(R.id.tv_settings_notification);
		notificationSwitch = (Switch) fragmentView
				.findViewById(R.id.Notification_switch);
		notificationSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						Editor editor = MainScreen.sharedpreferences.edit();
						if (isChecked) {

							// To set the SharedPrefs
							MainScreen.isNotifOn = true;
							editor.putBoolean(MainScreen.setNotifOn, true);

							tv.setText("You will now receive a notification at the time of your class");
							cancelAllNotif();
						} else {

							MainScreen.isNotifOn = false;
							editor.putBoolean(MainScreen.setNotifOn, false);
							tv.setText("You will no longer receive any notifications");
							cancelAllNotif();
						}
						editor.commit();

					}
				});

		if (MainScreen.isNotifOn) {
			tv.setText("You will now receive a notification at the time of your class.");
		} else {
			tv.setText("You will no longer receive any notifications");

		}

		notificationSwitch.setChecked(MainScreen.isNotifOn);

		return fragmentView;
	}

	public void cancelAllNotif() {
		ArrayList<Integer> rowId = new ArrayList<Integer>();

		// Getting all rowIds
		Database_Timetable db = new Database_Timetable(getActivity());
		db.open();

		Cursor cursor = db.getAllRows();
		if (cursor.moveToFirst()) {
			do {
				int row_Id = cursor.getInt(Database_Timetable.COL_ROWID);
				rowId.add(row_Id);

			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// Cancelling each row ID.
		for (int i = 0; i < rowId.size(); i++) {
			if (MainScreen.isNotifOn)
				updateStatusinDB(rowId.get(i), 5);
			else
				updateStatusinDB(rowId.get(i), 4);
		}

	}

	public void updateStatusinDB(int id, int choice) {

		String subName = "";
		String day = "";
		String time = "";
		String status = "";
		String notifdate = "";
		String notifStatus = "";
		int rowId = 0;

		Database_Timetable dt = new Database_Timetable(getActivity());
		dt.open();
		Cursor c_dt = dt.getRow(id);
		if (c_dt.moveToFirst()) {
			do {
				rowId = c_dt.getInt(Database_Timetable.COL_ROWID);
				subName = c_dt.getString(Database_Timetable.COL_SUBJECTNAME);
				day = c_dt.getString(Database_Timetable.COL_DAY);
				time = c_dt.getString(Database_Timetable.COL_TIME);
				status = c_dt.getString(Database_Timetable.COL_STATUS);
				notifStatus = c_dt
						.getString(Database_Timetable.COL_NOTIFSTATUS);
				notifdate = c_dt.getString(Database_Timetable.COL_NOTIFDATE);
			} while (c_dt.moveToNext());
		}
		c_dt.close();

		if (choice == 4) {
			notifStatus = "C";
		} else if (choice == 5) {
			notifStatus = "S";
		}

		dt.updateRow(rowId, day, time, subName, status, notifStatus, notifdate);

		dt.close();

	}

}
