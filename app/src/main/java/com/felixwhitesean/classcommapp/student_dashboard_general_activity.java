package com.felixwhitesean.classcommapp;

/*
	 *	This content is generated from the API File Info.
	 *	(Alt+Shift+Ctrl+I).
	 *
	 *	@desc 		
	 *	@file 		student_dashboard_general
	 *	@date 		Wednesday 10th of July 2024 07:18:27 PM
	 *	@title 		Page 1
	 *	@author 	
	 *	@keywords 	
	 *	@generator 	Export Kit v1.3.figma
	 *
	 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.felixwhitesean.classcommapp.R;

	public class student_dashboard_general_activity extends Activity {


	private View _bg__student_dashboard_general_ek2;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_dashboard_general);

		Button menuBtn = findViewById(R.id.icon);
		menuBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(student_dashboard_general_activity.this, Menu.class);
				startActivity(intent);
			}
		});
		_bg__student_dashboard_general_ek2 = (View) findViewById(R.id._bg__student_dashboard_general_ek2);
		//custom code goes here
	
	}
}
	
	