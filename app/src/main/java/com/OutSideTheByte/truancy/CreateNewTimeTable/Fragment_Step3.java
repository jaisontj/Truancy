package com.OutSideTheByte.truancy.CreateNewTimeTable;

import java.util.ArrayList;
import java.util.Calendar;

import com.OutSideTheByte.truancy.MainScreen;
import com.OutSideTheByte.truancy.R;
import com.OutSideTheByte.truancy.Database.*;
import com.OutSideTheByte.truancy.Notification.*;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TimePicker.OnTimeChangedListener;

public class Fragment_Step3 extends Fragment {

	ArrayList<String> subjectList;
	ArrayList<TextView> TimeTable_subject;
	ArrayList<TextView> TimeTable_time;

	ArrayList<Integer> subjectCountPerWeek;

	ArrayList<ArrayList<TextView>> TimeTable_subjectContainer = new ArrayList<ArrayList<TextView>>();
	ArrayList<ArrayList<TextView>> TimeTable_timeContainer = new ArrayList<ArrayList<TextView>>();

	/** Initializations **/
	Button b_next, b_back, b_add, b_addClass;
	LinearLayout entrySpace;
	TextView tv_title, tv_day;
	ScrollView sv;
	Animation fade_in;
	Spinner sp;
	TimePicker tp;

	int uniqueId = 0;
	int dayNum = 0;
	int selectedSubject;
	int hour, minute;

	PendingIntent pendingIntent;

	Boolean isEntryError;

	Boolean[] enteredDay = { false, false, false, false, false, false, false };

	/** String **/
	String[] day = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
			"Friday", "Saturday" };

	View fragmentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragmentView = inflater.inflate(R.layout.fragment_step3, container,
				false);

		tv_day = (TextView) fragmentView.findViewById(R.id.tv_step3_day);
		tv_title = (TextView) fragmentView.findViewById(R.id.tv_step3_title);

		sv = (ScrollView) fragmentView.findViewById(R.id.step3_scrollView);

		entrySpace = (LinearLayout) fragmentView
				.findViewById(R.id.step3_entrySpace);
		b_add = (Button) fragmentView.findViewById(R.id.button_step3_add);
		b_next = (Button) fragmentView.findViewById(R.id.button_step3_nextStep);
		b_back = (Button) fragmentView.findViewById(R.id.button_step3_prevStep);

		setViewForDay();

		return fragmentView;

	}

	public void classPickerBox() {

		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.timetable_pickerbox);
		dialog.setTitle("Pick Subject and Time");

		sp = (Spinner) dialog.findViewById(R.id.spinner_step3_subjectList);
		setSpinner();

		tp = (TimePicker) dialog.findViewById(R.id.step3_timePicker);
		setTimePicker();

		b_addClass = (Button) dialog.findViewById(R.id.button_addClass);
		b_addClass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				addEntry();

				dialog.dismiss();

			}
		});

		dialog.show();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				sv.fullScroll(View.FOCUS_DOWN);
			}
		}, 200);
	}

	public void setViewForDay() {

		tv_day.setText(day[dayNum]);
		tv_day.setTextColor(Color.BLACK);

		tv_title.setText("Enter the timetable for the day");
		tv_title.setTextColor(Color.WHITE);

		entrySpace.removeAllViews();

		// Initializing the ArrayList
		TimeTable_subject = new ArrayList<TextView>();
		TimeTable_time = new ArrayList<TextView>();
		TimeTable_subject.clear();
		TimeTable_time.clear();

		// Checking if the page has already been set before(CHeck for previous
		// day click)
		if (enteredDay[dayNum]) {
			Log.i("above addPrevious", "not first time ");
			addPreviousEntries();

		}

		// Listener For the AddEntry Button.
		b_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				classPickerBox();

			}
		});

		// Listeners on NextDay,PreviousDay and Finish Buttons.
		b_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dayNum == 0) {

					warning_dialog();

				} else {

					arrangeTimetable();

					if (enteredDay[dayNum]) {

						TimeTable_subjectContainer.set(dayNum,
								TimeTable_subject);
						TimeTable_timeContainer.set(dayNum, TimeTable_time);

					} else {
						enteredDay[dayNum] = true;
						TimeTable_subjectContainer.add(TimeTable_subject);
						TimeTable_timeContainer.add(TimeTable_time);

					}
					dayNum--;
					setViewForDay();
				}
			}
		});

		b_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				arrangeTimetable();

				if (enteredDay[dayNum]) {

					TimeTable_subjectContainer.set(dayNum, TimeTable_subject);
					TimeTable_timeContainer.set(dayNum, TimeTable_time);

				} else {
					enteredDay[dayNum] = true;
					TimeTable_subjectContainer.add(TimeTable_subject);
					TimeTable_timeContainer.add(TimeTable_time);

				}

				if (dayNum == 6) {
					if (uniqueId == 0) {
						tv_title.setText("This app is useless if you're free throughout the week");
						tv_title.setTextColor(Color.RED);
					} else
						updateDatabase();
				} else {
					dayNum++;
					setViewForDay();
				}

			}
		});

	}

	public void arrangeTimetable() {

		for (int i = 0; i < TimeTable_time.size(); i++) {

			String[] time1 = TimeTable_time.get(i).getText().toString()
					.split(":");
			String t1_part1 = time1[0];
			String t1_part2 = time1[1];
			int hour1 = Integer.parseInt(t1_part1);
			int min1 = Integer.parseInt(t1_part2);

			for (int j = i + 1; j < TimeTable_time.size(); j++) {

				String[] time2 = TimeTable_time.get(j).getText().toString()
						.split(":");
				String t2_part1 = time2[0];
				String t2_part2 = time2[1];
				int hour2 = Integer.parseInt(t2_part1);
				int min2 = Integer.parseInt(t2_part2);

				TextView tv = new TextView(getActivity());
				tv.setText(hour2 + ":" + min2);
				TextView tv2 = new TextView(getActivity());
				tv2.setText(hour1 + ":" + min1);

				TextView t = new TextView(getActivity());
				t.setText(TimeTable_subject.get(j).getText().toString());
				TextView t2 = new TextView(getActivity());
				t2.setText(TimeTable_subject.get(i).getText().toString());

				if (hour2 < hour1) {

					TimeTable_time.set(i, tv);
					TimeTable_time.set(j, tv2);
					TimeTable_subject.set(i, t);
					TimeTable_subject.set(j, t2);

					hour1 = hour2;
					min1 = min2;

				} else if (hour2 == hour1) {
					if (min2 < min1) {
						TimeTable_time.set(i, tv);
						TimeTable_time.set(j, tv2);
						TimeTable_subject.set(i, t);
						TimeTable_subject.set(j, t2);

						hour1 = hour2;
						min1 = min2;

					}
				}

			}
		}
	}

	/** Dialog To Display When CreateNewTimeTable button is clicked. **/
	public void warning_dialog() {

		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.warning_message_dialog);

		TextView warning_dialog_content1 = (TextView) dialog
				.findViewById(R.id.content1);

		warning_dialog_content1
				.setText("Going back to step 2 will erase all the current data from step 3");

		Button b_yes = (Button) dialog.findViewById(R.id.b_yes);
		b_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.createnewtt_container,
						new Fragment_Step2());
				fragmentTransaction.commit();

			}

		});

		Button b_no = (Button) dialog.findViewById(R.id.b_no);
		b_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

			}

		});

		dialog.show();

	}

	public void addPreviousEntries() {

		tv_day.setText(day[dayNum]);
		tv_day.setTextColor(Color.BLACK);

		for (int i = 0; i < TimeTable_subjectContainer.get(dayNum).size(); i++) {

			View inflatedView = getActivity().getLayoutInflater().inflate(
					R.layout.new_entry_subjectandtime, entrySpace);

			TextView tv_subjectName = (TextView) inflatedView
					.findViewById(R.id.button_subjectName);
			tv_subjectName.setText(TimeTable_subjectContainer.get(dayNum)
					.get(i).getText().toString());
			tv_subjectName.setId(uniqueId);
			TimeTable_subject.add(tv_subjectName);

			TextView tv_subjectTime = (TextView) inflatedView
					.findViewById(R.id.button_time);

			tv_subjectTime.setText(TimeTable_timeContainer.get(dayNum).get(i)
					.getText().toString());
			tv_subjectTime.setId(uniqueId + 200);

			TimeTable_time.add(tv_subjectTime);

			Button deleteEntry = (Button) inflatedView
					.findViewById(R.id.button_delete);
			deleteEntry.setId(uniqueId + 400);
			deleteEntry.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					deleteEntry(v);
					Log.i("Delete: ", "CAlled");

				}
			});

			uniqueId++;

			// Scrolling down each time a new entry is added.
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					sv.fullScroll(ScrollView.FOCUS_DOWN);
				}

			}, 200);
		}

	}

	public void addEntry() {

		isEntryError = false;

		for (int i = 0; i < TimeTable_time.size(); i++) {
			if (TimeTable_time.get(i).getText().toString()
					.equalsIgnoreCase(hour + ":" + minute)) {

				final TextView tv = ((TextView) ((LinearLayout) (TimeTable_time
						.get(i)).getParent()).getChildAt(0));
				tv.setTextColor(Color.RED);

				final LinearLayout ll = (LinearLayout) tv.getParent();
				((TextView) ((LinearLayout) (TimeTable_time.get(i)).getParent())
						.getChildAt(1)).setTextColor(Color.RED);
				((TextView) ((LinearLayout) (TimeTable_time.get(i)).getParent())
						.getChildAt(2)).setTextColor(Color.RED);
				((TextView) ((LinearLayout) (TimeTable_time.get(i)).getParent())
						.getChildAt(3)).setTextColor(Color.RED);

				// To Scroll to where the error is
				ll.requestFocus();
				final int x = ll.getHeight() * i;

				new Handler().post(new Runnable() {
					@Override
					public void run() {
						sv.smoothScrollTo(0, x);
					}
				});

				if (TimeTable_subject.get(i).getText().toString()
						.equalsIgnoreCase(subjectList.get(selectedSubject))) {

					tv_title.setText("Duplicate Class");
					tv_title.setTextColor(Color.RED);
				} else {

					tv_title.setText("Different Classes at the same time");
					tv_title.setTextColor(Color.RED);
				}
				isEntryError = true;
			} else {
				((TextView) ((LinearLayout) (TimeTable_time.get(i)).getParent())
						.getChildAt(0)).setTextColor(Color.BLACK);
				((TextView) ((LinearLayout) (TimeTable_time.get(i)).getParent())
						.getChildAt(1)).setTextColor(Color.BLACK);
				((TextView) ((LinearLayout) (TimeTable_time.get(i)).getParent())
						.getChildAt(2)).setTextColor(Color.BLACK);
				((TextView) ((LinearLayout) (TimeTable_time.get(i)).getParent())
						.getChildAt(3)).setTextColor(Color.BLACK);

			}
		}

		if (!isEntryError) {

			tv_title.setText("Enter the timetable for the day");
			tv_title.setTextColor(Color.WHITE);

			View inflatedView = getActivity().getLayoutInflater().inflate(
					R.layout.new_entry_subjectandtime, entrySpace);

			TextView tv_subjectName = (TextView) inflatedView
					.findViewById(R.id.button_subjectName);
			tv_subjectName.setText(subjectList.get(selectedSubject));
			tv_subjectName.setId(uniqueId);
			TimeTable_subject.add(tv_subjectName);

			TextView tv_subjectTime = (TextView) inflatedView
					.findViewById(R.id.button_time);

			tv_subjectTime.setText(hour + ":" + minute);
			tv_subjectTime.setId(uniqueId + 200);

			TimeTable_time.add(tv_subjectTime);

			Button deleteEntry = (Button) inflatedView
					.findViewById(R.id.button_delete);
			deleteEntry.setId(uniqueId + 400);
			deleteEntry.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					deleteEntry(v);

				}
			});

			uniqueId++;

			// Scrolling down each time a new entry is added.
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					sv.fullScroll(ScrollView.FOCUS_DOWN);
				}

			}, 200);
		}

	}

	public void deleteEntry(View view) {

		Log.i("Delete: ", "CAlled");
		Button deleteButton = (Button) view.findViewById(view.getId());
		LinearLayout subjectEntryContainer = (LinearLayout) deleteButton
				.getParent();

		TextView subName = (TextView) subjectEntryContainer.getChildAt(0);
		TextView subTime = (TextView) subjectEntryContainer.getChildAt(2);
		TimeTable_subject.remove(subName);
		TimeTable_time.remove(subTime);

		Log.i("Delete: ", "here");
		((LinearLayout) subjectEntryContainer.getParent())
				.removeView(subjectEntryContainer);

		uniqueId--;

	}

	public void setSpinnerList() {

		subjectList = new ArrayList<String>();
		subjectCountPerWeek = new ArrayList<Integer>();
		for (int i = 0; i < CreateNewTimeTable.SUBJECT_NAME.size(); i++) {

			subjectList.add((CreateNewTimeTable.SUBJECT_NAME.get(i)).getText()
					.toString());
			subjectCountPerWeek.add(0);
		}
	}

	public void setSpinner() {

		setSpinnerList();

		// Adds The SubjectLists to the Spinner.
		ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, subjectList);
		sp.setAdapter(adp);

		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				selectedSubject = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});
	}

	public void setTimePicker() {

		Calendar mcurrentTime = Calendar.getInstance();
		hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
		minute = mcurrentTime.get(Calendar.MINUTE);

		tp.setCurrentHour(hour);
		tp.setCurrentMinute(minute);

		tp.setIs24HourView(true);

		tp.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay,
					int minuteOfHour) {

				hour = hourOfDay;
				minute = minuteOfHour;

			}
		});
	}

	public void deleteDatabase() {

		// when create new timetable is clicked, the previous time table is
		// deleted
		Database_Timetable db = new Database_Timetable(getActivity());
		db.open();
		db.deleteAll();
		db.close();

		Database_SubjectList db2 = new Database_SubjectList(getActivity());
		db2.open();
		db2.deleteAll();
		db2.close();

		Database_UnAccountedClasses db3 = new Database_UnAccountedClasses(
				getActivity());
		db3.open();
		db3.deleteAll();
		db3.close();
	}

	public void cancelAllNotifs() {

		Database_Timetable dt = new Database_Timetable(getActivity());
		dt.open();

		Cursor cursor = dt.getAllRows();

		if (cursor.moveToFirst()) {
			do {
				// process the data
				int id = cursor.getInt(Database_Timetable.COL_ROWID);
				String subname = cursor
						.getString(Database_Timetable.COL_SUBJECTNAME);
				String day = cursor.getString(Database_Timetable.COL_DAY);
				String time = cursor.getString(Database_Timetable.COL_TIME);

				cancelNotif(id, subname, day, time);

			} while (cursor.moveToNext());
		}

		// close the cursor to avoid resource leak
		cursor.close();

		// always close the Database
		dt.close();

		deleteDatabase();
	}

	public void cancelNotif(int notif_id, String sub, String day, String time) {

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

		Intent myIntent = new Intent(getActivity(), NotificationReceiver.class);
		myIntent.putExtra("Sub_name", sub);
		myIntent.putExtra("idfornotif", notif_id);

		pendingIntent = PendingIntent.getBroadcast(getActivity(), notif_id,
				myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getActivity()
				.getSystemService(Context.ALARM_SERVICE);

		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();

	}

	public void updateDatabase() {

		cancelAllNotifs();

		Database_Timetable dt = new Database_Timetable(getActivity());

		for (int i = 0; i < TimeTable_subjectContainer.size(); i++) {
			for (int j = 0; j < TimeTable_subjectContainer.get(i).size(); j++) {
				dt.addTimetable(day[i], TimeTable_timeContainer.get(i).get(j)
						.getText().toString(), TimeTable_subjectContainer
						.get(i).get(j).getText().toString());

				for (int k = 0; k < subjectList.size(); k++) {
					if (TimeTable_subjectContainer.get(i).get(j).getText()
							.toString().equalsIgnoreCase(subjectList.get(k))) {
						int subCount = subjectCountPerWeek.get(k);
						++subCount;
						subjectCountPerWeek.set(k, subCount);

					}

				}

			}

		}
		Log.i("updateDone", "here");

		Database_SubjectList dsl = new Database_SubjectList(getActivity());

		for (int i = 0; i < subjectList.size(); i++) {
			dsl.addSubject(
					subjectList.get(i),
					0,
					0,
					CreateNewTimeTable.total_weeks * subjectCountPerWeek.get(i),
					CreateNewTimeTable.bunkLimit);

		}

		// To set the SharedPrefs
		MainScreen.isNotifOn = true;
		Editor editor = MainScreen.sharedpreferences.edit();
		editor.putBoolean(MainScreen.hasRunBefore, true);
		editor.putBoolean(MainScreen.setNotifOn, true);
		editor.commit();

		// Setting up Notifications
		Intent intent = new Intent(getActivity(),
				RepeatAlarmEveryWeekService.class);
		Log.i("start service", "RepeatEveryWeek");
		getActivity().startService(intent);

		MainScreen.newtimetableCreated = true;
		// Finish Activity
		getActivity().finish();

	}
}