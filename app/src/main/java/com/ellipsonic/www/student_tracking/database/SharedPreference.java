package com.ellipsonic.www.student_tracking.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by USER on 10/17/2016.
 */
public class SharedPreference {

    public static final String PREFS_FILE_NAME = "STUDENT_PREFS";
    public static final String STUDENT_NAME = "STUDENT_NAME";
    public static final String STUDENT_SCHOOL = "STUDENT_SCHOOL";
    public static final String STUDENT_MAC = "STUDENT_MAC";
    public static final String STUDENT_AGE = "STUDENT_AGE";
    public static final String STUDENT_STANDARD = "STUDENT_STANDARD";


    public SharedPreference() {
        super();
    }

    public void saveStudentDetails(Context context, String s_name, String s_school , String s_mac,
                                   String s_age , String s_standard) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(STUDENT_NAME, s_name); //1
        editor.putString(STUDENT_SCHOOL, s_school); //2
        editor.putString(STUDENT_MAC, s_mac); //3
        editor.putString(STUDENT_AGE, s_age); //3
        editor.putString(STUDENT_STANDARD, s_standard); //3

        editor.commit(); //4
    }
//    public void saveUserId(Context context, String user_id) {
//        SharedPreferences settings;
//        Editor editor;
//
//        //settings = PreferenceManager.getDefaultSharedPreferences(context);
//        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE); //1
//        editor = settings.edit(); //2
//
//        editor.putString(USER_ID, user_id); //3
//
//        editor.commit(); //4
//    }

    public String getValue(Context context, String key) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        text = settings.getString(key, null);
        return text;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context, String key) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(key);
        editor.commit();
    }
}
