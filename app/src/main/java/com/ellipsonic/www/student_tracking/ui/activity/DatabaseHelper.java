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
import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.detectedMacId;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Student.db";

    public static final String TABLE_NAME_STUDENT = "student_table";
    public static final String COL_1 = "FIRSTNAME";
    public static final String COL_2 = "LASTNAME";
    public static final String COL_3 = "EMAIL";
    public static final String COL_4 = "PASSWORD";
    public static final String COL_5 = "CONFORM_PASSWORD";

    public static final String TABLE_NAME_BEACON = "beacon_table";
    public static final String COL_1_BEACON = "STUDENT_NAME";
    public static final String COL_2_BEACON= "SCHOOL_NAME";
    public static final String COL_3_BEACON = "MACID";
    public static final String COL_4_BEACON= "STUDENT_AGE";
    public static final String COL_5_BEACON = "STUDENT_STANDARD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_STUDENT + " (EMAIL TEXT PRIMARY KEY, FIRSTNAME TEXT,LASTNAME TEXT," +
                "PASSWORD TEXT,CONFORM_PASSWORD TEXT)");

        db.execSQL("create table " + TABLE_NAME_BEACON + " (MACID TEXT PRIMARY KEY, STUDENT_NAME TEXT,SCHOOL_NAME TEXT," +
                "STUDENT_AGE TEXT,STUDENT_STANDARD TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BEACON);

        onCreate(db);
    }

    public boolean insertStudentData(String firstname, String lastname, String email, String password, String coform_pwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, firstname);
        contentValues.put(COL_2, lastname);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, password);
        contentValues.put(COL_5, coform_pwd);

        String abx = email;
        long result = db.insert(TABLE_NAME_STUDENT, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insertBeaconData(String student_name, String school_name, String macID, String age,
                                    String student_standard) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_BEACON, student_name);
        contentValues.put(COL_2_BEACON, school_name);
        contentValues.put(COL_3_BEACON, macID);
        contentValues.put(COL_4_BEACON, age);
        contentValues.put(COL_5_BEACON, student_standard);

        //  String abx = email;
        long result = db.insert(TABLE_NAME_BEACON, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllStudentData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_STUDENT, null);
        return res;
    }
    public  String[]  getAllBeaconData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[]  arrData = null;
       // Cursor res = db.rawQuery("select * from " + TABLE_NAME_BEACON, null);


        Cursor cursor = db.query(TABLE_NAME_BEACON, new String[] { "*" },
                null,
                null, null, null, null, null);

        if(cursor != null)
        {
            if (cursor.moveToFirst()) {
                arrData = new String[cursor.getColumnCount()];

                arrData[0] = cursor.getString(0); //  MACID
                arrData[1] = cursor.getString(1); // STUDENT_NAME
                arrData[2] = cursor.getString(2); // SCHOOL_NAME
                arrData[3] = cursor.getString(3); // AGE
                arrData[4] = cursor.getString(4); // STUDENT_STANDARD
            }
        }

        cursor.close();
        db.close();
        return arrData;
    }

    public boolean updateBeaconData( String student_name, String school_name,String mac, String age,String student_standard) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1_BEACON, student_name);
        contentValues.put(COL_2_BEACON, school_name);
        contentValues.put(COL_3_BEACON, mac);
        contentValues.put(COL_4_BEACON, age);
        contentValues.put(COL_5_BEACON, student_standard);
        db.update(TABLE_NAME_BEACON, contentValues, "MACID = ?", new String[]{mac});
        return true;
    }

    public void deleteBeaconData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME_BEACON);
        db.execSQL("create table " + TABLE_NAME_BEACON + " (MACID TEXT PRIMARY KEY, STUDENT_NAME TEXT,SCHOOL_NAME TEXT," +
                "STUDENT_AGE TEXT,STUDENT_STANDARD TEXT)");
//        return db.delete(TABLE_NAME_BEACON, "MACID = ?", new String[]{id});
    }


    public int Login(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int i = 0;
            Cursor c = null;
            c = db.rawQuery("select * from " + TABLE_NAME_STUDENT + " where email =" + "\"" + email.trim() + "\""
                    + " and password=" + "\"" + password.trim() + "\"", null);
            c.moveToFirst();
            i = c.getCount();
            c.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
