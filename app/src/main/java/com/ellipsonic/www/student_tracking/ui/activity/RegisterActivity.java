package com.ellipsonic.www.student_tracking.ui.activity;

import android.Manifest;
import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ellipsonic.www.student_tracking.R;
import com.ellipsonic.www.student_tracking.data.NewDbStoreService;
import com.ellipsonic.www.student_tracking.database.SharedPreference;
import com.ellipsonic.www.student_tracking.model.TrackedBeacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.Major;
import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.Minor;
import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.UUID;
import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.detectedMacId;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    static String uuid_beacon = null;

    TextInputEditText MacID;

    private static final String url = "http://wisdomkraft.com/wisdomkraft.com/prudence/IoT/StudentTracking/Data_in_json.php";
    private SharedPreference sharedPreference = new SharedPreference();


    protected static final String TAG = "RegisterActivity";
    public static String StudentName;
    public static String SchooltName;
    public static String mac1;
    public static String StudentAge;
    public static String StudentStandard;
    private ActionBar actionBar;
    DatabaseHelper databaseHelper;
    Button register_User;
   public static  TextInputEditText student_name, student_school,
            student_age, student_standard;

    EditText student_beaconMac;

    //Permision code that will be checked in the method onRequestPermissionsResult
    private int STORAGE_PERMISSION_CODE = 23;

    String studentMacIdFromIntent;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                BluetoothAdapter.getDefaultAdapter().enable();
            }
        }

        //First checking if the app is already having the permission
//        if (isReadStorageAllowed()) {
//            //If permission is already having then showing the toast
//            Toast.makeText(RegisterActivity.this, "You already have the permission", Toast.LENGTH_LONG).show();
//            //Existing the method with return
//            return;
//        }

        //If the app has not the permission then asking for the permission
//        requestStoragePermission();

        student_name = (TextInputEditText) findViewById(R.id.student_name);
        student_school = (TextInputEditText) findViewById(R.id.student_school);
        student_beaconMac = (EditText) findViewById(R.id.student_mac);
        student_age = (TextInputEditText) findViewById(R.id.student_age);
        student_standard = (TextInputEditText) findViewById(R.id.student_standard);
        findViewById(R.id.register_btn).setOnClickListener(this);

        student_beaconMac.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        Intent intent1 = getIntent();
        try {
            if (!intent1.equals(null)) {
                studentMacIdFromIntent = intent1.getStringExtra("MacID");

                String sName = intent1.getStringExtra("S_NAME");
                String sSchol = intent1.getStringExtra("S_SCHOOL");
                String sAge = intent1.getStringExtra("S_AGE");
                String sStd = intent1.getStringExtra("S_STD");

                student_name.setText(sName);
                student_school.setText(sSchol);
                student_age.setText(sAge);
                student_standard.setText(sStd);
                student_beaconMac.setText(studentMacIdFromIntent);

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try{
            String intentReg = intent1.getStringExtra("Edit-Registaration");
            if(intentReg.equals("TRUE")){
                String[] cursor = databaseHelper.getAllBeaconData();
                student_beaconMac.setText(cursor[0]);
                student_name.setText(cursor[1]);
                student_school.setText(cursor[2]);
                student_age.setText(cursor[3]);
                student_standard.setText(cursor[4]);
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                Server();
                break;
            case R.id.student_mac:

                String editIntent = "NIL";
                try {
                    if (getIntent().getStringExtra("Edit-Registaration").equals("TRUE")) {
                        Intent intent = new Intent(RegisterActivity.this, NewMainNavigationActivity.class);
                        intent.putExtra("S_NAME", student_name.getText().toString().trim());
                        intent.putExtra("S_NAME", student_name.getText().toString().trim());
                        intent.putExtra("S_SCHOOL", student_school.getText().toString().trim());
                        intent.putExtra("S_AGE", student_age.getText().toString().trim());
                        intent.putExtra("S_STD", student_standard.getText().toString().trim());
                        intent.putExtra("Edit-Registaration", "TRUE");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(RegisterActivity.this, NewMainNavigationActivity.class);
                    intent.putExtra("S_NAME", student_name.getText().toString().trim());
                    intent.putExtra("S_NAME", student_name.getText().toString().trim());
                    intent.putExtra("S_SCHOOL", student_school.getText().toString().trim());
                    intent.putExtra("S_AGE", student_age.getText().toString().trim());
                    intent.putExtra("S_STD", student_standard.getText().toString().trim());
                    intent.putExtra("Edit-Registaration", "TRUE");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                break;
        }
    }



//        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(RegisterActivity.this,response, Toast.LENGTH_LONG).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(RegisterActivity.this,error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                }){
//            @Override

//            protected Map<String,String> getParams(){
//
//                NewDbStoreService newDbStoreService=new NewDbStoreService(getApplicationContext());
//                List<TrackedBeacon> obj=newDbStoreService.getBeacons();
//                String str="";
//            for(TrackedBeacon object: obj) {
//                str = object.getBluetoothAddress().trim();
//            }
//
//               String s = str.replace(":", "");
//               // String s="0CF3EE09392B";
//              //  s="0CF3EE09392B";
//                Map<String,String> params = new HashMap<String, String>();
//             //   params.put("mac","NAME"+studentName+" SCHOOL"+schoolName+" MAC="+s+" AGE="+studentAge);
//                params.put("mac","0CF3EE09392B");
////                params.put("mac",s);
////                params.put("mac",s);
////                params.put("mac",s);
////                params.put("mac",s);
//                return params;
//            }
//
//        };

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);



    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH);
        int result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }



//    private void startBeaconService() {
//
//
//    }

//    private void register_student() {
//
//        String studentName = student_name.getText().toString().trim();
//        String schoolName = student_school.getText().toString().trim();
//        String studentAge = student_age.getText().toString().trim();
//        String studentStandard = student_standard.getText().toString().trim();
//        String studentMacAdd = student_beaconMac.getText().toString().trim();
//        Intent intent1 = getIntent();
//        studentMacIdFromIntent = intent1.getStringExtra("MacID");
//
//        if (studentName.equals(null) || schoolName.equals(null) || studentMacIdFromIntent.equals(null) || studentAge.equals(null) || studentStandard.equals(null) ||
//                studentName.equals("") || schoolName.equals("") || studentAge.equals("") || studentMacIdFromIntent.equals("") || studentStandard.equals("")) {
//
//            Toast.makeText(getApplicationContext(), "Fill All the fields", Toast.LENGTH_SHORT).show();
//
//        } else if(getIntent().getStringExtra("Edit-Registaration").equals("TRUE")) {
//
//           boolean updateBeacon =  databaseHelper.updateBeaconData(studentName,schoolName
//                   ,studentMacAdd,studentAge,studentStandard);
//
//            if(updateBeacon){
//                startActivity(new Intent(getApplicationContext(),Home_Beacon.class));
//            }
//
//
//        }else  {
//
//            Toast.makeText(getApplicationContext(), "Succefully Registered", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(this, MapsActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
//    }

    public void Server() {

        final String studentName = student_name.getText().toString().trim();
        final String schoolName = student_school.getText().toString().trim();
        final String studentAge = student_age.getText().toString().trim();
        final String studentStandard = student_standard.getText().toString().trim();
        final String student_beaconValue = student_beaconMac.getText().toString().trim();

        String editIntent = "NIL";
        try {
            if (getIntent().getStringExtra("Edit-Registaration").equals("TRUE"))
                editIntent = "true";
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        StudentName = studentName;
        SchooltName = schoolName;
        mac1 = student_beaconValue;
        StudentAge = studentAge;
        StudentStandard = studentStandard;

        if (studentName.equals("") || schoolName.equals("") || studentAge.equals("")
                || student_beaconValue.equals("") || studentStandard.equals("")) {

            Toast.makeText(getApplicationContext(), "Fill All the fields", Toast.LENGTH_SHORT).show();

        } else {
            if (editIntent.equals("true")) {

                String macFromDb = "NIL";
                try{
                    String arr[] = databaseHelper.getAllBeaconData();
                    macFromDb = arr[0];
                }catch (NullPointerException e){
                    e.printStackTrace();
                    macFromDb = "NIL";
                }

                try {

                    if(macFromDb.equals("NIL")){
                        boolean isInserted = databaseHelper.insertBeaconData(
                                studentName,
                                schoolName,
                                student_beaconValue,
                                studentAge,
                                studentStandard);
                        if (isInserted) {
                            Toast.makeText(getApplicationContext(), "Succefully Registered", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(this, HomeBeaconNavigationActivity.class);
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            myIntent.putExtra("BeaconMACID",
                                    "MACID=" + detectedMacId + "\n" +
                                            "UUID=" + UUID + "\n" +
                                            "Major=" + Major + "\n" +
                                            "Minior=" + Minor);
                            startActivity(myIntent);
                        }

                    }else if(macFromDb.equals(student_beaconValue)){
                        boolean updateBeacon = databaseHelper.updateBeaconData(studentName, schoolName
                                , student_beaconValue, studentAge, studentStandard);

                        if (updateBeacon) {
                            startActivity(new Intent(getApplicationContext(), HomeBeaconNavigationActivity.class));
                        }
                    }else {
                        databaseHelper.deleteBeaconData();
                        boolean isInserted = databaseHelper.insertBeaconData(
                                studentName,
                                schoolName,
                                student_beaconValue,
                                studentAge,
                                studentStandard);
                        if (isInserted) {
                            Toast.makeText(getApplicationContext(), "Succefully Registered", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(this, HomeBeaconNavigationActivity.class);
                           // myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            myIntent.putExtra("BeaconMACID",
                                    "MACID=" + detectedMacId + "\n" +
                                            "UUID=" + UUID + "\n" +
                                            "Major=" + Major + "\n" +
                                            "Minior=" + Minor);
                            startActivity(myIntent);

                        }
                    }

                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            } else {
                boolean isInserted = databaseHelper.insertBeaconData(
                        studentName,
                        schoolName,
                        student_beaconValue,
                        studentAge,
                        studentStandard);

                if (isInserted) {
                    Toast.makeText(getApplicationContext(), "Succefully Registered", Toast.LENGTH_SHORT).show();
                    String bmac = detectedMacId;
                    String buid = UUID;
                    String bmajor = Major;
                    String bminior = Minor;

                    Intent myIntent = new Intent(this, HomeBeaconNavigationActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    myIntent.putExtra("BeaconMACID",
                            "MACID=" + bmac + "\n" +
                                    "UUID=" + buid + "\n" +
                                    "Major=" + bmajor + "\n" +
                                    "Minior=" + bminior);
                    startActivity(myIntent);

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }



    }






//
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(), HomeBeaconNavigationActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//
//        finish();
//        System.exit(1);
//
//    }


    @Override
    public void onBackPressed(){

        startActivity(new Intent(getApplicationContext(), HomeBeaconNavigationActivity.class));
        finish();
        System.exit(1);

    }




}

