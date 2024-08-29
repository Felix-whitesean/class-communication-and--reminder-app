package com.felixwhitesean.classcommapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Menu extends Activity {

	private RelativeLayout FinalBtn, TextView5, TextView4, TextView3, TextView2, TextView1;
	private Button btnLogout;


	@Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_large___2);


			// Initialize views
			TextView1 = findViewById(R.id.setting);
			TextView2 = findViewById(R.id.account);
			TextView3 = findViewById(R.id.date);
			TextView4 = findViewById(R.id.date_ek2);
			TextView5 = findViewById(R.id.Home);
			FinalBtn = findViewById(R.id.bkup);
			btnLogout= findViewById(R.id.logout);

		btnLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Menu.this, SignUpActivity.class);
				startActivity(intent);
			}
		});

		// Set click listeners for each view
			TextView1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Navigate to ActivityA
					Intent intent = new Intent(Menu.this, SensorActivity.class);
					startActivity(intent);
				}
			});

			TextView2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Navigate to ActivityB
					Intent intent = new Intent(Menu.this, ClassrepDashboard.class);
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

		TextView5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Navigate to ActivityC
				Intent intent = new Intent(Menu.this,MainActivity.class);
				startActivity(intent);
			}
		});
		FinalBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Menu.this,UserInformationSQLite_Activity.class);
				startActivity(intent);
			}
		});
	}
}
	
	