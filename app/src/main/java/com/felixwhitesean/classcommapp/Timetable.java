package com.felixwhitesean.classcommapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Timetable extends Activity {
	private RecyclerView recyclerView,recyclerView2;
	private TimectableAdapter adapter;
	private UnitsAdapter adapter2;
	private TextView dateTextView, coursename;
	private Handler handler = new Handler();
	FirebaseFirestore db;
	String userUID;
	SharedPreferences sharedPref;
	private static final String PREFS_NAME = "UserLoginPrefs";
	private static final String CURRENT_USER_ID = "CurrentUserId";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.timetable);
		//custom code goes here
		recyclerView = findViewById(R.id.recyclerview);
		recyclerView2 = findViewById(R.id.recyclerview2);
		dateTextView = findViewById(R.id.date); // Assuming your TextView has this ID
		handler.post(runnable);

		sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		userUID = sharedPref.getString(CURRENT_USER_ID, null);
		db = FirebaseFirestore.getInstance();
		coursename = findViewById(R.id.computer_technology);

		DocumentReference docRef = db.collection("users").document(userUID);

		if(userUID != null) {
			docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
				@Override
				public void onComplete(@NonNull Task<DocumentSnapshot> task) {
					if (task.isSuccessful()) {
						DocumentSnapshot document = task.getResult();
						if (document.exists()) {
							// Document data successfully retrieved
							String course = document.getString("course");
							String department = document.getString("department");
							String courseDetails = course+" ("+department+")";
							coursename.setText(courseDetails);
						} else {
							Toast.makeText(Timetable.this, "No such document", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(Timetable.this, "get failed with "+task.getException(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		else{
			Toast.makeText(this, "No user details found", Toast.LENGTH_SHORT).show();
			Intent toHome = new Intent(Timetable.this, userInfoActivity.class);
			startActivity(toHome);
			finish();
		}

		adapter = new TimectableAdapter(this);
		adapter2 = new UnitsAdapter(this);

		recyclerView.setAdapter(adapter);
		recyclerView2.setAdapter(adapter2);

		GridLayoutManager layoutManager = new GridLayoutManager(this, 6);
		GridLayoutManager layoutManager2 = new GridLayoutManager(this, 4);
		layoutManager.setOrientation(RecyclerView.HORIZONTAL);
		layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView2.setLayoutManager(layoutManager2);

		ArrayList<TimetableModelClass> lessons = new ArrayList<>();
		lessons.add(new TimetableModelClass("Day of the Week", "0700\n0900","", "0900\n1100","", "1100\n 1300","", "1400\n 1600", "","1600\n 1800", ""));
		lessons.add(new TimetableModelClass("Monday", "ECCI 3301", "D1","ECCI 3303", "D2", "ECSI 3304", "D3", "ECCI 3304", "D4","ECCI 3306", "D5" ));
		lessons.add(new TimetableModelClass("Tuesday", "ECCI 3305","D21", "ECCI 3306","D23", "ECCI 3307","D32", "ECCI 3308", "D31", "ECCI 3309","D2"));
		lessons.add(new TimetableModelClass("Wednesday", "ECCI 3301", "D1","ECCI 3303", "D2", "ECSI 3304", "D3", "ECCI 3304", "D4","ECCI 3306", "D5" ));
		lessons.add(new TimetableModelClass("Thursday", "ECCI 3305","D21", "ECCI 3306","D23", "ECCI 3307","D32", "ECCI 3308", "D31", "ECCI 3309","D2"));
		lessons.add(new TimetableModelClass("Friday", "ECCI 3301", "D1","ECCI 3303", "D2", "ECSI 3304", "D3", "ECCI 3304", "D4","ECCI 3306", "D5" ));

		ArrayList<UnitsModelClass> unitDetails = new ArrayList<>();
		unitDetails.add(new UnitsModelClass("1", "ECS1 3301","Net Design & Fibre ", "John Okwoyooz"));
		unitDetails.add(new UnitsModelClass("2", "ECS1 3302","Industrial Programming Logic", "Kulvert Lewin"));
		unitDetails.add(new UnitsModelClass("3", "ECS1 3312","Business Plan", "Kulvert Lewin"));
		unitDetails.add(new UnitsModelClass("4", "ECS1 3332","Hardware Assembly Workshop", "Peter Ngugi"));
		unitDetails.add(new UnitsModelClass("5", "ECS1 3342","Mobile App Development", "Edwin Ouma"));
		unitDetails.add(new UnitsModelClass("6", "ECS1 3352","System Development Lab", "Akello"));

		adapter.setModelClassesRows(lessons);
		adapter2.setUnits(unitDetails);
		Button menuBtn = findViewById(R.id.icon);
		menuBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Timetable.this, Menu.class);
				startActivity(intent);
			}
		});

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
	
	