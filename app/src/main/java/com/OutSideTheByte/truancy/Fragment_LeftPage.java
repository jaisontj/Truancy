package com.OutSideTheByte.truancy;

import java.util.Calendar;
import com.OutSideTheByte.truancy.Database.*;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Fragment_LeftPage extends Fragment {

	Button b_all, b_missed;

	Button b_sun, b_mon, b_tue, b_wed, b_thur, b_fri, b_sat;
	LinearLayout entrySpace, leftBoxTitle, title1, title2;
	TextView tv_dayTitle, title2_day;
	Button b_title2_all, b_title2_missed;

	ScrollView sv;

	Database_UnAccountedClasses duc;
	Database_SubjectList dsl;
	Database_Timetable dt;

	View fragmentView;
	int uniqueId = 0;
	int currentDay, setDay;

	String[] dayofweek = { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY",
			"THURSDAY", "FRIDAY", "SATURDAY" };

	Fragment_MiddlePage fmiddle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragmentView = inflater.inflate(R.layout.fragment_page_left, container,
				false);

		// To set the timetable to the current day
		Calendar calendar = Calendar.getInstance();
		currentDay = calendar.get(Calendar.DAY_OF_WEEK);
		currentDay--;

		sv = (ScrollView) fragmentView.findViewById(R.id.left_scrollView);
		ViewGroup.LayoutParams params = sv.getLayoutParams();

		params.height = 70 * MainScreen.Screenheight / 100;

		sv.setLayoutParams(params);

		title1 = (LinearLayout) fragmentView.findViewById(R.id.left_sub_title1);
		title2 = (LinearLayout) fragmentView.findViewById(R.id.left_sub_title2);
		title2_day = (TextView) fragmentView
				.findViewById(R.id.tv_left_sub_title2_day);
		b_title2_all = (Button) fragmentView
				.findViewById(R.id.button_left_sub_title2_all);
		b_title2_missed = (Button) fragmentView
				.findViewById(R.id.button_left_sub_title2_missed);

		tv_dayTitle = (TextView) fragmentView
				.findViewById(R.id.tv_left_box_title);

		entrySpace = (LinearLayout) fragmentView
				.findViewById(R.id.left_entrySpace);

		leftBoxTitle = (LinearLayout) fragmentView
				.findViewById(R.id.left_box_title);

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
				if (leftBoxTitle.getLocalVisibleRect(scrollBounds)) {
					title1.setVisibility(View.VISIBLE);
					title2.setVisibility(View.GONE);
				} else {
					title1.setVisibility(View.GONE);
					title2.setVisibility(View.VISIBLE);
					title2_day.setText(dayofweek[setDay]);

					if (MainScreen.leftPage_num == 1) {
						b_title2_missed.setTextSize(20);
						b_title2_all.setTextSize(14);
					} else if (MainScreen.leftPage_num == 0) {
						b_title2_missed.setTextSize(14);
						b_title2_all.setTextSize(20);
					}

				}
				Log.i("Scrolled:", "here");
				return false;

			}
		});

		b_sun = (Button) fragmentView.findViewById(R.id.button_sunday);
		b_sun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setDay(0);
			}
		});

		b_mon = (Button) fragmentView.findViewById(R.id.button_monday);
		b_mon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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

		b_all = (Button) fragmentView.findViewById(R.id.button_left_All);
		b_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewAll();
			}
		});

		b_missed = (Button) fragmentView.findViewById(R.id.button_left_Missed);
		b_missed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewMissed();
			}
		});

		b_title2_missed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewMissed();

			}
		});

		b_title2_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewAll();

			}
		});

		setDayColor();
		setDay(currentDay);
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
		tv_dayTitle.setText("" + dayofweek[dayNum]);
		tv_dayTitle.setTextSize(MainScreen.Screenheight / 100);

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

		if (MainScreen.leftPage_num == 0)
			viewAll();
		else if (MainScreen.leftPage_num == 1)
			viewMissed();
	}

	public void viewMissed() {

		Boolean dataPresent = false;

		title1.setVisibility(View.VISIBLE);
		title2.setVisibility(View.GONE);

		MainScreen.leftPage_num = 1;
		b_missed.setTextSize(20);
		b_all.setTextSize(14);

		entrySpace.removeAllViews();

		// Check the DB for previous records.
		duc = new Database_UnAccountedClasses(getActivity());
		duc.open();
		Cursor cursor = duc.getAllRows();

		if (cursor.moveToFirst()) {
			do {

				String classId = cursor
						.getString(Database_UnAccountedClasses.COL_CLASSID);
				String subjectName = cursor
						.getString(Database_UnAccountedClasses.COL_SUBJECTNAME);
				String day = cursor
						.getString(Database_UnAccountedClasses.COL_DAY);
				String time = cursor
						.getString(Database_UnAccountedClasses.COL_TIME);
				String date = cursor
						.getString(Database_UnAccountedClasses.COL_DATE);

				if (day.equalsIgnoreCase(dayofweek[setDay])
						&& !date.equalsIgnoreCase("")) {

					dataPresent = true;

					View newEntry = getActivity().getLayoutInflater().inflate(
							R.layout.new_entry_leftpage, entrySpace);

					Button b_subjectName = (Button) newEntry
							.findViewById(R.id.button_left_subjectName);
					b_subjectName.setId(uniqueId);
					b_subjectName.setText(subjectName);

					b_subjectName.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							bunk_attend_holiday(v, 0);
						}
					});

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

					Button b_time = (Button) newEntry
							.findViewById(R.id.button_left_time);
					b_time.setId(uniqueId);
					b_time.setText(shour + ":" + smin);

					b_time.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							bunk_attend_holiday(v, 0);
						}
					});

					Button b_date = (Button) newEntry
							.findViewById(R.id.button_left_date);
					b_date.setId(uniqueId);
					b_date.setText(date);

					b_date.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							bunk_attend_holiday(v, 0);
						}
					});

					TextView tv = (TextView) newEntry
							.findViewById(R.id.id_entryLeftPage);
					tv.setId(uniqueId + 200);
					tv.setText(classId);

					uniqueId++;
				}

			} while (cursor.moveToNext());
		}
		cursor.close();
		duc.close();

		// CHeck the current Week.

		String subname = "";
		String day = "";
		String time = "";
		String status = "";
		String notifdate = "";
		int daynum = 0;
		int rowId = 0;
		dt = new Database_Timetable(getActivity());
		dt.open();

		Cursor c_dt = dt.getAllRows();
		if (c_dt.moveToFirst()) {
			do {

				rowId = c_dt.getInt(Database_Timetable.COL_ROWID);
				subname = c_dt.getString(Database_Timetable.COL_SUBJECTNAME);
				day = c_dt.getString(Database_Timetable.COL_DAY);
				time = c_dt.getString(Database_Timetable.COL_TIME);
				status = c_dt.getString(Database_Timetable.COL_STATUS);
				notifdate = c_dt.getString(Database_Timetable.COL_NOTIFDATE);

				for (int j = 0; j < 7; j++) {
					if (dayofweek[j].equalsIgnoreCase(day)) {
						daynum = j + 1;
					}
				}
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

				// Create a new calendar set to the date chosen
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(System.currentTimeMillis());
				c.set(Calendar.HOUR_OF_DAY, hour);
				c.set(Calendar.MINUTE, min);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.DAY_OF_WEEK, daynum);

				// checking if time has passed
				Calendar cCheck = Calendar.getInstance();
				cCheck.setTimeInMillis(System.currentTimeMillis());

				// For UnAccounted Classes
				if (cCheck.getTimeInMillis() > c.getTimeInMillis()) {
					if ("E".equalsIgnoreCase(status)
							&& !notifdate.equalsIgnoreCase("")) {

						String id = "" + rowId;

						if (dayofweek[setDay].equalsIgnoreCase(day)) {

							dataPresent = true;

							View newEntry = getActivity().getLayoutInflater()
									.inflate(R.layout.new_entry_leftpage,
											entrySpace);

							Button b_subjectName = (Button) newEntry
									.findViewById(R.id.button_left_subjectName);
							b_subjectName.setId(uniqueId);
							b_subjectName.setText(subname);

							b_subjectName
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											bunk_attend_holiday(v, 1);
										}
									});

							Button b_time = (Button) newEntry
									.findViewById(R.id.button_left_time);
							b_time.setId(uniqueId + 100);
							b_time.setText(shour + ":" + smin);

							b_time.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									bunk_attend_holiday(v, 1);
								}
							});

							Button b_date = (Button) newEntry
									.findViewById(R.id.button_left_date);
							b_date.setId(uniqueId + 200);
							b_date.setText(notifdate);

							b_date.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									bunk_attend_holiday(v, 1);
								}
							});

							TextView tv = (TextView) newEntry
									.findViewById(R.id.id_entryLeftPage);
							tv.setId(uniqueId + 300);
							tv.setText(id);

							uniqueId++;

						}
					}

				}
			} while (c_dt.moveToNext());
		}

		// close the cursor to avoid resource leak
		c_dt.close();
		dt.close();

		if (!dataPresent) {
			View newEntry = getActivity().getLayoutInflater().inflate(
					R.layout.new_entry_leftpage, entrySpace);

			TextView tv = (TextView) newEntry
					.findViewById(R.id.id_entryLeftPage);
			tv.setVisibility(View.VISIBLE);
			tv.setId(uniqueId);
			tv.setText("NO MISSED NOTIFICATIONS");
			tv.setTextColor(Color.GRAY);
			tv.setTextSize(MainScreen.Screenheight / 70);

			ViewGroup.LayoutParams params = tv.getLayoutParams();

			params.height = MainScreen.Screenheight / 2;

			tv.setLayoutParams(params);

			Button b_subjectName = (Button) newEntry
					.findViewById(R.id.button_left_subjectName);
			b_subjectName.setVisibility(View.GONE);
			Button b_time = (Button) newEntry
					.findViewById(R.id.button_left_time);
			b_time.setVisibility(View.GONE);
			Button b_date = (Button) newEntry
					.findViewById(R.id.button_left_date);
			b_date.setVisibility(View.GONE);

		}

	}

	public void bunk_attend_holiday(View view, final int type) {

		Button button = (Button) view.findViewById(view.getId());
		final LinearLayout viewContainer = (LinearLayout) button.getParent();

		TextView tv_id = (TextView) viewContainer.getChildAt(6);
		final int classId = Integer.parseInt(tv_id.getText().toString().trim());

		Button bunk = (Button) viewContainer.getChildAt(3);
		if (bunk.getVisibility() == View.GONE) {
			bunk.setVisibility(View.VISIBLE);
			MainScreen.viewPager.setPagingEnabled(false);
		} else {
			bunk.setVisibility(View.GONE);
			MainScreen.viewPager.setPagingEnabled(true);
		}
		bunk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainScreen.viewPager.setPagingEnabled(true);
				((LinearLayout) viewContainer.getParent())
						.removeView(viewContainer);
				updateCountinDB(classId, 0);
				if (type == 1) {
					updateStatusinDB(classId, 0);
				}

			}
		});
		Button attend = (Button) viewContainer.getChildAt(4);
		if (attend.getVisibility() == View.GONE)
			attend.setVisibility(View.VISIBLE);
		else
			attend.setVisibility(View.GONE);
		attend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainScreen.viewPager.setPagingEnabled(true);
				((LinearLayout) viewContainer.getParent())
						.removeView(viewContainer);
				updateCountinDB(classId, 1);
				if (type == 1) {
					updateStatusinDB(classId, 1);
				}
			}
		});
		Button noClass = (Button) viewContainer.getChildAt(5);
		if (noClass.getVisibility() == View.GONE)
			noClass.setVisibility(View.VISIBLE);
		else
			noClass.setVisibility(View.GONE);
		noClass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainScreen.viewPager.setPagingEnabled(true);
				((LinearLayout) viewContainer.getParent())
						.removeView(viewContainer);
				updateCountinDB(classId, 2);
				if (type == 1) {
					updateStatusinDB(classId, 2);
				}
			}
		});

	}

	public void updateStatusinDB(int id, int choice) {

		String subName = "";
		String day = "";
		String time = "";
		String status = "";
		String notifdate = "";
		String notifStatus = "";
		int rowId = 0;

		dt = new Database_Timetable(getActivity());
		dt.open();
		Cursor c_dt = dt.getRow(id);
		if (c_dt.moveToFirst()) {
			do {
				rowId = c_dt.getInt(Database_Timetable.COL_ROWID);
				subName = c_dt.getString(Database_Timetable.COL_SUBJECTNAME);
				day = c_dt.getString(Database_Timetable.COL_DAY);
				time = c_dt.getString(Database_Timetable.COL_TIME);
				status = c_dt.getString(Database_Timetable.COL_STATUS);
				notifStatus = c_dt
						.getString(Database_Timetable.COL_NOTIFSTATUS);
				notifdate = c_dt.getString(Database_Timetable.COL_NOTIFDATE);
			} while (c_dt.moveToNext());
		}
		c_dt.close();

		if (choice == 0) {
			status = "B";
		} else if (choice == 1) {
			status = "A";
		} else if (choice == 2) {
			status = "H";
		} else if (choice == 4) {
			notifStatus = "C";
		} else if (choice == 5) {
			notifStatus = "S";
		}

		dt.updateRow(rowId, day, time, subName, status, notifStatus, notifdate);

		dt.close();

	}

	public void updateCountinDB(int id, int choice) {

		String subName = "";

		dt = new Database_Timetable(getActivity());
		dt.open();
		Cursor c_dt = dt.getRow(id);
		if (c_dt.moveToFirst()) {
			do {
				subName = c_dt.getString(Database_Timetable.COL_SUBJECTNAME);
			} while (c_dt.moveToNext());
		}
		c_dt.close();
		dt.close();

		int totalclasses = 0, bunkCount = 0, attendCount = 0, maxBunks = 0;

		dsl = new Database_SubjectList(getActivity());
		dsl.open();
		Cursor c_dsl = dsl.getLimit(subName);
		if (c_dsl.moveToFirst()) {
			do {
				subName = c_dsl.getString(Database_SubjectList.COL_SUBJECTNAME);
				String bunk_count = c_dsl
						.getString(Database_SubjectList.COL_BUNKED);
				String attend_count = c_dsl
						.getString(Database_SubjectList.COL_ATTENDED);
				String total_classes = c_dsl
						.getString(Database_SubjectList.COL_TOTAL);
				String max_bunks = c_dsl
						.getString(Database_SubjectList.COL_MAXBUNKS);

				maxBunks = Integer.parseInt(max_bunks);
				attendCount = Integer.parseInt(attend_count);
				bunkCount = Integer.parseInt(bunk_count);
				totalclasses = Integer.parseInt(total_classes);
			} while (c_dsl.moveToNext());
		}

		c_dsl.close();

		if (choice == 0) {
			bunkCount++;
		} else if (choice == 1) {
			attendCount++;
		} else if (choice == 2) {
			totalclasses--;
		}

		dsl.updateSubject(subName, bunkCount, attendCount, totalclasses,
				maxBunks, subName);
		dsl.close();
	}

	public void viewAll() {

		Boolean dataPresent = false;

		title1.setVisibility(View.VISIBLE);
		title2.setVisibility(View.GONE);

		MainScreen.leftPage_num = 0;
		b_missed.setTextSize(14);
		b_all.setTextSize(20);

		entrySpace.removeAllViews();

		if (MainScreen.isNotifOn) {

			String subname = "";
			String day = "";
			String time = "";
			int daynum = 0;
			dt = new Database_Timetable(getActivity());
			dt.open();

			Cursor c_dt = dt.getAllRows();
			if (c_dt.moveToFirst()) {
				do {

					final int rowId = c_dt.getInt(Database_Timetable.COL_ROWID);
					subname = c_dt
							.getString(Database_Timetable.COL_SUBJECTNAME);
					day = c_dt.getString(Database_Timetable.COL_DAY);
					time = c_dt.getString(Database_Timetable.COL_TIME);
					final String status = c_dt
							.getString(Database_Timetable.COL_NOTIFSTATUS);

					for (int j = 0; j < 7; j++) {
						if (dayofweek[j].equalsIgnoreCase(day)) {
							daynum = j + 1;
						}
					}
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

					// Create a new calendar set to the date chosen
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(System.currentTimeMillis());
					c.set(Calendar.HOUR_OF_DAY, hour);
					c.set(Calendar.MINUTE, min);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.DAY_OF_WEEK, daynum);

					// checking if time has passed
					Calendar cCheck = Calendar.getInstance();
					cCheck.setTimeInMillis(System.currentTimeMillis());

					// For UnAccounted Classes
					if (cCheck.getTimeInMillis() < c.getTimeInMillis()) {
						if ("S".equalsIgnoreCase(status)
								|| "C".equalsIgnoreCase(status)) {

							if (dayofweek[setDay].equalsIgnoreCase(day)) {

								String id = "" + rowId;

								dataPresent = true;

								View newEntry = getActivity()
										.getLayoutInflater().inflate(
												R.layout.new_entry_leftpage,
												entrySpace);

								Button b_subject = (Button) newEntry
										.findViewById(R.id.button_left_subjectName);
								Button b_time = (Button) newEntry
										.findViewById(R.id.button_left_time);
								Button b_date = (Button) newEntry
										.findViewById(R.id.button_left_date);
								b_date.setId(uniqueId + 200);
								b_date.setVisibility(View.GONE);

								b_subject.setId(uniqueId);
								b_subject.setText(subname);

								b_subject
										.setOnLongClickListener(new OnLongClickListener() {

											@Override
											public boolean onLongClick(View v) {

												if ("C".equalsIgnoreCase(status))
													updateStatusinDB(rowId, 5);
												else
													updateStatusinDB(rowId, 4);

												viewAll();

												return true;
											}
										});

								b_time.setId(uniqueId + 100);
								b_time.setText(shour + ":" + smin);

								b_time.setOnLongClickListener(new OnLongClickListener() {

									@Override
									public boolean onLongClick(View v) {

										if ("C".equalsIgnoreCase(status))
											updateStatusinDB(rowId, 5);
										else
											updateStatusinDB(rowId, 4);

										viewAll();

										return true;
									}
								});

								TextView tv = (TextView) newEntry
										.findViewById(R.id.id_entryLeftPage);
								tv.setId(uniqueId + 300);
								tv.setText(id);

								if ("C".equalsIgnoreCase(status)) {
									b_time.setTextColor(Color.GRAY);
									b_subject.setTextColor(Color.GRAY);
								} else {
									b_subject.setTextColor(Color.BLACK);
									b_time.setTextColor(Color.BLACK);

								}

								uniqueId++;

							}
						}

					}
				} while (c_dt.moveToNext());
			}

			// close the cursor to avoid resource leak
			c_dt.close();
			dt.close();

			if (!dataPresent) {
				View newEntry = getActivity().getLayoutInflater().inflate(
						R.layout.new_entry_leftpage, entrySpace);

				TextView tv = (TextView) newEntry
						.findViewById(R.id.id_entryLeftPage);
				tv.setVisibility(View.VISIBLE);
				tv.setId(uniqueId);
				tv.setText("NO NOTIFICATIONS ");
				tv.setTextColor(Color.GRAY);
				tv.setTextSize(MainScreen.Screenheight / 70);

				ViewGroup.LayoutParams params = tv.getLayoutParams();

				params.height = MainScreen.Screenheight / 2;

				tv.setLayoutParams(params);

				Button b_subjectName = (Button) newEntry
						.findViewById(R.id.button_left_subjectName);
				b_subjectName.setVisibility(View.GONE);
				Button b_time = (Button) newEntry
						.findViewById(R.id.button_left_time);
				b_time.setVisibility(View.GONE);
				Button b_date = (Button) newEntry
						.findViewById(R.id.button_left_date);
				b_date.setVisibility(View.GONE);

			}

		} else {
			View newEntry = getActivity().getLayoutInflater().inflate(
					R.layout.new_entry_leftpage, entrySpace);

			TextView tv = (TextView) newEntry
					.findViewById(R.id.id_entryLeftPage);
			tv.setVisibility(View.VISIBLE);
			tv.setId(uniqueId);
			tv.setText("NOTIFICATIONS SWITCHED OFF");
			tv.setTextColor(Color.GRAY);
			tv.setTextSize(MainScreen.Screenheight / 70);

			ViewGroup.LayoutParams params = tv.getLayoutParams();

			params.height = MainScreen.Screenheight / 2;

			tv.setLayoutParams(params);

			Button b_subjectName = (Button) newEntry
					.findViewById(R.id.button_left_subjectName);
			b_subjectName.setVisibility(View.GONE);
			Button b_time = (Button) newEntry
					.findViewById(R.id.button_left_time);
			b_time.setVisibility(View.GONE);
			Button b_date = (Button) newEntry
					.findViewById(R.id.button_left_date);
			b_date.setVisibility(View.GONE);
		}

	}

}