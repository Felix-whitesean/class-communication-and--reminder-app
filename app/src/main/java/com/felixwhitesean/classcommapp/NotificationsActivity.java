package com.felixwhitesean.classcommapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationsActivity extends Activity {
	private TextView dateTextView;
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_dashboard);

		dateTextView = findViewById(R.id.editTextDate2);
		handler.post(runnable);
	}
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			updateDate();
			handler.postDelayed(this, 1000); // Update every second
		}
	};
	private void updateDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String currentDateAndTime = dateFormat.format(new Date());
		dateTextView.setText(currentDateAndTime);
	}
}
	
	