package com.ellipsonic.www.student_tracking.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ellipsonic.www.student_tracking.R;

import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.Major;
import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.Minor;
import static com.ellipsonic.www.student_tracking.model.DetectedBeacon.UUID;
import static com.ellipsonic.www.student_tracking.ui.activity.RegisterActivity.StudentName;

public class HomeBeaconNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    DatabaseHelper databaseHelper;
    String MacID = "";
    CardView card_view;
    private LayoutInflater inflater;
    String MACID;
    TextView mac;
    String macIdFromDb = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDeviceOnline(getApplicationContext());
        setContentView(R.layout.activity_home_beacon_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mac = (TextView) findViewById(R.id.mac);
        card_view = (CardView) findViewById(R.id.card_view);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            String[] macid =  databaseHelper.getAllBeaconData();
            MacID = macid[0];
        }catch (NullPointerException e){
            e.printStackTrace();
            MacID = "";
        }
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!MacID.equals("")) {
                    Toast.makeText(getApplicationContext(), "Already Registerd", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Register Your Beacon", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });

        try {
            String[] beaconData =  databaseHelper.getAllBeaconData();

            String cardData = "MACID                      :"+beaconData[0]+"\n"+
                              "Student Name        :"+beaconData[1]+"\n"+
                              "School Name          :"+beaconData[2]+"\n"+
                              "Student Age            :"+beaconData[3]+"\n"+
                              "Student Standard  :"+beaconData[4]+"\n";
            mac.setText(cardData);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }

    }


    public static boolean isDeviceOnline(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());
        if(!isOnline)
            Toast.makeText(context, " No internet Connection ", Toast.LENGTH_SHORT).show();

        return isOnline;
    }





    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {
            String[] cursor = databaseHelper.getAllBeaconData();

            try {
                if (!cursor[0].equals(null)) {
                    macIdFromDb = cursor[0];
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            Log.d("Mac-Id", macIdFromDb);

            if (!macIdFromDb.equals("")) {

                String student_name = StudentName;
                Intent myIntent = new Intent(this, MapNavigationActivty.class);
                myIntent.putExtra("studentname", student_name);
                startActivity(myIntent);
            } else {

                Toast.makeText(getApplicationContext(), "Please Register Your Beacon", Toast.LENGTH_SHORT).show();

            }

        } else if (id == R.id.nav_edit) {

            String[] cursor = databaseHelper.getAllBeaconData();
            try {
                if (!cursor[0].equals(null)) {
                    macIdFromDb = cursor[0];
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (!macIdFromDb.equals("")) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("Edit-Registaration","TRUE");
                startActivity(intent);

            } else {
                Toast.makeText(getApplicationContext(), "Please Register your beacon", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_signout) {
            Intent intent = new Intent(getApplicationContext(), com.ellipsonic.www.student_tracking.ui.activity.Login.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
