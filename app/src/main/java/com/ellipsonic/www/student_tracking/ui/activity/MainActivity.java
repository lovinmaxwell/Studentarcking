package com.ellipsonic.www.student_tracking.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ellipsonic.www.student_tracking.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //https://developers.google.com/identity/sign-in/android/start-integrating


    Button getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        getStarted = (Button)findViewById(R.id.get_started);
        getStarted.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_started:
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                break;
        }

    }




    @Override
    public void onBackPressed() {


        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


}
