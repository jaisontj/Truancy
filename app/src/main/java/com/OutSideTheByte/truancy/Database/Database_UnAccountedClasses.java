package com.OutSideTheByte.truancy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_UnAccountedClasses extends SQLiteOpenHelper {

	// Database ROw details
	private final static String KEY_ROWID = "_Id";
	private final static String KEY_CLASSID = "_ClassId";
	private final static String KEY_TIME = "_Time";
	private final static String KEY_DAY = "_Day";
	private final static String KEY_DATE = "_Date";
	private final static String KEY_SUBJECTNAME = "Subject_Name";

	public static final String[] ALL_KEYS = new String[] { KEY_ROWID,
			KEY_CLASSID, KEY_TIME, KEY_DAY, KEY_DATE, KEY_SUBJECTNAME };

	// Database and table details
	private final static String DATABASE_NAME = "UnAccountedClass_Details";
	private final static int DATABASE_VERSION = 2;
	private static String TABLE_NAME = "UnAccountedClasses";

	// Field Numbers
	public static final int COL_ROWID = 0;
	public static final int COL_CLASSID = 1;
	public static final int COL_TIME = 2;
	public static final int COL_DAY = 3;
	public static final int COL_DATE = 4;
	public static final int COL_SUBJECTNAME = 5;

	// private final Context context;
	private SQLiteDatabase myDatabase;

	public Database_UnAccountedClasses(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Creating a table
		db.execSQL(

		"CREATE TABLE " + TABLE_NAME + " (" + KEY_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_CLASSID
				+ " TEXT, " + KEY_TIME + " TEXT, " + KEY_DAY + " TEXT,"
				+ KEY_DATE + " TEXT," + KEY_SUBJECTNAME + " TEXT);"

		);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop table if exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Now re-create it
		onCreate(db);

	}

	public Database_UnAccountedClasses open() {
		myDatabase = this.getWritableDatabase();
		return this;
	}

	// Close the database connection.
	public void close() {
		myDatabase.close();
	}

	// Adding timetable for a subject
	public void addUnAccountedClass(String classId, String time,String day,String date,String subjectName) {
		myDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_CLASSID, classId); // Subject Name
		values.put(KEY_TIME,time);
		values.put(KEY_DAY,day);
		values.put(KEY_DATE,date);
		values.put(KEY_SUBJECTNAME,subjectName);

		myDatabase.insert(TABLE_NAME, null, values);
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
