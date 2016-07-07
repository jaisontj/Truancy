package com.OutSideTheByte.truancy.CreateNewTimeTable;

import com.OutSideTheByte.truancy.R;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Fragment_Step1 extends Fragment {

	View fragmentView;
	Button b_startMonth, b_endMonth, b_done;
	EditText ed_cutOff;
	TextView tv_title;

	Boolean isEntered[] = { false, false, false };

	/** String **/

	String[] months = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
			"JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragmentView = inflater.inflate(R.layout.fragment_step1, container,
				false);

		tv_title = (TextView) fragmentView.findViewById(R.id.tv_step1_title);
		b_startMonth = (Button) fragmentView
				.findViewById(R.id.button_startMonth);
		b_endMonth = (Button) fragmentView.findViewById(R.id.button_endMonth);
		b_done = (Button) fragmentView.findViewById(R.id.button_done);
		b_done.setVisibility(View.GONE);

		ed_cutOff = (EditText) fragmentView.findViewById(R.id.editText_cutOff);

		b_startMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pickMonth(v, 0);

			}
		});

		b_endMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pickMonth(v, 1);

			}
		});

		ed_cutOff.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.length() == 0) {
					isEntered[2] = false;
					b_done.setVisibility(View.GONE);

				} else {

					isEntered[2] = true;
					if (isEntered[0] && isEntered[1] && isEntered[2]) {
						b_done.setVisibility(View.VISIBLE);
					}

					if (Integer.parseInt(s.toString()) >= 100) {

						tv_title.setText("Percentage cannot be greater than 100");
						tv_title.setTextColor(Color.RED);

						b_done.setVisibility(View.GONE);
					} else {
						tv_title.setText("Fill in the details");
						tv_title.setTextColor(Color.WHITE);
					}
				}

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

		b_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				executeDone();

			}
		});

		return fragmentView;
	}

	public void pickMonth(View view, final int flag) {

		final Button picked_month = (Button) getView().findViewById(
				view.getId());
		Button button_monthpicked;
		// create a Dialog component
		final Dialog dialog = new Dialog(getActivity());
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.pickmonthdialog);
		dialog.setTitle("Pick Month");
		button_monthpicked = (Button) dialog.findViewById(R.id.jan);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("JANUARY");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.feb);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("FEBRUARY");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.march);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("MARCH");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.april);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("APRIL");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.may);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("MAY");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.june);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("JUNE");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.july);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("JULY");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.aug);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("AUGUST");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.sept);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("SEPTEMBER");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.oct);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("OCTOBER");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.nov);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("NOVEMBER");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});
		button_monthpicked = (Button) dialog.findViewById(R.id.dec);
		button_monthpicked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				picked_month.setText("DECEMBER");
				isEntered[flag] = true;
				dialog.dismiss();
				if (isEntered[0] && isEntered[1] && isEntered[2]) {
					b_done.setVisibility(View.VISIBLE);
				}
			}
		});

		dialog.show();
	}

	public int getMonth(String month) {

		for (int i = 0; i < 12; i++) {
			if (months[i].equals(month)) {
				return i + 1;
			}
		}
		return 0;
	}

	public void executeDone() {

		int From_monthpicked = getMonth(b_startMonth.getText().toString()
				.trim());
		int To_monthpicked = getMonth(b_endMonth.getText().toString().trim());

		if (From_monthpicked < To_monthpicked) {
			CreateNewTimeTable.total_weeks = ((To_monthpicked - From_monthpicked) + 1) * 4;
		} else if (From_monthpicked > To_monthpicked) {
			CreateNewTimeTable.total_weeks = (((12 - From_monthpicked) + 1) + To_monthpicked) * 4;
		} else if (From_monthpicked == To_monthpicked) {
			CreateNewTimeTable.total_weeks = 4;
		}

		CreateNewTimeTable.bunkLimit = 100 - Integer.parseInt(ed_cutOff
				.getText().toString().trim());

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.createnewtt_container,
				new Fragment_Step2());
		fragmentTransaction.commit();

	}

}
