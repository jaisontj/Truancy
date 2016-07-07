package com.OutSideTheByte.truancy.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//Receive the subject name and Id and then send it to the service via the intent
		String subjectname = intent.getStringExtra("Sub_name");
		int id_notif = intent.getIntExtra("idfornotif", 0);
		
		Log.i("Receiver : ", "Broadcast received: " + id_notif);
		
		Intent service = new Intent(context, MyAlarmService.class);
		service.putExtra("subjectname", subjectname);
		service.putExtra("notif_id", id_notif);
		
		Log.i("Start Service :", "AlarmService");
		context.startService(service);

	}

}
