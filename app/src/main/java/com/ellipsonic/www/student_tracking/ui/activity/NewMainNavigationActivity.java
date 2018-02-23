package com.ellipsonic.www.student_tracking.ui.activity;



/*
 *
 *  Copyright (c) 2015 SameBits UG. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */



        import android.annotation.TargetApi;
        import android.app.Dialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.NavigationView;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;
        import com.ellipsonic.www.student_tracking.Student_Tracking;
        import com.ellipsonic.www.student_tracking.R;
        import com.ellipsonic.www.student_tracking.data.NewDbStoreService;
        import com.ellipsonic.www.student_tracking.model.TrackedBeacon;
        import com.ellipsonic.www.student_tracking.ui.fragment.DetectedBeaconsFragment;
        import com.ellipsonic.www.student_tracking.ui.fragment.ScanFragment;
        import com.ellipsonic.www.student_tracking.ui.fragment.TrackedBeaconsFragment;
        import com.ellipsonic.www.student_tracking.util.Constants;
        import com.ellipsonic.www.student_tracking.util.DialogBuilder;

        import org.altbeacon.beacon.BeaconManager;

        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

        import butterknife.Bind;
        import butterknife.ButterKnife;
        import butterknife.OnClick;

        import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.Major;
        import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.Minor;
        import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.UUID;
        import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.detectedMacId;

public class NewMainNavigationActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    NewDbStoreService newDbStoreService;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Bind(R.id.button)
    Button button;
    private static final String url = "http://wisdomkraft.com/wisdomkraft.com/prudence/IoT/StudentTracking/Data_in_json.php";

    String text="";

    String s_name,s_school,s_age,s_std;

    BeaconManager mBeaconManager;
    TrackedBeacon mBeacon;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, NewMainNavigationActivity.class);
    }

    @OnClick(R.id.fab)
    void navAction() {

        Fragment currentFragment = getFragmentInstance(R.id.content_frame);
       // String tag = currentFragment.getTag();
        ((ScanFragment) currentFragment).scanStartStopAction();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main_navigation);
        ButterKnife.bind(this);

      //  setupToolbar();
        s_name = getIntent().getStringExtra("S_NAME");
        s_school = getIntent().getStringExtra("S_SCHOOL");
        s_age = getIntent().getStringExtra("S_AGE");
        s_std = getIntent().getStringExtra("S_STD");



        navigationView.setNavigationItemSelectedListener(this);

        mBeaconManager = Student_Tracking.from(this).getComponent().beaconManager();

      //  checkPermissions();
        verifyBluetooth();

      //  readExtras();

        if (null == savedInstanceState || mBeacon != null) {
            //launchTrackedListView();
        }

        launchScanBeaconView();

        button.setOnClickListener(this);
   }



    public void onClick(View v) {
        if(v == button){

            String a=detectedMacId;
            String b=a;
            NewDbStoreService newDbStoreService=new NewDbStoreService(getApplicationContext());
            List<TrackedBeacon> obj=newDbStoreService.getBeacons();
            String str="";
            for(TrackedBeacon object: obj) {
                str = object.getBluetoothAddress().trim();
            }
            String s = str.replace(":", "");


            Intent myIntent = new Intent(this,RegisterActivity .class);
            myIntent.putExtra("MacID", a);
            myIntent.putExtra("S_NAME",s_name);
            myIntent.putExtra("S_SCHOOL", s_school);
            myIntent.putExtra("S_AGE",s_age);
            myIntent.putExtra("S_STD",s_std);
            try {
                if(getIntent().getStringExtra("Edit-Registaration").equals("TRUE")){
                    myIntent.putExtra("Edit-Registaration", "TRUE");
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            startActivity(myIntent);

        }
    }


    //@OnClick(R.id.button)

//    public void registerUser(){
//
//        NewDbStoreService newDbStoreService=new NewDbStoreService(getApplicationContext());
//        List<TrackedBeacon> obj=newDbStoreService.getBeacons();
//        String str="";
//        for(TrackedBeacon object: obj) {
//            str = object.getBluetoothAddress().trim();
//        }
//        String s = str.replace(":", "");
////
////        Intent intent = new Intent(current.this, RegisterActivity.class);
////        intent.putExtra("str",s);
////        startActivity(intent);
//        Intent myIntent = new Intent(this, RegisterActivity.class);
//        myIntent.putExtra("MacID", s);
//        startActivity(myIntent);
//
//        //Intent registerIntent = new Intent();
////
////        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
////                new Response.Listener<String>() {
////                    @Override
////                    public void onResponse(String response) {
////                        Toast.makeText(NewMainNavigationActivity.this,response, Toast.LENGTH_LONG).show();
////                    }
////                },
////                new Response.ErrorListener() {
////                    @Override
////                    public void onErrorResponse(VolleyError error) {
////                        Toast.makeText(NewMainNavigationActivity.this,error.toString(),Toast.LENGTH_LONG).show();
////                    }
////                }){
////            @Override
////
////            protected Map<String,String> getParams(){
////
////                NewDbStoreService newDbStoreService=new NewDbStoreService(getApplicationContext());
////                List<TrackedBeacon> obj=newDbStoreService.getBeacons();
////                String str="";
////            for(TrackedBeacon object: obj) {
////                str = object.getBluetoothAddress().trim();
////            }
////
////                String s = str.replace(":", "");
//
////                Map<String,String> params = new HashMap<String, String>();
////                params.put("mac",s);
////                return params;
////            }
////
////        };
////
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
////        requestQueue.add(stringRequest);
//    }


//    protected void readExtras() {
//        Intent intent = getIntent();
//        if (intent.getExtras() != null) {
//            mBeacon = intent.getExtras().getParcelable(Constants.ARG_BEACON);
//        }
//    }

    private void setupToolbar() {

//        setSupportActionBar(toolbar);

//        final ActionBar ab = getSupportActionBar();
//        if (ab != null) {
//            ab.setDisplayHomeAsUpEnabled(true);
//        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(Constants.TAG, "coarse location permission granted");
                } else {
                    final Dialog permFailedDialog = DialogBuilder.createSimpleOkErrorDialog(
                            this,
                            getString(R.string.dialog_error_functionality_limited),
                            getString(R.string.error_message_location_access_not_granted)
                    );
                    permFailedDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            //finish();
                        }
                    });
                    permFailedDialog.show();
                }
                return;
            }
        }
    }


    @TargetApi(18)
    private void verifyBluetooth() {

        try {
            if (!mBeaconManager.checkAvailability()) {

                final Dialog bleDialog = DialogBuilder.createSimpleOkErrorDialog(
                        this,
                        getString(R.string.dialog_error_ble_not_enabled),
                        getString(R.string.error_message_please_enable_bluetooth)
                );
                bleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        System.exit(0);
                    }
                });
                bleDialog.show();

            }
        } catch (RuntimeException e) {

            final Dialog bleDialog = DialogBuilder.createSimpleOkErrorDialog(
                    this,
                    getString(R.string.dialog_error_ble_not_supported),
                    getString(R.string.error_message_bluetooth_le_not_supported)
            );
            bleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }
            });
            bleDialog.show();
        }
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_scan_around:
                launchScanBeaconView();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void addScanBeaconFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            if (checkFragmentInstance(R.id.content_frame, DetectedBeaconsFragment.class) == null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, DetectedBeaconsFragment.newInstance(), Constants.TAG_FRAGMENT_SCAN_LIST)
                        .commit();

            }
        }

    }





    public void launchScanBeaconView() {
        addScanBeaconFragment();
    }




    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), HomeBeaconNavigationActivity.class));
        finish();
    }

}
