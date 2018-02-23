//package com.ellipsonic.www.student_tracking.ibeaconreference;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import com.ellipsonic.smartstudent.R;
//import com.ellipsonic.smartstudent.ibeacon.IBeaconManager;
//
//
//public class I_MainActivity extends Activity {
//	protected static final String TAG = "I_MainActivity";
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		Log.d(TAG, "oncreate");
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main_one);
//		verifyBluetooth();
//
//	}
//
//	public void onMonitoringClicked(View view) {
//		Intent myIntent = new Intent(this, com.ellipsonic.smartstudent.Activity.RegisterActivity.class);
//		this.startActivity(myIntent);
//	}
//
//	private void verifyBluetooth() {
//
//		try {
//			if (!IBeaconManager.getInstanceForApplication(this).checkAvailability()) {
//				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setTitle("Bluetooth not enabled");
//				builder.setMessage("Please enable bluetooth in settings and restart this application.");
//				builder.setPositiveButton(android.R.string.ok, null);
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            finish();
//                            System.exit(0);
//                        }
//
//                    });
//				}
//				builder.show();
//
//			}
//		} catch (RuntimeException e) {
//			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("Bluetooth LE not available");
//			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
//			builder.setPositiveButton(android.R.string.ok, null);
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                        finish();
//                        System.exit(0);
//                    }
//
//                });
//			}
//			builder.show();
//
//		}
//
//	}
//
//}
