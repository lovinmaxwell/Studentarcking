//package com.ellipsonic.www.student_tracking.ibeaconreference;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.RemoteException;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.ellipsonic.smartstudent.R;
//import com.ellipsonic.smartstudent.ibeacon.IBeacon;
//import com.ellipsonic.smartstudent.ibeacon.IBeaconConsumer;
//import com.ellipsonic.smartstudent.ibeacon.IBeaconManager;
//import com.ellipsonic.smartstudent.ibeacon.MonitorNotifier;
//import com.ellipsonic.smartstudent.ibeacon.RangeNotifier;
//import com.ellipsonic.smartstudent.ibeacon.Region;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class MonitoringActivity extends Activity implements IBeaconConsumer {
//	protected static final String TAG = "MonitoringActivity";
//	int a=0;
//	int b=0;
//	int c=0;
//	int d=0;
//	int e=0;
//
//	private ListView list = null;
//	private BeaconAdapter adapter = null;
//	private ArrayList<IBeacon> arrayL = new ArrayList<IBeacon>();
//	private LayoutInflater inflater;
//
//	private BeaconServiceUtility beaconUtill = null;
//	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_monitor);
//		beaconUtill = new BeaconServiceUtility(this);
//		list = (ListView) findViewById(R.id.list);
//		adapter = new BeaconAdapter();
//		list.setAdapter(adapter);
//		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//		beaconUtill.onStart(iBeaconManager, this);
//	}
//
//	@Override
//	protected void onStop() {
//		beaconUtill.onStop(iBeaconManager, this);
//		super.onStop();
//	}
//
//	@Override
//	public void onIBeaconServiceConnect() {
//
//		iBeaconManager.setRangeNotifier(new RangeNotifier() {
//			@Override
//			public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
//
//				arrayL.clear();
//				arrayL.addAll((ArrayList<IBeacon>) iBeacons);
//				adapter.notifyDataSetChanged();
//			}
//
//		});
//
//		iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
//			@Override
//			public void didEnterRegion(Region region) {
//				Log.e("BeaconDetactorService", "didEnterRegion");
//				// logStatus("I just saw an iBeacon for the first time!");
//			}
//
//			@Override
//			public void didExitRegion(Region region) {
//				Log.e("BeaconDetactorService", "didExitRegion");
//				// logStatus("I no longer see an iBeacon");
//			}
//
//			@Override
//			public void didDetermineStateForRegion(int state, Region region) {
//				Log.e("BeaconDetactorService", "didDetermineStateForRegion");
//				// logStatus("I have just switched from seeing/not seeing iBeacons: " + state);
//			}
//
//		});
//
//		try {
//			iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//
//		try {
//			iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private class BeaconAdapter extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			if (arrayL != null && arrayL.size() > 0)
//				return arrayL.size();
//			else
//				return 0;
//		}
//
//		@Override
//		public IBeacon getItem(int arg0) {
//			return arrayL.get(arg0);
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			return arg0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			try {
//				ViewHolder holder;
//
//				if (convertView != null) {
//					holder = (ViewHolder) convertView.getTag();
//				} else {
//					holder = new ViewHolder(convertView = inflater.inflate(R.layout.tupple_monitoring, null));
//				}
//				if (arrayL.get(position).getProximityUuid() != null)
//					holder.beacon_uuid.setText("UUID: " + arrayL.get(position).getProximityUuid());
//
//				holder.beacon_major.setText("Major: " + arrayL.get(position).getMajor());
//
//				holder.beacon_minor.setText(", Minor: " + arrayL.get(position).getMinor());
//
//				holder.beacon_proximity.setText("Proximity: " + arrayL.get(position).getProximity());
//
//				holder.beacon_rssi.setText(", Rssi: " + arrayL.get(position).getRssi());
//
//				holder.beacon_txpower.setText(", TxPower: " + arrayL.get(position).getTxPower());
//
//				//holder.beacon_range.setText("" + arrayL.get(position).getAccuracy());
//
//				if(arrayL.get(position).getMajor()==1)
//				{
//					a=a+1;
//					holder.time.setText("Dwelling Time for Guest A:" + a+ " Seconds");
//				}
//
//				if(arrayL.get(position).getMajor()==0)
//				{
//					b=b+1;
//					holder.time.setText("Dwelling Time for Guest B:" + b+ " Seconds");
//				}
//				if(arrayL.get(position).getMajor()==2)
//				{
//					c=c+1;
//					holder.time.setText("Dwelling Time for Guest C:" + c+ " Seconds");
//				}
//				if(arrayL.get(position).getMajor()==4)
//				{
//					d=d+1;
//					holder.time.setText("Dwelling Time for Guest D:" + d+ " Seconds");
//				}
//				if(arrayL.get(position).getMajor()==112)
//				{
//					e=e+1;
//					holder.time.setText("Dwelling Time for Guest E:" + e+ " Seconds");
//				}
//
//
//			/*	if(position==3){
//					//20
//					int onebminor=arrayL.get(1).getMinor();
//					holder.beacon_number.setText("Quadrant 1");
//
//
//
//				}
//				else if(position==2){
//					//21
//					int twobminor=arrayL.get(2).getMinor();
//					holder.beacon_number.setText("Quadrant 2");
//
//				}
//				else if(position==1){
//					//10
//					int threebminor=arrayL.get(3).getMinor();
//					holder.beacon_number.setText("Quadrant 3");
//
//				}
//				else if(position==0){
//					//22
//					int fourbminor=arrayL.get(4).getMinor();
//					holder.beacon_number.setText("Quadrant 4");
//
//				}
//				double onebacc=arrayL.get(3).getAccuracy();
//				double twobacc=arrayL.get(2).getAccuracy();
//				double threebacc=arrayL.get(1).getAccuracy();
//				double fourbacc =arrayL.get(0).getAccuracy();
//				double smallest;
//				if(onebacc < twobacc && onebacc<threebacc && onebacc<fourbacc){
//					smallest = onebacc;
//					holder.beacon_number.setText("Quadrant 1");
//
//				}else if(twobacc<threebacc && twobacc<onebacc && twobacc<fourbacc){
//					smallest = twobacc;
//					holder.beacon_number.setText("Quadrant 2");
//
//				}else if(threebacc<twobacc && threebacc<onebacc && threebacc<fourbacc){
//					smallest = threebacc;
//					holder.beacon_number.setText("Quadrant 3");
//
//				}else{
//					smallest = fourbacc;
//					holder.beacon_number.setText("Quadrant 4");
//
//				}*/
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			return convertView;
//		}
//
//		private class ViewHolder {
//			private TextView beacon_uuid;
//			private TextView beacon_major;
//			private TextView beacon_minor;
//			private TextView beacon_proximity;
//			private TextView beacon_rssi;
//			private TextView beacon_txpower;
//		//	private TextView beacon_range;
//		//	private TextView beacon_number;
//			private TextView time;
//
//
//			public ViewHolder(View view) {
//				beacon_uuid = (TextView) view.findViewById(R.id.BEACON_uuid);
//				beacon_major = (TextView) view.findViewById(R.id.BEACON_major);
//				beacon_minor = (TextView) view.findViewById(R.id.BEACON_minor);
//				beacon_proximity = (TextView) view.findViewById(R.id.BEACON_proximity);
//				beacon_rssi = (TextView) view.findViewById(R.id.BEACON_rssi);
//				beacon_txpower = (TextView) view.findViewById(R.id.BEACON_txpower);
//			//	beacon_range = (TextView) view.findViewById(R.id.BEACON_range);
//			//	beacon_number = (TextView) view.findViewById(R.id.number);
//				time=(TextView)view.findViewById(R.id.number);
//				view.setTag(this);
//			}
//		}
//
//	}
//
//}