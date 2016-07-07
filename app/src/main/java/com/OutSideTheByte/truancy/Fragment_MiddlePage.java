package com.OutSideTheByte.truancy;

import java.util.ArrayList;

import com.OutSideTheByte.truancy.Settings.*;
import com.OutSideTheByte.truancy.Database.Database_SubjectList;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Fragment_MiddlePage extends Fragment {

	/** View Initializations **/
	View fragmentView;

	/** Strings **/
	ArrayList<String> subjectList;

	/** Database **/
	Database_SubjectList dsl;

	int settings_num = 1;
	int selectedSubject = 0;

	Button b_settings, b_back, b_subjectlist, b_settings1, b_settings2,
			b_settings3, b_settings4, b_attendancePercent, b_classesBunked,
			b_classesAttended, b_classesHeld, b_editMain, b_doneEdit,
			b_addbunked, b_subBunked, b_addAttended, b_subAttended;
	RelativeLayout middleBox, middleBox_main, middleBox_editMain;
	LinearLayout middleBox_settings, attendance, titleBar, settings_body,
			subjects_body;
	TextView tv_title, tv_attendancePercent, tv_classesBunked,
			tv_classesAttended, tv_classesHeld, tv_bunksLeft, tv_bunked,
			tv_attended, tv_bunkedTitle, tv_attendedTitle;
	ScrollView middleBox_subjects;

	String attendancePercent = "";
	String classesAttended = "";
	String bunksLeft = "";
	String classesBunked = "";
	String classesHeld = "";

	int attended, bunked;

	public static TextView tv_settings_subtitle;

	// Animation Stuff
	Handler handler, handler2, h;
	Runnable runnable, runnable2, r;
	int tempHeight = 0, tempHeight_box = 0;;
	Boolean animationDone = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragmentView = inflater.inflate(R.layout.fragment_page_middle,
				container, false);

		// Title Bar.
		titleBar = (LinearLayout) fragmentView
				.findViewById(R.id.middle_titleBar);
		tv_title = (TextView) fragmentView.findViewById(R.id.middle_title);
		b_back = (Button) fragmentView.findViewById(R.id.button_middle_back);
		b_subjectlist = (Button) fragmentView
				.findViewById(R.id.button_middle_subjectList);
		b_settings = (Button) fragmentView
				.findViewById(R.id.button_middle_settings);

		// Attendance Layout
		attendance = (LinearLayout) fragmentView
				.findViewById(R.id.totalAttendance);
		b_attendancePercent = (Button) fragmentView
				.findViewById(R.id.button_middle_attendancePercent);
		b_attendancePercent.setTextSize(MainScreen.Screenwidth / 10);
		tv_attendancePercent = (TextView) fragmentView
				.findViewById(R.id.tv_middle_attendacePercent);
		tv_attendancePercent.setTextSize(MainScreen.Screenwidth / 60);

		// Middle Box
		middleBox = (RelativeLayout) fragmentView.findViewById(R.id.middleBox);

		// Middle Box Main.
		middleBox_main = (RelativeLayout) fragmentView
				.findViewById(R.id.middleBox_main);

		b_editMain = (Button) fragmentView.findViewById(R.id.button_edit);

		b_classesAttended = (Button) fragmentView
				.findViewById(R.id.button_middle_classesAttended);
		b_classesAttended.setTextSize(MainScreen.Screenwidth / 25);
		tv_classesAttended = (TextView) fragmentView
				.findViewById(R.id.tv_middle_classesAttended);
		tv_classesAttended.setTextSize(MainScreen.Screenwidth / 70);

		b_classesBunked = (Button) fragmentView
				.findViewById(R.id.button_middle_classesBunked);
		b_classesBunked.setTextSize(MainScreen.Screenwidth / 25);
		tv_classesBunked = (TextView) fragmentView
				.findViewById(R.id.tv_middle_classesBunked);
		tv_classesBunked.setTextSize(MainScreen.Screenwidth / 70);

		b_classesHeld = (Button) fragmentView
				.findViewById(R.id.button_middle_classesHeld);
		b_classesHeld.setTextSize(MainScreen.Screenwidth / 25);
		tv_classesHeld = (TextView) fragmentView
				.findViewById(R.id.tv_middle_classesHeld);
		tv_classesHeld.setTextSize(MainScreen.Screenwidth / 70);

		tv_bunksLeft = (TextView) fragmentView
				.findViewById(R.id.tv_middle_bunksLeft);
		tv_bunksLeft.setTextSize(MainScreen.Screenwidth / 50);

		// Middle Box Edit Main
		middleBox_editMain = (RelativeLayout) fragmentView
				.findViewById(R.id.middleBox_editMain);
		b_doneEdit = (Button) fragmentView.findViewById(R.id.button_doneEdit);
		b_addbunked = (Button) fragmentView.findViewById(R.id.button_addBunked);
		b_addAttended = (Button) fragmentView
				.findViewById(R.id.button_addAttended);
		b_subBunked = (Button) fragmentView.findViewById(R.id.button_subBunked);
		b_subAttended = (Button) fragmentView
				.findViewById(R.id.button_subAttended);
		tv_attended = (TextView) fragmentView
				.findViewById(R.id.tv_attendedValue);
		tv_bunked = (TextView) fragmentView.findViewById(R.id.tv_bunkedValue);

		tv_bunkedTitle = (TextView) fragmentView.findViewById(R.id.tv_bunked);
		tv_attendedTitle = (TextView) fragmentView
				.findViewById(R.id.tv_attended);

		b_addbunked.setTextSize(MainScreen.Screenwidth / 40);
		b_subBunked.setTextSize(MainScreen.Screenwidth / 40);
		b_addAttended.setTextSize(MainScreen.Screenwidth / 40);
		b_subAttended.setTextSize(MainScreen.Screenwidth / 40);
		tv_bunked.setTextSize(MainScreen.Screenwidth / 20);
		tv_attended.setTextSize(MainScreen.Screenwidth / 20);
		tv_bunkedTitle.setTextSize(MainScreen.Screenwidth / 50);
		tv_attendedTitle.setTextSize(MainScreen.Screenwidth / 50);

		// Middle Box settings.
		middleBox_settings = (LinearLayout) fragmentView
				.findViewById(R.id.middleBox_settings);
		tv_settings_subtitle = (TextView) fragmentView
				.findViewById(R.id.tv_settings_sub_title);
		b_settings1 = (Button) fragmentView.findViewById(R.id.button_settings1);
		b_settings2 = (Button) fragmentView.findViewById(R.id.button_settings2);
		b_settings3 = (Button) fragmentView.findViewById(R.id.button_settings3);
		b_settings4 = (Button) fragmentView.findViewById(R.id.button_settings4);
		settings_body = (LinearLayout) fragmentView
				.findViewById(R.id.settings_body);

		// Middle Box Subjects.
		middleBox_subjects = (ScrollView) fragmentView
				.findViewById(R.id.middleBox_subjects);
		subjects_body = (LinearLayout) fragmentView
				.findViewById(R.id.middleBox_subjects_entrySpace);

		b_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showSettings();
			}
		});

		b_subjectlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showSubjects();
			}
		});

		b_editMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showEditMain();
			}
		});

		showMain();

		return fragmentView;
	}

	public void showEditMain() {

		tv_title.setText("EDIT");

		middleBox_main.setVisibility(View.GONE);
		middleBox_settings.setVisibility(View.GONE);
		middleBox_subjects.setVisibility(View.GONE);
		middleBox_editMain.setVisibility(View.VISIBLE);
		attendance.setVisibility(View.INVISIBLE);

		b_doneEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				saveEdit();
				showMain();
			}
		});

		tv_attended.setText(classesAttended);
		tv_bunked.setText(classesBunked);

		attended = Integer.parseInt(tv_attended.getText().toString());
		bunked = Integer.parseInt(tv_bunked.getText().toString());

		b_addAttended.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				attended++;
				tv_attended.setText("" + attended);
			}
		});

		b_subAttended.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				attended--;
				tv_attended.setText("" + attended);
			}
		});

		b_addbunked.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bunked++;
				tv_bunked.setText("" + bunked);
			}
		});

		b_subBunked.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bunked--;
				tv_bunked.setText("" + bunked);
			}
		});

	}

	public void saveEdit() {

		int totalclasses = 0, maxBunks = 0;
		String subName = "";

		dsl = new Database_SubjectList(getActivity());
		dsl.open();
		Cursor c_dsl = dsl.getLimit(subjectList.get(selectedSubject));
		if (c_dsl.moveToFirst()) {
			do {
				subName = c_dsl.getString(Database_SubjectList.COL_SUBJECTNAME);

				String total_classes = c_dsl
						.getString(Database_SubjectList.COL_TOTAL);
				String max_bunks = c_dsl
						.getString(Database_SubjectList.COL_MAXBUNKS);

				maxBunks = Integer.parseInt(max_bunks);
				totalclasses = Integer.parseInt(total_classes);
			} while (c_dsl.moveToNext());
		}

		c_dsl.close();

		dsl.updateSubject(subName, bunked, attended, totalclasses, maxBunks,
				subName);
		dsl.close();
	}

	public void showMain() {

		getsubjectList();
		h = new Handler();
		r = new Runnable() {

			@Override
			public void run() {

				if (MainScreen.newtimetableCreated) {
					showMain();
					MainScreen.newtimetableCreated = false;
				} else {

					h.postDelayed(r, 1);
				}

			}
		};
		h.postDelayed(r, 1);

		MainScreen.viewPager.setPagingEnabled(true);

		// the green part.
		final ViewGroup.LayoutParams params = MainScreen.b_top
				.getLayoutParams();

		handler = new Handler();
		tempHeight = 0;
		runnable = new Runnable() {
			@Override
			public void run() {

				params.height = tempHeight;

				tempHeight += 50;
				if (tempHeight < MainScreen.Screenheight / 2)
					handler.postDelayed(runnable, 1);
				else
					params.height = MainScreen.Screenheight / 2;

				MainScreen.b_top.setLayoutParams(params);

			}
		};
		handler.postDelayed(runnable, 1);

		MainScreen.isMainShowing = true;

		getSubjectInfo(selectedSubject);
		if (selectedSubject == 0) {
			b_editMain.setVisibility(View.INVISIBLE);
		} else
			b_editMain.setVisibility(View.VISIBLE);

		b_settings.setVisibility(View.VISIBLE);
		b_subjectlist.setVisibility(View.VISIBLE);
		b_back.setVisibility(View.GONE);

		tv_title.setText(subjectList.get(selectedSubject));
		attendance.setVisibility(View.VISIBLE);
		b_attendancePercent.setText(attendancePercent);

		middleBox_main.setVisibility(View.VISIBLE);
		middleBox_settings.setVisibility(View.GONE);
		middleBox_subjects.setVisibility(View.GONE);
		middleBox_editMain.setVisibility(View.GONE);

		b_classesBunked.setText(classesBunked);
		b_classesAttended.setText(classesAttended);
		b_classesHeld.setText(classesHeld);

		tv_bunksLeft.setText("You have " + bunksLeft + " bunks left");
	}

	public void getsubjectList() {

		subjectList = new ArrayList<String>();
		subjectList.add("OverAll");

		dsl = new Database_SubjectList(getActivity());
		dsl.open();

		Cursor cursor = dsl.getAllRows();
		if (cursor.moveToFirst()) {
			do {

				String data = cursor
						.getString(Database_SubjectList.COL_SUBJECTNAME);
				subjectList.add(data);

			} while (cursor.moveToNext());
		}

		// close the cursor to avoid resource leak
		cursor.close();
		dsl.close();
	}

	public void showSubjects() {

		// the green part.
		final ViewGroup.LayoutParams params = MainScreen.b_top
				.getLayoutParams();

		handler = new Handler();
		tempHeight = MainScreen.Screenheight / 2;
		tempHeight_box = (60 * MainScreen.Screenheight) / 100;
		runnable = new Runnable() {
			@Override
			public void run() {

				params.height = tempHeight;

				tempHeight += 50;
				if (tempHeight < MainScreen.Screenheight)
					handler.postDelayed(runnable, 1);
				else {
					params.height = MainScreen.Screenheight;
					animationDone = true;
				}
				MainScreen.b_top.setLayoutParams(params);

			}
		};
		handler.postDelayed(runnable, 1);

		MainScreen.isMainShowing = false;
		subjects_body.removeAllViews();

		MainScreen.viewPager.setPagingEnabled(false);

		handler2 = new Handler();
		runnable2 = new Runnable() {
			@Override
			public void run() {

				if (animationDone) {

					b_settings.setVisibility(View.VISIBLE);
					b_subjectlist.setVisibility(View.INVISIBLE);
					b_back.setVisibility(View.GONE);

					tv_title.setText("SUBJECT LIST");
					attendance.setVisibility(View.GONE);

					middleBox_main.setVisibility(View.GONE);
					middleBox_settings.setVisibility(View.GONE);
					middleBox_subjects.setVisibility(View.VISIBLE);

					for (int i = 0; i < subjectList.size(); i++) {

						View newSubject = getActivity().getLayoutInflater()
								.inflate(
										R.layout.new_entry_middlepage_subjects,
										subjects_body);

						LinearLayout ll = (LinearLayout) newSubject
								.findViewById(R.id.subjectListContainer);
						TextView tv_subjectName = (TextView) newSubject
								.findViewById(R.id.tv_subjectList_subjectName);
						tv_subjectName.setText(subjectList.get(i).toString());
						tv_subjectName.setId(i);

						getSubjectInfo(i);
						TextView tv_attendanceP = (TextView) newSubject
								.findViewById(R.id.tv_subjectList_subjectDetails_attendanceValue);
						tv_attendanceP.setText(attendancePercent);
						if (Integer.parseInt(attendancePercent) < 75) {

							tv_subjectName.setTextColor(Color.RED);
							tv_attendanceP.setTextColor(Color.RED);
						} else {
							tv_subjectName.setTextColor(Color.BLACK);
							tv_attendanceP.setTextColor(Color.BLACK);
						}
						tv_attendanceP.setId(i + 1000);

						Log.i("attendancePercent",
								Integer.parseInt(attendancePercent) + "");

						TextView tv_bunksLeft = (TextView) newSubject
								.findViewById(R.id.tv_subjectList_subjectDetails_bunksleftValue);
						if (Integer.parseInt(bunksLeft) < Integer
								.parseInt(classesBunked))
							tv_attendanceP.setTextColor(Color.RED);
						else
							tv_attendanceP.setTextColor(Color.BLACK);
						tv_bunksLeft.setText(bunksLeft);
						tv_bunksLeft.setId(i + 2000);

						ll.measure(MeasureSpec.UNSPECIFIED,
								MeasureSpec.UNSPECIFIED);
						int height = ll.getMeasuredHeight();

						Button b = (Button) newSubject
								.findViewById(R.id.button_subjectList);
						b.setId(i + 100);

						// Make the whole Screen Green.
						ViewGroup.LayoutParams bparams = b.getLayoutParams();
						bparams.height = (height);
						b.setLayoutParams(bparams);

						b.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Button button = (Button) v.findViewById(v
										.getId());
								RelativeLayout mainContainer = (RelativeLayout) button
										.getParent();
								LinearLayout subContainer = (LinearLayout) mainContainer
										.getChildAt(0);
								TextView tv = (TextView) subContainer
										.getChildAt(0);
								String s = tv.getText().toString();
								for (int j = 0; j < subjectList.size(); j++) {
									if (s.equalsIgnoreCase(subjectList.get(j))) {
										{
											selectedSubject = j;
											showMain();
										}
									}
								}
							}
						});

					}

				} else
					handler2.postDelayed(runnable2, 1);

			}
		};
		handler2.postDelayed(runnable2, 1);

	}

	public void showSettings() {

		MainScreen.viewPager.setPagingEnabled(false);

		b_settings.setVisibility(View.GONE);
		b_subjectlist.setVisibility(View.INVISIBLE);
		b_back.setVisibility(View.VISIBLE);
		b_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMain();
			}
		});

		tv_title.setText("SETTINGS");
		attendance.setVisibility(View.GONE);

		// change the green background.
		ViewGroup.LayoutParams params = MainScreen.b_top.getLayoutParams();
		params.height = (15 * MainScreen.Screenheight) / 100;
		MainScreen.b_top.setLayoutParams(params);

		ViewGroup.LayoutParams params1 = settings_body.getLayoutParams();
		params1.height = (70 * MainScreen.Screenheight) / 100;
		settings_body.setLayoutParams(params1);

		middleBox_main.setVisibility(View.GONE);
		middleBox_settings.setVisibility(View.VISIBLE);
		middleBox_subjects.setVisibility(View.GONE);

		CustomViewPager viewPager = (CustomViewPager) fragmentView
				.findViewById(R.id.Vpager2);

		viewPager.setAdapter(new MyAdapter(getActivity()
				.getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				settings_num = arg0;
				if (arg0 == 0) {

					tv_settings_subtitle.setText("NOTIFICATIONS");
				} else if (arg0 == 1) {
					tv_settings_subtitle.setText("CREATE A NEW TIMETABLE");
				} else if (arg0 == 2) {
					tv_settings_subtitle.setText("SHARE");
				} else if (arg0 == 3) {
					tv_settings_subtitle.setText("TRUANCY");
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		viewPager.setCurrentItem(settings_num);

	}

	public void getSubjectInfo(int n) {

		int ibunkCount = 0;
		int iattended = 0;
		int itotalClasses = 0;
		int iclassesHeld = 0;
		int iattendancePercent = 0;
		int ibunksLeft = 0;
		int bunkPercent = 0;

		if (n == 0) {

			dsl = new Database_SubjectList(getActivity());
			dsl.open();

			Cursor cursor = dsl.getAllRows();
			if (cursor.moveToFirst()) {
				do {

					ibunkCount += Integer.parseInt(cursor
							.getString(Database_SubjectList.COL_BUNKED));
					iattended += Integer.parseInt(cursor
							.getString(Database_SubjectList.COL_ATTENDED));
					itotalClasses += Integer.parseInt(cursor
							.getString(Database_SubjectList.COL_TOTAL));
					bunkPercent = Integer.parseInt(cursor
							.getString(Database_SubjectList.COL_MAXBUNKS));

				} while (cursor.moveToNext());
			}

			// close the cursor to avoid resource leak
			cursor.close();
			dsl.close();

		} else {
			dsl = new Database_SubjectList(getActivity());
			dsl.open();

			Cursor cursor = dsl.getLimit(subjectList.get(n));
			if (cursor.moveToFirst()) {
				do {

					ibunkCount = Integer.parseInt(cursor
							.getString(Database_SubjectList.COL_BUNKED));
					iattended = Integer.parseInt(cursor
							.getString(Database_SubjectList.COL_ATTENDED));
					itotalClasses = Integer.parseInt(cursor
							.getString(Database_SubjectList.COL_TOTAL));
					bunkPercent = Integer.parseInt(cursor
							.getString(Database_SubjectList.COL_MAXBUNKS));

				} while (cursor.moveToNext());
			}

			// close the cursor to avoid resource leak
			cursor.close();
			dsl.close();

		}

		iclassesHeld = ibunkCount + iattended;
		if (iclassesHeld > itotalClasses) {
			itotalClasses = iclassesHeld;
		}

		ibunksLeft = (bunkPercent * itotalClasses) / 100 - ibunkCount;

		if (iclassesHeld == 0) {
			iattendancePercent = 0;
		} else
			iattendancePercent = (iattended * 100) / iclassesHeld;

		attendancePercent = "" + iattendancePercent;
		classesAttended = "" + iattended;
		classesBunked = "" + ibunkCount;
		classesHeld = "" + iclassesHeld;
		bunksLeft = "" + ibunksLeft;

	}

	/** Class For the ViewPager : The Fragment Page Adapter **/
	class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {

			Fragment fragment = null;
			if (arg0 == 0) {

				fragment = new Fragment_settings_notifications();

			}
			if (arg0 == 1) {

				fragment = new Fragment_settings_createnewtimetable();
			}
			if (arg0 == 2) {

				fragment = new Fragment_settings_share();
			}
			if (arg0 == 3) {

				fragment = new Fragment_settings_about();
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

	}

}