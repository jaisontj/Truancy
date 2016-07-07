package com.OutSideTheByte.truancy.CreateNewTimeTable;

import com.OutSideTheByte.truancy.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Fragment_Step2 extends Fragment {

	View fragmentView;

	Button b_next, b_back, b_hide, b_unhide, b_add;
	EditText et_subject;
	LinearLayout step2_bb, entrySpace;
	TextView tv_title;
	ScrollView sv;
	Animation fade_in;

	int unique_id = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragmentView = inflater.inflate(R.layout.fragment_step2, container,
				false);

		CreateNewTimeTable.SUBJECT_NAME_FINAL.clear();

		// Initializing Animation
		fade_in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);

		sv = (ScrollView) fragmentView.findViewById(R.id.step2_scrollView);

		tv_title = (TextView) fragmentView.findViewById(R.id.tv_step2_title);
		entrySpace = (LinearLayout) fragmentView
				.findViewById(R.id.step2_entrySpace);
		entrySpace.removeAllViews();
		step2_bb = (LinearLayout) fragmentView
				.findViewById(R.id.step2_bottomBar);

		b_next = (Button) fragmentView.findViewById(R.id.button_nextStep);
		b_back = (Button) fragmentView.findViewById(R.id.button_prevStep);
		b_unhide = (Button) fragmentView.findViewById(R.id.button_unhide);
		b_hide = (Button) fragmentView.findViewById(R.id.button_hide);
		b_add = (Button) fragmentView.findViewById(R.id.button_add);

		et_subject = (EditText) fragmentView
				.findViewById(R.id.editText_subject);

		b_unhide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				step2_bb.setVisibility(View.VISIBLE);
				b_unhide.setVisibility(View.GONE);
				b_next.setVisibility(View.GONE);
				b_back.setVisibility(View.GONE);
				b_add.setVisibility(View.GONE);

			}
		});

		b_hide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// to hide the keyboard
				InputMethodManager input = (InputMethodManager) getActivity()
						.getSystemService(Activity.INPUT_METHOD_SERVICE);
				input.hideSoftInputFromWindow(getActivity().getCurrentFocus()
						.getWindowToken(), 0);

				tv_title.setText("List down your subjects");
				tv_title.setTextColor(Color.WHITE);
				tv_title.setTextSize(20);

				for (int i = 0; i < CreateNewTimeTable.SUBJECT_NAME_FINAL
						.size(); i++) {

					TextView tv = ((TextView) ((LinearLayout) (CreateNewTimeTable.SUBJECT_NAME_FINAL
							.get(i)).getParent()).getChildAt(0));
					tv.setTextColor(Color.BLACK);

					((TextView) ((LinearLayout) (CreateNewTimeTable.SUBJECT_NAME_FINAL
							.get(i)).getParent()).getChildAt(1))
							.setTextColor(Color.BLACK);
				}

				step2_bb.setVisibility(View.INVISIBLE);
				b_unhide.setVisibility(View.VISIBLE);
				b_next.setVisibility(View.VISIBLE);
				b_back.setVisibility(View.VISIBLE);
				b_add.setVisibility(View.GONE);

			}
		});

		et_subject.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.length() > 0) {
					b_add.setVisibility(View.VISIBLE);
					tv_title.setText("List down your subjects");
					tv_title.setTextColor(Color.WHITE);
					tv_title.setTextSize(20);
					if (s.length() > 10) {
						tv_title.setText("Long subject name , use short hand");
						tv_title.setTextColor(Color.BLUE);
						tv_title.setTextSize(20);
						b_add.setVisibility(View.GONE);
					}

				} else
					b_add.setVisibility(View.GONE);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		b_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				addEntry();
			}
		});

		b_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (unique_id == 0) {
					tv_title.setText("At-least add one subject");
					tv_title.setTextColor(Color.RED);
					tv_title.setTextSize(20);
				} else {

					CreateNewTimeTable.SUBJECT_NAME.clear();
					for (int i = 0; i < CreateNewTimeTable.SUBJECT_NAME_FINAL
							.size(); i++) {

						CreateNewTimeTable.SUBJECT_NAME
								.add(CreateNewTimeTable.SUBJECT_NAME_FINAL
										.get(i));
					}

					FragmentManager fragmentManager = getFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.createnewtt_container,
							new Fragment_Step3());
					fragmentTransaction.commit();
				}
			}
		});

		b_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				CreateNewTimeTable.SUBJECT_NAME.clear();
				for (int i = 0; i < CreateNewTimeTable.SUBJECT_NAME_FINAL
						.size(); i++) {

					CreateNewTimeTable.SUBJECT_NAME
							.add(CreateNewTimeTable.SUBJECT_NAME_FINAL.get(i));
				}

				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.createnewtt_container,
						new Fragment_Step1());
				fragmentTransaction.commit();

			}
		});

		showPreviousEntries();

		return fragmentView;

	}

	public void showPreviousEntries() {

		for (int i = 0; i < CreateNewTimeTable.SUBJECT_NAME.size(); i++) {

			View newEntry = getActivity().getLayoutInflater().inflate(
					R.layout.new_entry_subjectname, entrySpace);

			/** Adding to list **/
			// Subject Name
			TextView tv_subName = (TextView) newEntry
					.findViewById(R.id.SubName);
			tv_subName.setText(CreateNewTimeTable.SUBJECT_NAME.get(i).getText()
					.toString());
			tv_subName.setTextSize(CreateNewTimeTable.Screenwidth / 50);
			tv_subName.setId(unique_id);

			CreateNewTimeTable.SUBJECT_NAME_FINAL.add(tv_subName);

			// Delete button
			Button deleteSub = (Button) newEntry.findViewById(R.id.Delete);
			deleteSub.setId(200 + unique_id);

			deleteSub.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					delete_subjectentry(v);
				}
			});

			unique_id++;

		}
	}

	public void addEntry() {

		Boolean duplicateEntry = false;

		for (int i = 0; i < CreateNewTimeTable.SUBJECT_NAME_FINAL.size(); i++) {
			if (CreateNewTimeTable.SUBJECT_NAME_FINAL.get(i).getText()
					.toString()
					.equalsIgnoreCase(et_subject.getText().toString())) {

				duplicateEntry = true;

				tv_title.setText("Duplicate subject");
				tv_title.setTextColor(Color.RED);
				tv_title.setTextSize(20);

				TextView tv = ((TextView) ((LinearLayout) (CreateNewTimeTable.SUBJECT_NAME_FINAL
						.get(i)).getParent()).getChildAt(0));
				tv.setTextColor(Color.RED);

				final LinearLayout ll = (LinearLayout) tv.getParent();
				((TextView) ((LinearLayout) (CreateNewTimeTable.SUBJECT_NAME_FINAL
						.get(i)).getParent()).getChildAt(1))
						.setTextColor(Color.RED);

				// To Scroll to where the error is
				ll.requestFocus();
				final int x = ll.getHeight() * i;

				new Handler().post(new Runnable() {
					@Override
					public void run() {
						sv.smoothScrollTo(0, x);
					}
				});
			} else {
				TextView tv = ((TextView) ((LinearLayout) (CreateNewTimeTable.SUBJECT_NAME_FINAL
						.get(i)).getParent()).getChildAt(0));
				tv.setTextColor(Color.BLACK);

				((TextView) ((LinearLayout) (CreateNewTimeTable.SUBJECT_NAME_FINAL
						.get(i)).getParent()).getChildAt(1))
						.setTextColor(Color.BLACK);
			}

		}

		if (!duplicateEntry) {

			tv_title.setText("List down your subjects");
			tv_title.setTextColor(Color.WHITE);
			tv_title.setTextSize(20);

			View newEntry = getActivity().getLayoutInflater().inflate(
					R.layout.new_entry_subjectname, entrySpace);
			newEntry.startAnimation(fade_in);

			/** Adding to list **/
			// Subject Name
			TextView tv_subName = (TextView) newEntry
					.findViewById(R.id.SubName);
			tv_subName.setText(et_subject.getText().toString());
			tv_subName.setTextSize(CreateNewTimeTable.Screenwidth / 50);
			tv_subName.setId(unique_id);

			CreateNewTimeTable.SUBJECT_NAME_FINAL.add(tv_subName);

			// Delete button
			Button deleteSub = (Button) newEntry.findViewById(R.id.Delete);
			deleteSub.setId(200 + unique_id);

			deleteSub.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					delete_subjectentry(v);
				}
			});

			unique_id++;

			// enabling 'X' button if more than one field is present
			if (CreateNewTimeTable.SUBJECT_NAME_FINAL.size() > 1) {
				LinearLayout templl = (LinearLayout) entrySpace.getChildAt(0);
				Button temp_x = (Button) templl.getChildAt(1);
				temp_x.setVisibility(View.VISIBLE);

			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					sv.fullScroll(View.FOCUS_DOWN);
				}
			}, 200);

			et_subject.setText("");
		}

	}

	public void delete_subjectentry(View view) {

		Button deleteButton = (Button) view.findViewById(view.getId());
		LinearLayout subjectEntryContainer = (LinearLayout) deleteButton
				.getParent();
		LinearLayout subjectcontainer = (LinearLayout) subjectEntryContainer
				.getParent();
		TextView subName = (TextView) subjectEntryContainer.getChildAt(0);

		((LinearLayout) subjectEntryContainer.getParent())
				.removeView(subjectEntryContainer);
		CreateNewTimeTable.SUBJECT_NAME_FINAL.remove(subName);
		unique_id--;

		// disabling 'X' button if only one field is present
		if (CreateNewTimeTable.SUBJECT_NAME_FINAL.size() == 1) {
			LinearLayout templl = (LinearLayout) subjectcontainer.getChildAt(0);
			Button temp_x = (Button) templl.getChildAt(1);
			temp_x.setVisibility(View.INVISIBLE);

		}

	}

}