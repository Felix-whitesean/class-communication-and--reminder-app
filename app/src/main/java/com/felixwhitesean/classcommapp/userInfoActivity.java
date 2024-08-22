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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class userInfoActivity extends Activity {
	//Declaration of variables
	FirebaseUser currentUser;
	EditText signupName, signupUsername, course, registrationNumber, department, phoneNumber;
	TextView loginRedirectText, signupEmail;
	Button signupButton;
	FirebaseFirestore database;
	private FirebaseAuth mAuth;
	String randomId = UUID.randomUUID().toString();
	private CollectionReference usersCollection;
	Spinner spinner;
	String userCategory;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);

		//Initializing form elements
		signupButton = (Button) findViewById(R.id.signupBtn);
		signupUsername = (EditText) findViewById(R.id.username);
		signupEmail = (TextView) findViewById(R.id.emailInput);
		registrationNumber = (EditText) findViewById(R.id.registration_number);
		course = (EditText) findViewById(R.id.course);
		department = (EditText) findViewById(R.id.department_name);
		phoneNumber = (EditText) findViewById(R.id.phone_number);

		// Initializing other items
		FirebaseApp.initializeApp(this);
		currentUser = FirebaseAuth.getInstance().getCurrentUser();

		if (currentUser != null) {
			String userUID = currentUser.getUid();  // Get user UID
			String userEmail = currentUser.getEmail();  // Get user email

			signupEmail.setText(userEmail);
			checkCategory();
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
					String finalUserCategory = checkCategory();
					if (email.isEmpty() || username.isEmpty() || registrationNo.isEmpty() || courseName.isEmpty() || departmentName.isEmpty() || phoneNo.isEmpty()) {
						if (username.isEmpty()) {
							signupUsername.requestFocus();
						} else if (email.isEmpty()) {
							signupEmail.requestFocus();
						} else if (registrationNo.isEmpty()) {
							registrationNumber.requestFocus();
						} else if (courseName.isEmpty()) {
							registrationNumber.requestFocus();
						} else if (departmentName.isEmpty()) {
							department.requestFocus();
						} else {
							phoneNumber.requestFocus();
						}
						Toast.makeText(userInfoActivity.this, "Input all the required details", Toast.LENGTH_SHORT).show();
						readData();

						return;

						// TODO: Verification of use input e.g. checking for particular patterns;
					} else {
						Map<String, Object> user = new HashMap<>();
						user.put("username", username);
						user.put("registration_number", registrationNo);
						user.put("email", email);
						user.put("course", courseName);
						user.put("department", departmentName);
						user.put("phone_number", phoneNo);
						user.put("user_category", finalUserCategory);

//					User user = new User(username, email, departmentName, courseName, registrationNo, phoneNo);
						usersCollection.document(userUID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
							@Override
							public void onSuccess(Void aVoid) {
								// User successfully added to Firestore
								Toast.makeText(userInfoActivity.this, "Successfully added the user: " + username, Toast.LENGTH_SHORT).show();
								Log.d("successmessage", "User added successfull");
								finish();
							}
						}).addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								// Handle any errors
								Toast.makeText(userInfoActivity.this, "Error in adding new user " + username, Toast.LENGTH_SHORT).show();
								Log.d("errormessage", "Signup not successfull");
							}
						});
					}
				}
			});
		}
	}
	public void readData(){
		FirebaseFirestore db = FirebaseFirestore.getInstance();
		db.collection("users").get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							for (QueryDocumentSnapshot document : task.getResult()) {
								ArrayList<User> arrayList = new ArrayList<>();
								for(QueryDocumentSnapshot doc: task.getResult()) {
									User user = doc.toObject(User.class);
									arrayList.add(user);
								}
								Log.d(TAG, document.getId() + " => " + document.getData());
							}
						} else {
							Log.w(TAG, "Error getting documents.", task.getException());
						}
					}
				});
	}
//	public String GenerateRandomId (){
//			randomId = randomId.replace("-", "");
//			return randomId;
//	}
	public String checkCategory(){
		spinner = findViewById(R.id.user_category);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_category, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				// Show a toast message when an item is selected
				userCategory = parentView.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				userCategory = "Student";
			}
		});
		return userCategory;
	}
}
	
	