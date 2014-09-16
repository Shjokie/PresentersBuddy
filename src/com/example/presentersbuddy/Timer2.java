package com.example.presentersbuddy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Timer2 extends Activity {
	TextView txt1, txt2, set;

	EditText title, hrs, min, sec;
	Button time;
	String hrss, mins, secs, duration;
	int sek, dak, saa, jumula;
	public static int jumla = 0;
	String[] tim;
	String[] titl;

	Button submit;

	Typeface face, face2;

	int counters;
	TimePickerDialog timePickerDialog;
	Calendar calendar, calSet, updated;

	ArrayList<HashMap<String, String>> sectionList;

	private static final String TAG_TITLE = "title";
	private static final String TAG_TIME = "time";

	DatabaseHandler db = new DatabaseHandler(this);
	SharedPreferences sharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer);
		face = Typeface
				.createFromAsset(getAssets(), "fonts/Bebas/BEBAS___.ttf");
		face2 = Typeface.createFromAsset(getAssets(), "fonts/pt/PTN77F.ttf");

		title = (EditText) findViewById(R.id.title);
		hrs = (EditText) findViewById(R.id.hrss);
		min = (EditText) findViewById(R.id.mins);
		sec = (EditText) findViewById(R.id.secs);
		set = (TextView) findViewById(R.id.set);
		hrs.setTypeface(face);
		min.setTypeface(face);
		sec.setTypeface(face);
		set.setTypeface(face2);
		title.setTypeface(face2);
		ListView lv = (ListView) findViewById(R.id.list);
		submit = (Button) findViewById(R.id.submit);

		sectionList = new ArrayList<HashMap<String, String>>();
		sharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				secs = sec.getText().toString();
				hrss = hrs.getText().toString();
				mins = min.getText().toString();

				if (!title.getText().toString().equalsIgnoreCase("")
						&& !secs.equalsIgnoreCase("")
						&& !mins.equalsIgnoreCase("")
						&& !hrss.equalsIgnoreCase("")) {

					sek = Integer.parseInt(secs);
					saa = Integer.parseInt(hrss);
					dak = Integer.parseInt(mins);
					if (sek <= 60 && dak <= 60) {
						if (hrss.length() != 1 && mins.length() != 1
								&& secs.length() != 1) {

							duration = hrss + ":" + mins + ":" + secs;
							jumla += ((saa * 3600 * 1000) + (dak * 60 * 1000) + (sek * 1000));
							// jumula = sharedPreferences.getInt("storedJumula",
							// 0);
							SharedPreferences.Editor editor = sharedPreferences
									.edit();
							editor.putInt("storedJumula", +jumla);
							editor.commit(); // Very important

							db.addSection(new Section(title.getText()
									.toString(), duration));

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();
							// adding each child node to HashMap key => value
							map.put(TAG_TITLE, title.getText().toString());
							map.put(TAG_TIME, duration);
							// adding HashList to ArrayList
							sectionList.add(map);
							ListView lv = (ListView) findViewById(R.id.list);

							SimpleAdapter adapters = new SimpleAdapter(
									Timer2.this, sectionList,
									R.layout.list_item, new String[] {
											TAG_TITLE, TAG_TIME }, new int[] {
											R.id.title, R.id.duration }) {
								@Override
								public View getView(int pos, View convertView,
										ViewGroup parent) {
									View v = convertView;
									if (v == null) {
										LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
										v = vi.inflate(R.layout.list_item, null);
									}
									TextView tv = (TextView) v
											.findViewById(R.id.title);
									tv.setText(sectionList.get(pos)
											.get("title"));
									tv.setTypeface(face2);
									TextView tvs = (TextView) v
											.findViewById(R.id.duration);
									tvs.setText(sectionList.get(pos)
											.get("time"));
									tvs.setTypeface(face2);
									return v;
								}
							};
							// updating listview
							lv.setAdapter(adapters);
							((MainActivity) getParent()).updatemain();
						} else {
							String message = "if input is one digit, add a leading zero";

							Toast toast = Toast.makeText(
									getApplicationContext(), message,
									Toast.LENGTH_LONG);
							View txt = toast.getView();
							TextView tv = (TextView) txt
									.findViewById(android.R.id.message);
							tv.setGravity(Gravity.CENTER);
							toast.show();
						}

					} else {
						String message = "Invalid input on time";

						Toast toast = Toast.makeText(getApplicationContext(),
								message, Toast.LENGTH_LONG);
						View txt = toast.getView();
						TextView tv = (TextView) txt
								.findViewById(android.R.id.message);
						tv.setGravity(Gravity.CENTER);
						toast.show();
					}

				} else {
					String message = "Please make sure you have filled both fields as specified";

					Toast toast = Toast.makeText(getApplicationContext(),
							message, Toast.LENGTH_LONG);
					View txt = toast.getView();
					TextView tv = (TextView) txt
							.findViewById(android.R.id.message);
					tv.setGravity(Gravity.CENTER);
					toast.show();

				}
			}
		});
		loadSavedPreferences();
	}

	private void loadSavedPreferences() {

		// Reading all contacts
		Log.d("Reading: ", "Reading all sections..");
		List<Section> sections = db.getAllSections();

		for (Section cn : sections) {
			String title = cn.getTitle();
			String time = cn.getTime();
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			// adding each child node to HashMap key => value
			map.put(TAG_TITLE, title);
			map.put(TAG_TIME, time);
			// adding HashList to ArrayList
			sectionList.add(map);
			ListView lv = (ListView) findViewById(R.id.list);

			SimpleAdapter adapters = new SimpleAdapter(Timer2.this,
					sectionList, R.layout.list_item, new String[] { TAG_TITLE,
							TAG_TIME }, new int[] { R.id.title, R.id.duration }) {
				@Override
				public View getView(int pos, View convertView, ViewGroup parent) {
					View v = convertView;
					if (v == null) {
						LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						v = vi.inflate(R.layout.list_item, null);
					}
					TextView tv = (TextView) v.findViewById(R.id.title);
					tv.setText(sectionList.get(pos).get("title"));
					tv.setTypeface(face2);
					TextView tvs = (TextView) v.findViewById(R.id.duration);
					tvs.setText(sectionList.get(pos).get("time"));
					tvs.setTypeface(face);
					return v;
				}
			};
			// updating listview
			lv.setAdapter(adapters);
			((MainActivity) getParent()).updatemain();
		}

	}

}
