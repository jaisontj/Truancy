package com.OutSideTheByte.truancy.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class EveryWeekServiceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Intent weeklyservice = new Intent(context, Notification_Setup.class);
		Log.i("Receiver : ","EveryWeekService");
		Log.i("Start Service:","Notif_Setup");
		context.startService(weeklyservice);
		
	}

}
