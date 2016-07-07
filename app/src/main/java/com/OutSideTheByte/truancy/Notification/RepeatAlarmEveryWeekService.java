package com.OutSideTheByte.truancy.Notification;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

public class RepeatAlarmEveryWeekService extends IntentService {

	PendingIntent pendingIntent;

	public RepeatAlarmEveryWeekService() {
		super("RepeatAlarmEveryWeekService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		Log.i("Started Service:", "RepeatEveryWeek");
		// Create a new calendar set to the date chosen
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.DAY_OF_WEEK, 1);

		Intent myIntent = new Intent(RepeatAlarmEveryWeekService.this,
				EveryWeekServiceReceiver.class);

		pendingIntent = PendingIntent.getBroadcast(
				RepeatAlarmEveryWeekService.this, 0, myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Log.i("Alarm Set:", "Sunday Midnight");
		alarmManager.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), 7 * 24
				* 60 * 60 * 1000, pendingIntent);

	}

}
