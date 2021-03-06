//package com.ellipsonic.www.student_tracking.ibeaconreference;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.RemoteException;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.ellipsonic.smartstudent.Activity.RegisterActivity;
//import com.ellipsonic.smartstudent.R;
//import com.ellipsonic.smartstudent.ibeacon.IBeaconConsumer;
//import com.ellipsonic.smartstudent.ibeacon.IBeaconManager;
//import com.ellipsonic.smartstudent.ibeacon.MonitorNotifier;
//import com.ellipsonic.smartstudent.ibeacon.Region;
//
//public class BeaconDetactorService extends Service implements IBeaconConsumer {
//
//	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
//
//	@Override
//	public IBinder onBind(Intent arg0) {
//		return null;
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		return START_STICKY;
//	}
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//		iBeaconManager.bind(this);
////
//		final Handler handler = new Handler();
//		final Runnable runnable = new Runnable() {
//
//			@Override
//			public void run() {
//				stopSelf();
//			}
//		};
//		handler.postDelayed(runnable, 10000);
//	}
//
//	@Override
//	public void onDestroy() {
//		iBeaconManager.unBind(this);
//		super.onDestroy();
//	}
//
//	@Override
//	public void onIBeaconServiceConnect() {
//		iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
//			@Override
//			public void didEnterRegion(Region region) {
//				Log.e("BeaconDetactorService", "didEnterRegion");
//				generateNotification(BeaconDetactorService.this, region.getUniqueId()
//						+ ": just saw this iBeacon for the first time");
//			}
//
//			@Override
//			public void didExitRegion(Region region) {
//				Log.e("BeaconDetactorService", "didExitRegion");
//				generateNotification(BeaconDetactorService.this, region.getUniqueId() + ": is no longer visible");
//			}
//
//			@Override
//			public void didDetermineStateForRegion(int state, Region region) {
//				Log.e("BeaconDetactorService", "didDetermineStateForRegion:" + state);
//			}
//
//		});
//
//		try {
//			iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * Issues a notification to inform the user that server has sent a message.
//	 */
//	private static void generateNotification(Context context, String message) {
//
//		Intent launchIntent = new Intent(context, RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
//				0,
//				new NotificationCompat.Builder(context).setWhen(System.currentTimeMillis())
//						.setSmallIcon(R.mipmap.ic_launcher).setTicker(message)
//						.setContentTitle(context.getString(R.string.app_name)).setContentText(message)
//						.setContentIntent(PendingIntent.getActivity(context, 0, launchIntent, 0)).setAutoCancel(true)
//						.build());
//
//	}
//
//}