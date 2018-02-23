package com.ellipsonic.www.student_tracking.ui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ellipsonic.www.student_tracking.R;

import static android.R.attr.name;

public class NewDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Student.db";
    public static final String TABLE_NAME = "beacon_table";
    public static final String COL_1 = "STUDENT_NAME";
    public static final String COL_2 = "SCHOOL_NAME";
    public static final String COL_3 = "MACID";
    public static final String COL_4 = "AGE";
    public static final String COL_5 = "STUDENT_STANDARD";

    public NewDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (MACID TEXT PRIMARY KEY AUTOINCREMENT, STUDENT_NAME TEXT,SCHOOL_NAME TEXT,AGE TEXT,STUDENT_STANDARD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String student_name, String school_name, String macID, String age, String student_standard) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, student_name);
        contentValues.put(COL_2, school_name);
        contentValues.put(COL_3, macID);
        contentValues.put(COL_4, age);
        contentValues.put(COL_5, student_standard);

      //  String abx = email;
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

//    public Cursor getAllData() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
//        return res;
//    }
//
//
//    public boolean updateData(String id, String name, String surname, String marks) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_1, id);
//        contentValues.put(COL_2, name);
//        contentValues.put(COL_3, surname);
//        contentValues.put(COL_4, marks);
//        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
//        return true;
//    }
//
//    public Integer deleteData(String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
//    }
//
//
//    public int Login(String email, String password) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        try {
//            int i = 0;
//            Cursor c = null;
//            c = db.rawQuery("select * from " + TABLE_NAME + " where email =" + "\"" + email.trim() + "\"" + " and password=" + "\"" + password.trim() + "\"", null);
//            c.moveToFirst();
//            i = c.getCount();
//            c.close();
//            return i;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

}
