package com.felixwhitesean.classcommapp;

import android.app.Activity;
import android.os.Bundle;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class android_large___3_activity extends Activity {
	private RecyclerView recyclerView,recyclerView2;
	private TimectableAdapter adapter;
	private UnitsAdapter adapter2;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_large___3);
		//custom code goes here
		recyclerView = findViewById(R.id.recyclerview);
		recyclerView2 = findViewById(R.id.recyclerview2);
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
	}
}
	
	