package com.OutSideTheByte.truancy;

import java.util.Calendar;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.OutSideTheByte.truancy.Database.Database_Timetable;

public class Fragment_RightPage extends Fragment {

	Database_Timetable db;

	int uniqueId = 0;
	int currentDay, setDay;

	/** String **/
	String[] daysofweek = { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY",
			"THURSDAY", "FRIDAY", "SATURDAY" };

	/** Initialization **/
	Button b_sun, b_mon, b_tue, b_wed, b_thur, b_fri, b_sat;
	LinearLayout entrySpace, title1;
	TextView title2, tv_boxTitle;
	ScrollView sv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View fragmentView = inflater.inflate(
				R.layout.fragment_page_right, container, false);

		entrySpace = (LinearLayout) fragmentView
				.findViewById(R.id.right_entrySpace);

		sv = (ScrollView) fragmentView.findViewById(R.id.right_scrollView);
		ViewGroup.LayoutParams params = sv.getLayoutParams();

		params.height = 70 * MainScreen.Screenheight / 100;

		sv.setLayoutParams(params);

		tv_boxTitle = (TextView) fragmentView
				.findViewById(R.id.right_box_title);

		title1 = (LinearLayout) fragmentView
				.findViewById(R.id.right_sub_title1);
		title2 = (TextView) fragmentView.findViewById(R.id.right_sub_title2);

		sv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_SCROLL:
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					break;

				}
				Rect scrollBounds = new Rect();
				sv.getHitRect(scrollBounds);
				if (tv_boxTitle.getLocalVisibleRect(scrollBounds)) {
					title1.setVisibility(View.VISIBLE);
					title2.setVisibility(View.GONE);
				} else {

					title1.setVisibility(View.GONE);
					title2.setVisibility(View.VISIBLE);
					title2.setText(daysofweek[setDay]);

				}
				return false;

			}
		});

		b_sun = (Button) fragmentView.findViewById(R.id.button_sunday);
		b_sun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDay(0);
			}
		});

		b_mon = (Button) fragmentView.findViewById(R.id.button_monday);
		b_mon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDay(1);
			}
		});

		b_tue = (Button) fragmentView.findViewById(R.id.button_tuesday);
		b_tue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDay(2);
			}
		});

		b_wed = (Button) fragmentView.findViewById(R.id.button_wednesday);
		b_wed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDay(3);
			}
		});

		b_thur = (Button) fragmentView.findViewById(R.id.button_thursday);
		b_thur.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDay(4);
			}
		});

		b_fri = (Button) fragmentView.findViewById(R.id.button_friday);
		b_fri.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDay(5);
			}
		});

		b_sat = (Button) fragmentView.findViewById(R.id.button_saturday);
		b_sat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setDay(6);
			}
		});

		// To set the timetable to the current day
		Calendar calendar = Calendar.getInstance();
		currentDay = calendar.get(Calendar.DAY_OF_WEEK);
		currentDay--;
		setDay(currentDay);

		setDayColor();

		return fragmentView;
	}

	public void setDayColor() {
		if (currentDay == 0) {
			b_sun.setTextColor(Color.RED);
		} else {
			b_sun.setTextColor(Color.BLACK);
		}
		if (currentDay == 1) {
			b_mon.setTextColor(Color.RED);
		} else {
			b_mon.setTextColor(Color.BLACK);
		}
		if (currentDay == 2) {
			b_tue.setTextColor(Color.RED);
		} else {
			b_tue.setTextColor(Color.BLACK);
		}
		if (currentDay == 3) {
			b_wed.setTextColor(Color.RED);
		} else {
			b_wed.setTextColor(Color.BLACK);
		}
		if (currentDay == 4) {
			b_thur.setTextColor(Color.RED);
		} else {
			b_thur.setTextColor(Color.BLACK);
		}
		if (currentDay == 5) {
			b_fri.setTextColor(Color.RED);
		} else {
			b_fri.setTextColor(Color.BLACK);
		}
		if (currentDay == 6) {
			b_sat.setTextColor(Color.RED);
		} else {
			b_sat.setTextColor(Color.BLACK);
		}
	}

	public void setDay(int dayNum) {

		setDay = dayNum;
		if (dayNum == currentDay) {
			tv_boxTitle.setText("TODAY (" + daysofweek[dayNum] + ")");
		} else
			tv_boxTitle.setText("" + daysofweek[dayNum]);

		entrySpace.removeAllViews();
		Boolean classToday = false;

		if (dayNum == 0) {
			b_sun.setTextSize(26);
		} else {
			b_sun.setTextSize(18);
		}
		if (dayNum == 1) {
			b_mon.setTextSize(26);
		} else {
			b_mon.setTextSize(18);
		}
		if (dayNum == 2) {
			b_tue.setTextSize(26);
		} else {
			b_tue.setTextSize(18);
		}
		if (dayNum == 3) {
			b_wed.setTextSize(26);
		} else {
			b_wed.setTextSize(18);
		}
		if (dayNum == 4) {
			b_thur.setTextSize(26);
		} else {
			b_thur.setTextSize(18);
		}
		if (dayNum == 5) {
			b_fri.setTextSize(26);
		} else {
			b_fri.setTextSize(18);
		}
		if (dayNum == 6) {
			b_sat.setTextSize(26);
		} else {
			b_sat.setTextSize(18);
		}

		db = new Database_Timetable(getActivity());

		db.open();

		Cursor cursor = db.getAllRows();
		if (cursor.moveToFirst()) {
			do {

				String subname = cursor
						.getString(Database_Timetable.COL_SUBJECTNAME);
				String day = cursor.getString(Database_Timetable.COL_DAY);
				String time = cursor.getString(Database_Timetable.COL_TIME);
				String status = cursor.getString(Database_Timetable.COL_STATUS);

				if (daysofweek[dayNum].equalsIgnoreCase(day)) {

					classToday = true;

					String[] parts = time.split(":");
					String part1 = parts[0];
					String part2 = parts[1];
					int hour = Integer.parseInt(part1);
					int min = Integer.parseInt(part2);

					String shour;
					String smin;

					if (hour / 10 == 0) {
						shour = "0" + hour;
					} else
						shour = "" + hour;

					if (min / 10 == 0) {
						smin = "0" + min;
					} else
						smin = "" + min;

					View newView = getActivity().getLayoutInflater().inflate(
							R.layout.new_entry_viewtimetable, entrySpace);

					TextView s = (TextView) newView.findViewById(R.id.Subject);

					s.setId(uniqueId);
					s.setText(subname);

					TextView t = (TextView) newView.findViewById(R.id.Time);
					t.setId(uniqueId + 200);
					t.setText(shour + ":" + smin);

					TextView b = (TextView) newView.findViewById(R.id.state);
					b.setId(uniqueId + 400);
					if (status.equalsIgnoreCase("b"))
						b.setBackgroundResource(R.drawable.bunk_button_clicked);
					else if (status.equalsIgnoreCase("a"))
						b.setBackgroundResource(R.drawable.attend_button_clicked);
					else if (status.equalsIgnoreCase("h"))
						b.setBackgroundResource(R.drawable.holiday_button_clicked);
					else if (status.equalsIgnoreCase("e"))
						b.setBackgroundResource(R.drawable.unknown_clicked);

					uniqueId++;
				}

			} while (cursor.moveToNext());
		}

		// close the cursor to avoid resource leak
		cursor.close();
		db.close();

		if (!classToday) {

			View newView = getActivity().getLayoutInflater().inflate(
					R.layout.new_entry_viewtimetable, entrySpace);

			TextView s = (TextView) newView.findViewById(R.id.Subject);

			s.setId(uniqueId);
			s.setText("HOLIDAY");
			s.setTextColor(Color.GRAY);
			s.setTextSize(MainScreen.Screenheight / 70);

			ViewGroup.LayoutParams params = s.getLayoutParams();

			params.height = MainScreen.Screenheight / 2;
			params.width = (9 * MainScreen.Screenwidth) / 10;

			s.setLayoutParams(params);

			TextView t = (TextView) newView.findViewById(R.id.Time);
			t.setId(uniqueId + 200);
			t.setVisibility(View.GONE);

			TextView b = (TextView) newView.findViewById(R.id.state);
			b.setId(uniqueId + 400);
			b.setVisibility(View.GONE);

		}

	}

}