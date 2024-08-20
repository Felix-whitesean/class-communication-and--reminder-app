package com.felixwhitesean.classcommapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class userInfoActivity extends Activity {
	//Declaration of variables
	EditText signupName, signupUsername, signupEmail, course, registrationNumber, department, phoneNumber;
	TextView loginRedirectText;
	Button signupButton;
	FirebaseFirestore database;
	private FirebaseAuth mAuth;
	private CollectionReference usersCollection;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);

		//Initializing form elements
		FirebaseApp.initializeApp(this);
		signupButton = (Button) findViewById(R.id.signupBtn);
		signupUsername = (EditText) findViewById(R.id.username);
		signupEmail = (EditText) findViewById(R.id.emailInput);
		registrationNumber = (EditText) findViewById(R.id.registration_number);
		course = (EditText) findViewById(R.id.course);
		department = (EditText) findViewById(R.id.department_name);
		phoneNumber = (EditText) findViewById(R.id.phone_number);

		//custom code goes here
		signupButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				database = FirebaseFirestore.getInstance();
//				reference = database.getReference("users");
				usersCollection = database.collection("users");
				String email = signupEmail.getText().toString();
				String username = signupUsername.getText().toString();
				String registrationNo = registrationNumber.getText().toString();
				String courseName = course.getText().toString();
				String departmentName = department.getText().toString();
				String phoneNo = phoneNumber.getText().toString();
				if(email.isEmpty() || username.isEmpty() || registrationNo.isEmpty() || courseName.isEmpty() || departmentName.isEmpty() || phoneNo.isEmpty()) {
					if (username.isEmpty()) {
						signupUsername.requestFocus();
					} else if(email.isEmpty()){
						signupEmail.requestFocus();
					} else if(registrationNo.isEmpty()){
						registrationNumber.requestFocus();
					}
					else if(courseName.isEmpty()){
						registrationNumber.requestFocus();
					}
					else if(departmentName.isEmpty()){
						department.requestFocus();
					}
					else {
						phoneNumber.requestFocus();
					}
					Toast.makeText(userInfoActivity.this, "Input all the required details", Toast.LENGTH_SHORT).show();
					readData();
					return;
				}
				else{
					Map<String, Object> user = new HashMap<>();
					user.put("first", "Ada");
					user.put("last", "Lovelace");
					user.put("born", 1815);
//					if(confirm == password) {

//					User user = new User(username, email);
//					usersCollection.document("userID").set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//						@Override
//						public void onSuccess(Void aVoid) {
//							// User successfully added to Firestore
//							Log.d("successmessage", "User added successfull");
//						}
//					}).addOnFailureListener(new OnFailureListener() {
//						@Override
//						public void onFailure(@NonNull Exception e) {
//							// Handle any errors
//							Log.d("errormessage", "Signup not successfull");
//						}
//					});

//					}
//					else{
//						signupConfirmPassword.requestFocus();
						Toast.makeText(userInfoActivity.this, "Snap! Confirm password", Toast.LENGTH_SHORT).show();
						readData();
//					}
				}
			}
		});
	}
	public void readData(){
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		db.collection("class comm app")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								Log.d(TAG, document.getId() + " => " + document.getData());
							}
						} else {
							Log.w(TAG, "Error getting documents.", task.getException());
						}
					}
				});
	}
}
	
	