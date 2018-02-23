package com.ellipsonic.www.student_tracking.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
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

import com.ellipsonic.www.student_tracking.R;

import static com.ellipsonic.www.student_tracking.ui.activity.RegisterActivity.StudentName;

public class MapNavigationActivty extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView studentname;
    DatabaseHelper databaseHelper;
    CardView card_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navigation_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        databaseHelper = new DatabaseHelper(this);
        card_view = (CardView) findViewById(R.id.card_view1);
        studentname=(TextView)findViewById(R.id.user);

        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] cursor = databaseHelper.getAllBeaconData();
                String student_name=cursor[1];
                Intent myIntent = new Intent(getApplicationContext(), MapsActivity .class);
                myIntent.putExtra("studentname", student_name);
                startActivity(myIntent);
            }
        });


        try {
            String[] mac = databaseHelper.getAllBeaconData();
            studentname.setText("\n"+"  Start Tracking :  "+mac[1]);
        } catch (NullPointerException e) {
             e.printStackTrace();
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signout) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }


//       else if (id == R.id.nav_map) {
//            // Handle the camera action
//            Intent intent = new Intent(getApplicationContext(),Login.class);
//            startActivity(intent);
//            finish();
//        }


        else if(id == R.id.nav_register) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(),HomeBeaconNavigationActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//    @Override
//    public void onBackPressed(){
//
//        startActivity(new Intent(getApplicationContext(), HomeBeaconNavigationActivity.class));
//        finish();
//        System.exit(1);
//
//    }


    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
                startActivity(new Intent(getApplicationContext(), HomeBeaconNavigationActivity.class));

        // android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }
}
