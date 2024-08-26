package com.felixwhitesean.classcommapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.TextView;

public class Menu extends Activity {


	private TextView TextView1, TextView2, TextView3, TextView4;


	@Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_large___2);


			// Initialize views
			TextView1 = findViewById(R.id.setting_ek1);
			TextView2 = findViewById(R.id.account_ek1);
			TextView3 = findViewById(R.id.notifications);
			TextView4 = findViewById(R.id.timetable);


		// Set click listeners for each view
			TextView1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Navigate to ActivityA
					Intent intent = new Intent(Menu.this, student_dashboard_general_activity.class);
					startActivity(intent);
				}
			});

			TextView2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Navigate to ActivityB
					Intent intent = new Intent(Menu.this, GeneralDashboard.class);
					startActivity(intent);
				}
			});

			TextView3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Navigate to ActivityC
					Intent intent = new Intent(Menu.this, GeneralDashboard.class);
					startActivity(intent);
				}
			});

			TextView4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Navigate to ActivityC
					Intent intent = new Intent(Menu.this, Timetable.class);
					startActivity(intent);
				}
			});
	}
}
	
	