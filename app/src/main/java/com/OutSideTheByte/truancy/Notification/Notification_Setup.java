package com.OutSideTheByte.truancy.Notification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.OutSideTheByte.truancy.MainScreen;
import com.OutSideTheByte.truancy.Database.Database_Timetable;
import com.OutSideTheByte.truancy.Database.Database_UnAccountedClasses;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

public class Notification_Setup extends Service {

	PendingIntent pendingIntent;
	Database_Timetable dt;
	public static List<Integer> rowIds;

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		Log.i("Started Service :", "Notif_Setup");
		get_notifvalues();

	}

	// Function to call Database and get the timetable.
	public void get_notifvalues() {

		rowIds = new ArrayList<Integer>();

		dt = new Database_Timetable(this);
		dt.open();

		Cursor cursor = dt.getAllRows();

		if (cursor.moveToFirst()) {
			do {
				// process the data
				int id = cursor.getInt(Database_Timetable.COL_ROWID);
				rowIds.add(id);
				String subname = cursor
						.getString(Database_Timetable.COL_SUBJECTNAME);
				String day = cursor.getString(Database_Timetable.COL_DAY);
				String time = cursor.getString(Database_Timetable.COL_TIME);
				String status = cursor.getString(Database_Timetable.COL_STATUS);
				String date = cursor
						.getString(Database_Timetable.COL_NOTIFDATE);

				if ("E".equalsIgnoreCase(status)) {

					Log.i("DB", day+" "+subname + " "+status);
					Database_UnAccountedClasses duc = new Database_UnAccountedClasses(
							this);
					duc.open();
					duc.addUnAccountedClass("" + id, time, day, date, subname);

					duc.close();

					Log.i("DB", "" + day + " " + subname);
				}
				setAlarmforNotif(id, subname, day, time);

			} while (cursor.moveToNext());
		}

		// close the cursor to avoid resource leak
		cursor.close();

		// always close the Database
		dt.close();
		for (int i = 0; i < rowIds.size(); i++) {
			updateDatabaseStatus(rowIds.get(i));
		}

		Log.i("Stop Service:", "Notif_Setup");
		stopSelf();
	}

	public void updateDatabaseStatus(int id) {

		dt = new Database_Timetable(this);
		dt.open();

		String dt_subname = "";
		String dt_day = "";
		String dt_time = "";
		String dt_status = "";
		String dt_notifstatus = "";
		String dt_notifdate = "";
		Cursor cursordt = dt.getRow(id);
		if (cursordt.moveToFirst()) {
			do {

				dt_subname = cursordt
						.getString(Database_Timetable.COL_SUBJECTNAME);
				dt_day = cursordt.getString(Database_Timetable.COL_DAY);
				dt_time = cursordt.getString(Database_Timetable.COL_TIME);
				dt_status = cursordt.getString(Database_Timetable.COL_STATUS);
				dt_notifstatus = cursordt
						.getString(Database_Timetable.COL_NOTIFSTATUS);
				dt_notifdate = cursordt
						.getString(Database_Timetable.COL_NOTIFDATE);

			} while (cursordt.moveToNext());
		}

		dt_status = "E";
		dt_notifstatus = "S";
		dt_notifdate = "";

		// close the cursor to avoid resource leak
		cursordt.close();

		dt.updateRow(id, dt_day, dt_time, dt_subname, dt_status,
				dt_notifstatus, dt_notifdate);
		dt.close();

	}

	public void setAlarmforNotif(int notif_id, String sub, String day,
			String time) {

		// since Time in a string in the format (hh:mm) , to separate the hour
		// and min
		// then to convert the string to integer
		String[] parts = time.split(":");
		String part1 = parts[0];
		String part2 = parts[1];
		int hour = Integer.parseInt(part1);
		int min = Integer.parseInt(part2);

		int dayofweek = 0;

		// convert day to a number Sunday as 1 and Saturday as 7
		if ("Sunday".equals(day)) {
			dayofweek = 1;
		} else if ("Monday".equals(day)) {
			dayofweek = 2;
		} else if ("Tuesday".equals(day)) {
			dayofweek = 3;
		} else if ("Wednesday".equals(day)) {
			dayofweek = 4;
		} else if ("Thursday".equals(day)) {
			dayofweek = 5;
		} else if ("Friday".equals(day)) {
			dayofweek = 6;
		} else if ("Saturday".equals(day)) {
			dayofweek = 7;
		}

		// Create a new calendar set to the date chosen
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.DAY_OF_WEEK, dayofweek);

		// checking if time has passed
		Calendar cCheck = Calendar.getInstance();
		cCheck.setTimeInMillis(System.currentTimeMillis());

		// Log.i("DAY ", "Broadcast received: " + dayofweek);

		// passing the subject name and Id in the intent to the broadcast
		// receiver
		Intent myIntent = new Intent(Notification_Setup.this,
				NotificationReceiver.class);
		myIntent.putExtra("Sub_name", sub);
		myIntent.putExtra("idfornotif", notif_id);

		// notif_id has to passed to it to make sure a unique id is passed each
		// time
		// to prevent it from getting overwritten
		pendingIntent = PendingIntent.getBroadcast(Notification_Setup.this,
				notif_id, myIntent, 0);

		// set the Alarm Manager to wake up at the specified Day and Time
		// Also set it to repeat every week.
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		if (cCheck.getTimeInMillis() <= c.getTimeInMillis()) {

			Log.i("Alarm Set : ", "Day : " + day + " ,Time :" + time
					+ " , Sub : " + sub);
			alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
					pendingIntent);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}