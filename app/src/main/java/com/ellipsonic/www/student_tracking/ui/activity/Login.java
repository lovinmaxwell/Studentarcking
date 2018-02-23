package com.ellipsonic.www.student_tracking.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ellipsonic.www.student_tracking.R;

import butterknife.OnClick;

import static com.ellipsonic.www.student_tracking.ui.activity.RegisterUser.Email1;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText email, password;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.signup).setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return super.onWindowStartingActionMode(callback);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                login_server();
                break;
            case R.id.signup:
                    Intent intent1 = new Intent(getApplicationContext(), RegisterUser.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);

                    break;
               // }
        }
    }

    public void login_server() {

        final String Loign_Email = email.getText().toString().trim();
        final String Login_Password = password.getText().toString().trim();

        if (Loign_Email.equals(null) || Login_Password.equals(null) ||
                Loign_Email.equals("") || Login_Password.equals("")) {

            Toast.makeText(getApplicationContext(), "Please Enter Valid Email and password",
                    Toast.LENGTH_SHORT).show();
        } else {
            int isLogedIn = databaseHelper.Login(Loign_Email, Login_Password);
            if(isLogedIn == 1){
                Intent intent = new Intent(getApplicationContext(),HomeBeaconNavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else
                Toast.makeText(getApplicationContext(), "Please Enter Valid Email and password",
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

//        super.onBackPressed();
      //  startActivity(new Intent(getApplicationContext(), MainActivity.class));
        //finish();
       // System.exit(0);
//
//
//    public void clickexit(View v)
//    {
//        moveTaskToBack(true); android.os.Process.killProcess(android.os.Process.myPid()); System.exit(1);
//    }

}
