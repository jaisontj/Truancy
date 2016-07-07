package com.OutSideTheByte.truancy.Notification;

import java.util.Calendar;

import com.OutSideTheByte.truancy.R;
import com.OutSideTheByte.truancy.Database.Database_SubjectList;
import com.OutSideTheByte.truancy.Database.Database_Timetable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyAlarmService extends Service {

	private NotificationManager mManager;
	int bunksLeft = 0;
	int attendancePercentage = 0;

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		// Retrieve the Subject NAme and the Id from the Intent
		String subject_name = intent.getStringExtra("subjectname");
		int id = intent.getIntExtra("notif_id", 0);

		getInfo(subject_name);

		Log.i("Started Service : ", "AlarmService");

		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(
						this.getApplicationContext().NOTIFICATION_SERVICE);

		Intent intent_bunk = new Intent(this.getApplicationContext(),
				Bunking.class);
		intent_bunk.putExtra("D_bunk_Id", subject_name);
		intent_bunk.putExtra("D_bunk_NotifId", id);
		PendingIntent pIntent_bunk = PendingIntent.getService(
				this.getApplicationContext(), id, intent_bunk,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent intent_attend = new Intent(this.getApplicationContext(),
				Attending.class);
		intent_attend.putExtra("D_attend_Id", subject_name);
		intent_attend.putExtra("D_attend_NotifId", id);
		PendingIntent pIntent_attend = PendingIntent.getService(
				this.getApplicationContext(), id, intent_attend,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent intent_noclass = new Intent(this.getApplicationContext(),
				NoClass.class);
		intent_noclass.putExtra("D_noclass_Id", subject_name);
		intent_noclass.putExtra("D_noclass_NotifId", id);
		PendingIntent pIntent_noclass = PendingIntent.getService(
				this.getApplicationContext(), id, intent_noclass,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder nbuild = new NotificationCompat.Builder(
				getApplicationContext())
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("YOU HAVE A CLASS : " + subject_name)
				.setPriority(Notification.PRIORITY_MAX)
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentText(
						"Attendance : " + attendancePercentage
								+ "    Bunks Left : " + bunksLeft)
				.setAutoCancel(true).addAction(0, "Bunk", pIntent_bunk)
				.addAction(0, "Attend", pIntent_attend)
				.addAction(0, "No Class", pIntent_noclass);

		Log.i("Notification Set : ", "Id : " + id + ", Sub :" + subject_name);
		if (!isNotifCancelled(id)) {
			mManager.notify(id, nbuild.build());
		}
		enterDateinDatabase(id);
		stopSelf();
		Log.i("Stop Service : ", "AlarmService");
	}

	void enterDateinDatabase(int rowId) {

		Database_Timetable dt = new Database_Timetable(this);
		dt.open();

		String dt_subname = "";
		String dt_day = "";
		String dt_time = "";
		String dt_status = "";
		String dt_notifstatus = "";
		String dt_notifdate = "";
		Cursor cursordt = dt.getRow(rowId);
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

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		String dayofmonth;
		if (c.get(Calendar.DAY_OF_MONTH) / 10 == 0) {
			dayofmonth = "0" + c.get(Calendar.DAY_OF_MONTH);
		} else
			dayofmonth = "" + c.get(Calendar.DAY_OF_MONTH);

		String month;
		if ((c.get(Calendar.MONTH) + 1) / 10 == 0) {
			month = "0" + (c.get(Calendar.MONTH) + 1);
		} else
			month = "" + (c.get(Calendar.MONTH) + 1);

		dt_notifdate = dayofmonth + "." + month + "." + c.get(Calendar.YEAR);

		cursordt.close();

		dt.updateRow(rowId, dt_day, dt_time, dt_subname, dt_status,
				dt_notifstatus, dt_notifdate);
		dt.close();

	}

	public Boolean isNotifCancelled(int rowId) {

		Database_Timetable dt = new Database_Timetable(this);
		dt.open();

		String dt_notifstatus = "";

		Cursor cursordt = dt.getRow(rowId);
		if (cursordt.moveToFirst()) {
			do {

				dt_notifstatus = cursordt
						.getString(Database_Timetable.COL_NOTIFSTATUS);

			} while (cursordt.moveToNext());
		}

		cursordt.close();

		dt.close();

		if ("C".equalsIgnoreCase(dt_notifstatus)) {
			return true;
		} else
			return false;
	}

	public void getInfo(String subjectName) {
		int totalclasses = 0, bunkCount = 0, maxBunks = 0, attendCount = 0;

		Database_SubjectList db = new Database_SubjectList(this);
		db.open();

		Cursor cursor = db.getLimit(subjectName);

		// Reset Cursor to the start , checking to see if there is any data
		if (cursor.moveToFirst()) {
			do {
				// process the data

				String bunk_count = cursor
						.getString(Database_SubjectList.COL_BUNKED);
				String total_classes = cursor
						.getString(Database_SubjectList.COL_TOTAL);
				String max_bunks = cursor
						.getString(Database_SubjectList.COL_MAXBUNKS);
				String attend_count = cursor
						.getString(Database_SubjectList.COL_ATTENDED);

				attendCount = Integer.parseInt(attend_count);
				bunkCount = Integer.parseInt(bunk_count);
				totalclasses = Integer.parseInt(total_classes);
				maxBunks = Integer.parseInt(max_bunks);

			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		bunksLeft = ((maxBunks * totalclasses) / 100) - bunkCount;
		if (bunksLeft < 0) {
			bunksLeft = 0;
		}

		if ((attendCount + bunkCount) == 0)
			attendancePercentage = 0;
		else
			attendancePercentage = (attendCount / (attendCount + bunkCount)) * 100;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
