package com.ellipsonic.www.student_tracking.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ellipsonic.www.student_tracking.R;

import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.UUID;
import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.Major;


import static com.ellipsonic.www.student_tracking.R.id.reg;
import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.Minor;
import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.detectedMacId;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    Button register;

    DatabaseHelper databaseHelper;


    EditText firstname, lastname, phone, email, password, conformpassword;
    public static String Email1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        databaseHelper = new DatabaseHelper(this);

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        conformpassword = (EditText) findViewById(R.id.conform);
        register = (Button) findViewById(R.id.reg);
        register.setOnClickListener(this);

        //UpdateData();
        // DeleteData();

    }


    //   public void DeleteData() {
//        btnDelete.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Integer deletedRows = myDb.deleteData(editTextId.getText().toString());
//                        if(deletedRows > 0)
//                            Toast.makeText(MainActivity.this,"Data Deleted",Toast.LENGTH_LONG).show();
//                        else
//                            Toast.makeText(MainActivity.this,"Data not Deleted", Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
//    }
//    public void UpdateData() {
//        btnviewUpdate.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        boolean isUpdate = myDb.updateData(editTextId.getText().toString(),
//                                editName.getText().toString(),
//                                editSurname.getText().toString(),editMarks.getText().toString());
//                        if(isUpdate == true)
//                            Toast.makeText(MainActivity.this,"Data Update",Toast.LENGTH_LONG).show();
//                        else
//                            Toast.makeText(MainActivity.this,"Data not Updated",Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
//    }
//    public  void AddData() {
//        register.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        boolean isInserted = myDb.insertData(firstname.getText().toString(),
//                                lastname.getText().toString(),
//                                username.getText().toString(),password.getText().toString(),
//                                conformpassword.getText().toString() );
//                        if(isInserted == true)
//                            Toast.makeText(RegisterUser.this,"Data Inserted",Toast.LENGTH_LONG).show();
//                        else
//                            Toast.makeText(RegisterUser.this,"Data not Inserted",Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case reg:
                Register_Server();



                break;

        }
    }

    public void Register_Server() {
        final String Firstanme = firstname.getText().toString().trim();
        final String Lasttanme = lastname.getText().toString().trim();

        String email1 = email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email1.matches(emailPattern)) {

            Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
            final String Password = password.getText().toString().trim();
            final String Phone = phone.getText().toString().trim();
            final String conform_pwd = conformpassword.getText().toString().trim();
            Email1=email1;


            String a=Email1;
            if (Firstanme.equals("") || Lasttanme.equals("") || email1.equals("") ||
                    Password.equals("") || Phone.equals("")) {

                Toast.makeText(getApplicationContext(), "Fill All the fields", Toast.LENGTH_SHORT).show();
            } else {
                if (Password.equals(conform_pwd)) {
                    boolean isInserted = databaseHelper.insertStudentData(firstname.getText().toString(),
                            lastname.getText().toString(),
                            email.getText().toString(), password.getText().toString(),
                            conformpassword.getText().toString());

                    if(isInserted){
                        try {
                            String[] mac = databaseHelper.getAllBeaconData();
                            detectedMacId = "";
                            Major = "";
                            Minor = "";
                            UUID = "";
//                            databaseHelper.deleteBeaconData(mac[0]);
                            Toast.makeText(getApplicationContext(), "Succefully Registered", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }catch (NullPointerException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Succefully Registered", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Dublicate Email", Toast.LENGTH_SHORT).show();
                    }



                } else
                    Toast.makeText(getApplicationContext(), "password is not matching", Toast.LENGTH_SHORT).show();
            }

        }
        else
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();



    }
//
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        System.exit(1);

    }


}