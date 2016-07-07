package com.OutSideTheByte.truancy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_Timetable extends SQLiteOpenHelper {

	// Database ROw details
	private final static String KEY_ROWID = "_Id";
	private final static String KEY_SUBJECTNAME = "Subject_Name";
	private final static String KEY_DAY = "Day";
	private final static String KEY_TIME = "Time";
	private final static String KEY_STATUS = "Bunk_stat";
	private final static String KEY_NOTIFSTATUS = "Notif_stat";
	private final static String KEY_NOTIFDATE = "Notif_date";

	public static final String[] ALL_KEYS = new String[] { KEY_ROWID,
			KEY_SUBJECTNAME, KEY_DAY, KEY_TIME, KEY_STATUS, KEY_NOTIFSTATUS,
			KEY_NOTIFDATE };

	// Database and table details
	private final static String DATABASE_NAME = "Timetable_Details";
	private final static int DATABASE_VERSION = 1;
	private static String TABLE_NAME = "Day_and_Time";

	// Field Numbers
	public static final int COL_ROWID = 0;
	public static final int COL_SUBJECTNAME = 1;
	public static final int COL_DAY = 2;
	public static final int COL_TIME = 3;
	public static final int COL_STATUS = 4;
	public static final int COL_NOTIFSTATUS = 5;
	public static final int COL_NOTIFDATE = 6;

	// private final Context context;
	private SQLiteDatabase myDatabase;

	public Database_Timetable(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creating a table
		db.execSQL(

		"CREATE TABLE " + TABLE_NAME + " (" + KEY_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_SUBJECTNAME
				+ " TEXT, " + KEY_DAY + " TEXT, " + KEY_TIME + " TEXT, "
				+ KEY_STATUS + " TEXT, " + KEY_NOTIFSTATUS + " TEXT, "
				+ KEY_NOTIFDATE + " TEXT);"

		);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop table if exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Now re-create it
		onCreate(db);

	}

	public Database_Timetable open() {
		myDatabase = this.getWritableDatabase();
		return this;
	}

	// Close the database connection.
	public void close() {
		myDatabase.close();
	}

	// Adding timetable for a subject
	public void addTimetable(String day, String time, String subject_name) {
		myDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_SUBJECTNAME, subject_name); // Subject Name
		values.put(KEY_DAY, day); // day
		values.put(KEY_TIME, time); // time
		values.put(KEY_STATUS, "");
		values.put(KEY_NOTIFSTATUS, "NS");
		values.put(KEY_NOTIFDATE, "");

		myDatabase.insert(TABLE_NAME, null, values);
		myDatabase.close();
	}

	// Update
	public void updateRow(int rowId, String day, String time,
			String subject_name, String status, String notif_status , String notif_date) {
		SQLiteDatabase myDatabase = this.getWritableDatabase();

		String where = KEY_ROWID + "=" + rowId;
		ContentValues values = new ContentValues();

		values.clear();
		values.put(KEY_SUBJECTNAME, subject_name); // Subject Name
		values.put(KEY_DAY, day);
		values.put(KEY_TIME, time);
		values.put(KEY_STATUS, status);
		values.put(KEY_NOTIFSTATUS, notif_status);
		values.put(KEY_NOTIFDATE, notif_date);

		myDatabase.update(TABLE_NAME, values, where, null);

		myDatabase.close();

	}

	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return myDatabase.delete(TABLE_NAME, where, null) != 0;
	}

	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));
			} while (c.moveToNext());
		}
		c.close();
	}

	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;
		Cursor c = myDatabase.query(true, TABLE_NAME, ALL_KEYS, where, null,
				null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = myDatabase.query(true, TABLE_NAME, ALL_KEYS, where, null,
				null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

}
