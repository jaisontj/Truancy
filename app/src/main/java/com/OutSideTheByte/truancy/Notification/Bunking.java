package com.OutSideTheByte.truancy.Notification;

import com.OutSideTheByte.truancy.Database.Database_SubjectList;
import com.OutSideTheByte.truancy.Database.Database_Timetable;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Bunking extends Service {
	Database_SubjectList db;
	Database_Timetable dt;

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.i("Started Service : ", "Bunking");

		int totalclasses = 0, bunkCount = 0, attendCount = 0,maxBunks = 0;
		String subName = "";
		db = new Database_SubjectList(this);
		db.open();
		String subjectName = intent.getStringExtra("D_bunk_Id");
		int id = intent.getIntExtra("D_bunk_NotifId", 0);

		Log.i("Bunking", "id is  " + id + "Sub : " + subjectName);
		Cursor cursor = db.getLimit(subjectName);

		// Reset Cursor to the start , checking to see if there is any data
		if (cursor.moveToFirst()) {
			do {
				// process the data

				subName = cursor
						.getString(Database_SubjectList.COL_SUBJECTNAME);
				String bunk_count = cursor
						.getString(Database_SubjectList.COL_BUNKED);
				String attend_count = cursor
						.getString(Database_SubjectList.COL_ATTENDED);
				String total_classes = cursor
						.getString(Database_SubjectList.COL_TOTAL);
				String max_bunks = cursor
						.getString(Database_SubjectList.COL_MAXBUNKS);
				
				attendCount = Integer.parseInt(attend_count);
				bunkCount = Integer.parseInt(bunk_count);
				totalclasses = Integer.parseInt(total_classes);
				maxBunks = Integer.parseInt(max_bunks);
				
				Log.i("bunkingincursor", "totalclasses: " + total_classes
						+ "and count  is " + bunk_count
						+ "and attende classes is " + attend_count);
			} while (cursor.moveToNext());
		}

		++bunkCount;

		cursor.close();

		Toast.makeText(
				this,
				"Classes Bunked : " + bunkCount + "  ,  Classes Attended : "
						+ attendCount + "  ,  Total Classes : " + totalclasses,
				Toast.LENGTH_LONG).show();

		db.updateSubject(subName, bunkCount, attendCount, totalclasses,maxBunks, subName);
		db.close();

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
				dt_notifstatus = cursordt.getString(Database_Timetable.COL_NOTIFSTATUS);
				dt_notifdate = cursordt.getString(Database_Timetable.COL_NOTIFDATE);

			} while (cursordt.moveToNext());
		}

		dt_status = "B";
		dt_notifstatus = "N";

		// close the cursor to avoid resource leak
		cursordt.close();

		dt.updateRow(id, dt_day, dt_time, dt_subname, dt_status ,dt_notifstatus,dt_notifdate);
		dt.close();

		// to cancel the notification after it is clicked
		if (Context.NOTIFICATION_SERVICE != null) {
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager nMgr = (NotificationManager) getApplicationContext()
					.getSystemService(ns);
			nMgr.cancel(id);
			Log.i("cancel notif : ", "id : " + id);
		}

		stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
