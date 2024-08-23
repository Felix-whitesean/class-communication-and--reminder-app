package com.felixwhitesean.classcommapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Menu extends Activity {

	private TextView TextView1, TextView2, TextView3, TextView4;
	private ImageView TextViewCamera;

	private static final int REQUEST_IMAGE_CAPTURE = 1;

	private FirebaseStorage firebaseStorage;
	private StorageReference storageReference;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_top);
	}

	@SuppressLint("WrongViewCast")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_large___2);

		// Initialize Firebase
		FirebaseApp.initializeApp(this);
		firebaseStorage = FirebaseStorage.getInstance();
		storageReference = firebaseStorage.getReference();

		// Initialize views
		TextView1 = findViewById(R.id.setting_ek1);
		TextView2 = findViewById(R.id.account_ek1);
		TextView3 = findViewById(R.id.notifications);
		TextView4 = findViewById(R.id.timetable);
		TextViewCamera = findViewById(R.id.profile);

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

		TextViewCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Open the camera
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
					startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			// Handle the captured image here (e.g., save it, display it, etc.)
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");

			// Set the captured image to the ImageView
			TextViewCamera.setImageBitmap(imageBitmap);

			// Upload image to Firebase Storage
			uploadImageToFirebase(imageBitmap);
		}
	}

	private void uploadImageToFirebase(Bitmap bitmap) {
		// Convert bitmap to byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] data = baos.toByteArray();

		// Create a unique file name for the image
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		String imageFileName = "JPEG_" + timeStamp + ".jpg";

		// Create a reference to the location where you want to store the image
		StorageReference imageRef = storageReference.child("images/" + imageFileName);

		// Upload the image
		UploadTask uploadTask = imageRef.putBytes(data);
		uploadTask.addOnSuccessListener(taskSnapshot -> {
			// Get the download URL and use it as needed
			imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
				// Handle the success, e.g., display the URL or save it to your database
				Log.d("Firebase", "Image upload successful. Download URL: " + uri.toString());
				Toast.makeText(Menu.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
			});
		}).addOnFailureListener(e -> {
			// Handle any errors
			Log.e("Firebase", "Image upload failed: " + e.getMessage());
			Toast.makeText(Menu.this, "Image upload failed!", Toast.LENGTH_SHORT).show();
		});
	}
}
