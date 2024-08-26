package com.felixwhitesean.classcommapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserInformationSQLite_Activity extends AppCompatActivity {
    DatabaseHelper infodb;
    EditText Username, RegistrationNumber, course, department, phoneNumber, Email, user_Category;
    Button btnAdd, btnViewAll, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_information_sqlite);
        infodb= new DatabaseHelper(this);



        Username = (EditText) findViewById(R.id.username);
        Email = (EditText) findViewById(R.id.emailInput);
        RegistrationNumber = (EditText) findViewById(R.id.registration_number);
        course = (EditText) findViewById(R.id.course);
        department = (EditText) findViewById(R.id.department_name);
        phoneNumber = (EditText) findViewById(R.id.phone_number);

        btnAdd= (Button) findViewById(R.id.btnadd);
        btnViewAll =findViewById(R.id.btnviewAll);
        btnUpdate= (Button) findViewById(R.id.btnUpdate);
        btnDelete =findViewById(R.id.btndelete);

        // Insert user data
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = infodb.insertData(Username.getText().toString(),
                        RegistrationNumber.getText().toString(),
                        Integer.parseInt(course.getText().toString()),
                        department.getText().toString(),
                        phoneNumber.getText().toString(),
                        Email.getText().toString(),
                        user_Category.getText().toString());
                if (isInserted)
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
            }
        });
        // View all user data
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = infodb.readData();
                if (res.getCount() == 0) {
                    // Show message
                    Toast.makeText(UserInformationSQLite_Activity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("ID: ").append(res.getString(0)).append("\n");
                    buffer.append("Username: ").append(res.getString(1)).append("\n");
                    buffer.append("RegistrationNumber: ").append(res.getString(2)).append("\n");
                    buffer.append("Course: ").append(res.getString(3)).append("\n");
                    buffer.append("Department: ").append(res.getString(4)).append("\n");
                    buffer.append("PhoneNumber: ").append(res.getString(5)).append("\n");
                    buffer.append("Email: ").append(res.getString(6)).append("\n");
                    buffer.append("User_Category: ").append(res.getString(7)).append("\n\n");
                }

                // Show all data
                Toast.makeText(UserInformationSQLite_Activity.this, buffer.toString(), Toast.LENGTH_LONG).show();
            }
        });
        // Update user data
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdated = infodb.updateUser(Integer.parseInt("1"),  // Change ID as needed
                        Username.getText().toString(),
                        RegistrationNumber.getText().toString(),
                        course.getText().toString(),
                        department.getText().toString(),
                        phoneNumber.getText().toString(),
                        Email.getText().toString(),
                        user_Category.getText().toString());
                if (isUpdated)
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
        // Delete user data
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = infodb.deleteUser(Integer.parseInt("1"));  // Change ID as needed
                if (deletedRows > 0)
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });



//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }


}