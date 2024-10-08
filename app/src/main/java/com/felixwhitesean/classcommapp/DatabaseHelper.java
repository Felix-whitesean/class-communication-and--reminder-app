package com.felixwhitesean.classcommapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "UserInformation.db";
    public static final String TABLE_NAME = "UserInfo_table";
    public static final String COL_1 = "UUID";
    public static final String COL_2 = "Username";
    public static final String COL_3 = "Email";
    public static final String COL_4 = "PhoneNumber";
    public static final String COL_5 = "RegistrationNumber";
    public static final String COL_6 = "Course";
    public static final String COL_7 = "Department";
    public static final String COL_8 = "User_category";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Construct the SQL CREATE TABLE statement
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT UNIQUE, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT, " +
                COL_6 + " TEXT, " +
                COL_7 + " TEXT, " +
                COL_8 + " TEXT )"); // Closing parentheses for the table definition

        // Execute the SQL statement
//        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }
    public boolean insertData(String Username,String Email, int PhoneNumber,String RegistrationNumber,String Course,String Department){
     SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String uuid = UUID.randomUUID().toString(); // Generate UUID
        contentValues.put(COL_2,Username);
        contentValues.put(COL_3,Email);
        contentValues.put(COL_4,PhoneNumber);
        contentValues.put(COL_5,RegistrationNumber);
        contentValues.put(COL_6,Course);
        contentValues.put(COL_7,Department);
//        contentValues.put(COL_8,User_category);
        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result== -1)
            return false;
        else
            return true;
    }
    public Cursor readData(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+ TABLE_NAME,null);
        return result;
    }


    // Update a user by id
    public boolean updateUserByEmail(String Email,String username, String RegistrationNumber, String course, String department, String phoneNumber,String user_Category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, RegistrationNumber);
        contentValues.put(COL_4, course);
        contentValues.put(COL_5, department);
        contentValues.put(COL_6, phoneNumber);
        contentValues.put(COL_7, Email);
        contentValues.put(COL_8, user_Category);

         int result= db.update(TABLE_NAME, contentValues, COL_3 + "= ?", new String[]{Email});
        return result>0; // returns true if update is successful
    }
    // Delete a user by id
    public Integer deleteUserByEmail(String Email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,  COL_3 + " = ?", new String[] {Email});
    }
}
