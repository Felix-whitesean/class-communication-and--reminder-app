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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserInformationSQLite_Activity extends AppCompatActivity {
    DatabaseHelper Mydb;
    EditText Username, RegistrationNumber, course, department, phoneNumber, Email, user_Category,id;
    Button btnAdd, btnViewAll, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_information_sqlite);

        Mydb = new DatabaseHelper(this);


        Username = (EditText) findViewById(R.id.username);
        Email = (EditText) findViewById(R.id.emailInput);
        RegistrationNumber = (EditText) findViewById(R.id.registration_number);
        course = (EditText) findViewById(R.id.course);
        department = (EditText) findViewById(R.id.department_name);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        //id = findViewById(R.id.editTextId);


        btnAdd = (Button) findViewById(R.id.btnadd);
        btnViewAll = findViewById(R.id.btnviewAll);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btndelete);

        AddData();
        viewAll();
        UpdateData();
        DeleteData();

    }

    // Insert user data
    public void AddData() {
        btnAdd.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String username = Username.getText().toString();
                String registrationNo = RegistrationNumber.getText().toString();
                String courseName = course.getText().toString();
                String departmentName = department.getText().toString();
                int phoneNo = Integer.parseInt(phoneNumber.getText().toString());
                boolean isInserted = Mydb.insertData(username,email,phoneNo, registrationNo, courseName, departmentName);
                if (isInserted==true)
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void viewAll() {
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = Mydb.readData();
                if (res.getCount() == 0) {
                    // Show message
                    showMessage("Error", "Nothing found");

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
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    // Update user data
    public void UpdateData() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
         boolean isUpdated = Mydb.updateUserByEmail(Email.getText().toString(),
         Username.getText().toString(),
         RegistrationNumber.getText().toString(),
         course.getText().toString(),
         department.getText().toString(),
         phoneNumber.getText().toString(),
         Email.getText().toString());


             if (isUpdated== true)
                 Toast.makeText(UserInformationSQLite_Activity.this, "Data Updated", Toast.LENGTH_SHORT).show();
             else
                 Toast.makeText(UserInformationSQLite_Activity.this, "Data Not Updated", Toast.LENGTH_SHORT).show();
           }
        }
     );
   }
    public void DeleteData() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = Mydb.deleteUserByEmail(Email.getText().toString());
                if (deletedRows > 0)
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(UserInformationSQLite_Activity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
  }


