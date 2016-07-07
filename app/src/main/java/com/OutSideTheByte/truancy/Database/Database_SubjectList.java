package com.OutSideTheByte.truancy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_SubjectList extends SQLiteOpenHelper {

	// Database Row details
	private final static String KEY_SUBJECTNAME = "Subject_Name";
	private final static String KEY_ROWID = "_Id";
	private final static String KEY_BUNKED = "Classes_Bunked";
	private final static String KEY_ATTENDED = "Classes_Attended";
	private final static String KEY_TOTAL = "Total_Classes";
	private final static String KEY_MAXBUNKS = "Attendance";

	public static final String[] ALL_KEYS = new String[] { KEY_ROWID,
			KEY_SUBJECTNAME, KEY_BUNKED, KEY_ATTENDED, KEY_TOTAL, KEY_MAXBUNKS };

	// Database and table details
	private final static String DATABASE_NAME = "Attendance_Details";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "Subject_List";

	// Field Numbers
	public static final int COL_ROWID = 0;
	public static final int COL_SUBJECTNAME = 1;
	public static final int COL_BUNKED = 2;
	public static final int COL_ATTENDED = 3;
	public static final int COL_TOTAL = 4;
	public static final int COL_MAXBUNKS = 5;

	// private final Context context;
	private SQLiteDatabase myDatabase;

	public Database_SubjectList(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// Creating a table
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_SUBJECTNAME
				+ " TEXT, " + KEY_BUNKED + " INTEGER, " + KEY_ATTENDED
				+ " INTEGER, " + KEY_TOTAL + " INTEGER, " + KEY_MAXBUNKS
				+ " REAL);"

		);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop table if exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Now re-create it
		onCreate(db);

	}

	public Database_SubjectList open() {
		myDatabase = this.getWritableDatabase();
		return this;
	}

	// Close the database connection.
	public void close() {
		myDatabase.close();
	}

	// Adding a new subject
	public void addSubject(String subject_name, int bunk_count,int attend_count,
			int total_classes,int max_bunks) {
		SQLiteDatabase myDatabase = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_SUBJECTNAME, subject_name); // Subject Name
		values.put(KEY_BUNKED, bunk_count); // Bunked Classes
		values.put(KEY_ATTENDED, attend_count); // attended Classes
		values.put(KEY_TOTAL, total_classes); // Limit
		values.put(KEY_MAXBUNKS, max_bunks); // bunk limit

		myDatabase.insert(TABLE_NAME, null, values);
		myDatabase.close();
	}

	// Update a subject
	public void updateSubject(String subject_name, int bunk_count, int attend_count,
			int total_classes,int max_bunks, String old_subject_name) {
		SQLiteDatabase myDatabase = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		
		values.clear();
		values.put(KEY_SUBJECTNAME, subject_name); // Subject Name
		values.put(KEY_BUNKED, bunk_count); // Bunked Classes
		values.put(KEY_ATTENDED, attend_count); // attended Classes
		values.put(KEY_TOTAL, total_classes); // Limit
		values.put(KEY_MAXBUNKS, max_bunks); // Bunked Percentage

		myDatabase.update(TABLE_NAME, values, KEY_SUBJECTNAME + "=?",
				new String[] { old_subject_name });

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

	// Get the Bunk Limit
	public Cursor getLimit(String subject_name) {
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE "
				+ KEY_SUBJECTNAME + " = ?";

		Cursor c = myDatabase.rawQuery(selectQuery,
				new String[] { subject_name });

		if (c != null) {
			c.moveToFirst();
		}
		return c;

	}
}
