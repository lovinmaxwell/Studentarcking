package com.ellipsonic.www.student_tracking.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ellipsonic.www.student_tracking.Modules.DirectionFinder;
import com.ellipsonic.www.student_tracking.Modules.DirectionFinderListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ellipsonic.www.student_tracking.R;
import com.ellipsonic.www.student_tracking.database.SharedPreference;

//import org.json.JSONArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ellipsonic.www.student_tracking.Modules.Distance;
import com.ellipsonic.www.student_tracking.Modules.Duration;
import com.ellipsonic.www.student_tracking.Modules.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,DirectionFinderListener{
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener {

    private static final String url = "http://wisdomkraft.com/wisdomkraft.com/prudence/IoT/StudentTracking/Data_in_json.php";


    DatabaseHelper databaseHelper;
    public static String LastSeen;
    String studentname3;
    Button student_info;
    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    Thread track_bus;
    Button logoutBtn;
    ScheduledExecutorService scheduleTaskExecutor;
    Marker myMarker = null;
    SharedPreference sharedPreference;
    private com.android.volley.toolbox.Volley Volley;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sharedPreference = new SharedPreference();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        student_info = (Button) findViewById(R.id.student_info);

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkLocationPermission();
//        }
        // Initializing
        MarkerPoints = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        Drawable circleDrawable = getResources().getDrawable(R.drawable.school_public);
//        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
//
//        LatLng latLng = new LatLng(12.9771, 77.5525);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//        MarkerPoints.add(latLng);
//
//        start_backgroundTask();
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("BNM public School");
//        markerOptions.icon(markerIcon);
//        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap = googleMap;
        LatLng hcmus = new LatLng(12.9771, 77.5525);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Đại học Khoa học tự nhiên")
                .position(hcmus)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        start_backgroundTask();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduleTaskExecutor != null) {
            scheduleTaskExecutor.shutdown();
        }

    }

    private void start_backgroundTask() {
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        //  This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                server();

                //   Log.d("Url",url_map);
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void server() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(MapsActivity.this,response, Toast.LENGTH_LONG).show();
                        JSONArray jsonObj = null;
                        try {
                            double lastSeen;
                            jsonObj = new JSONArray(response);
                            JSONObject resultObj = jsonObj.getJSONObject(0);
                            double latitude = 0.0, longitude = 0.0;
                            latitude = Double.parseDouble(resultObj.getString("LATITUDE"));
                            longitude = Double.parseDouble(resultObj.getString("LONGITUDE"));
                            LastSeen = String.valueOf(resultObj.getString("TIME"));
//                            display_busLocation(latitude, longitude);
                            LatLng originAddress = new LatLng(12.9771, 77.5525);
                            LatLng destinationAddress = new LatLng(latitude,longitude);
                            try {
                                new DirectionFinder(MapsActivity.this, originAddress, destinationAddress).execute();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }



                            Intent intent1 = getIntent();
                            try {
                                if (!intent1.equals(null)) {
                                    String[] cursor = databaseHelper.getAllBeaconData();
                                    //   studentname3 = intent1.getStringExtra("studentname");
                                    student_info.setText("Now We are Tracking " + " " + cursor[1] + "\n" +
                                            "LastSeen is " + "  " + LastSeen);

                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override

            protected Map<String, String> getParams() {
                String[] cursor = databaseHelper.getAllBeaconData();
                Map<String, String> params = new HashMap<String, String>();
                params.put("mac", cursor[0]);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


//    private void display_busLocation(double lat, double lon) {
//
//        // Already two locations
//        if (MarkerPoints.size() >= 2) {
//            MarkerPoints.remove(2);
//            MarkerPoints.add(new LatLng(lat, lon));
//            myMarker.remove();
//        } else {
//            MarkerPoints.add(new LatLng(lat, lon));
//        }
//        LatLng point = new LatLng(lat, lon);
//        // Adding new item to the ArrayList
//        MarkerPoints.add(point);
//
//        Drawable circleDrawable = getResources().getDrawable(R.drawable.child);
//        BitmapDescriptor icon = getMarkerIconFromDrawable(circleDrawable);
//
//        // Creating MarkerOptions
//        MarkerOptions options = new MarkerOptions();
//        options.position(point);
//        options.title(sharedPreference.getValue(this, "STUDENT_NAME"));
//        options.icon(icon);
//        myMarker = mMap.addMarker(options);
////            if (MarkerPoints.size() == 2) {
////                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
////                options.icon(icon);
////            }
////        mMap.addMarker(options);
//        // Checks, whether start and end locations are captured
//        if (MarkerPoints.size() == 2) {
//            LatLng origin = MarkerPoints.get(0);
//            LatLng dest = MarkerPoints.get(1);
//
//            // Getting URL to the Google Directions API
//            String url = getUrl(origin, dest);
//            FetchUrl FetchUrl = new FetchUrl();
//
//            // Start downloading json data from Google Directions API
//                FetchUrl.execute(url);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//            // Move the camera instantly to Sydney with a zoom of 15.
//            //      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 15));
//
//// Zoom in, animating the camera.
//            //       mMap.animateCamera(CameraUpdateFactory.zoomIn());
//
//// Zoom out to zoom level 10, animating with a duration of 2 seconds.
//            //    mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//
//// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
////                CameraPosition cameraPosition = new CameraPosition.Builder()
////                        .target(dest)      // Sets the center of the map to Mountain View
////                        .zoom(17)                   // Sets the zoom
////                        .bearing(90)                // Sets the orientation of the camera to east
////                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
////                        .build();                   // Creates a CameraPosition from the builder
////                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        }
//    }
//
//    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
//        Canvas canvas = new Canvas();
//        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
//                Bitmap.Config.ARGB_8888);
//        canvas.setBitmap(bitmap);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        drawable.draw(canvas);
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }
//
//
//    private String getUrl(LatLng origin, LatLng dest) {
//
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//        // Sensor enabled
//        String sensor = "sensor=false";
//
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//
//        return url;
//    }
//
//    /**
//     * A method to download json data from url
//     */
//
//    private String downloadUrl(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
//
//            // Creating an http connection to communicate with url
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//            // Connecting to url
//            urlConnection.connect();
//
//            // Reading data from url
//            iStream = urlConnection.getInputStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//            StringBuffer sb = new StringBuffer();
//
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            data = sb.toString();
//            Log.d("downloadUrl", data.toString());
//            br.close();
//
//        } catch (Exception e) {
//            Log.d("Exception", e.toString());
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }
//
//
//
//
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//    }
//
//    // Fetches data from url passed
//    private class FetchUrl extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... url) {
//
//            // For storing data from web service
//            String data = "";
//
//            try {
//                // Fetching the data from web service
//                data = downloadUrl(url[0]);
//                Log.d("Background Task data", data.toString());
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
////            ParserTask parserTask = new ParserTask();
////            // Invokes the thread for parsing the JSON data
////            parserTask.execute(result);
//            try {
//                onDirectionFinderSuccess(parseJSon(result));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    private List<Route> parseJSon(String data) throws JSONException {
//        if (data == null)
//            return null;
//
//        List<Route> routes = new ArrayList<Route>();
//        JSONObject jsonData = new JSONObject(data);
//        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
//        for (int i = 0; i < jsonRoutes.length(); i++) {
//            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
//            Route route = new Route();
//
//            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
//            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
//            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
//            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
//            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
//            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
//            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");
//
//            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
//            route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
//            route.endAddress = jsonLeg.getString("end_address");
//            route.startAddress = jsonLeg.getString("start_address");
//            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
//            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
//            route.points = decodePolyLine(overview_polylineJson.getString("points"));
//
//            routes.add(route);
//        }
////        listener.onDirectionFinderSuccess(routes);
//        return  routes;
//    }
//
//    public void onDirectionFinderSuccess(List<Route> routes) {
//        polylinePaths = new ArrayList<>();
//        originMarkers = new ArrayList<>();
//        destinationMarkers = new ArrayList<>();
//
//        for (Route route : routes) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
//            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
//            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
//
//            originMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
//                    .title(route.startAddress)
//                    .position(route.startLocation)));
//            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
//                    .title(route.endAddress)
//                    .position(route.endLocation)));
//
//            PolylineOptions polylineOptions = new PolylineOptions().
//                    geodesic(true).
//                    color(Color.BLUE).
//                    width(10);
//
//            for (int i = 0; i < route.points.size(); i++)
//                polylineOptions.add(route.points.get(i));
//
//            polylinePaths.add(mMap.addPolyline(polylineOptions));
//        }
//    }
//
//    private List<LatLng> decodePolyLine(final String poly) {
//        int len = poly.length();
//        int index = 0;
//        List<LatLng> decoded = new ArrayList<LatLng>();
//        int lat = 0;
//        int lng = 0;
//
//        while (index < len) {
//            int b;
//            int shift = 0;
//            int result = 0;
//            do {
//                b = poly.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//
//            shift = 0;
//            result = 0;
//            do {
//                b = poly.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            decoded.add(new LatLng(
//                    lat / 100000d, lng / 100000d
//            ));
//        }
//
//        return decoded;
//    }
//
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//
//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Asking user if explanation is needed
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted. Do the
//                    // contacts-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                        //    mMap.setMyLocationEnabled(true);
//                    }
//                } else {
//                    // Permission denied, Disable the functionality that depends on this permission.
//                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//    }
//
//    public class GetBusPoints extends AsyncTask<String, Void, String> {
//        String server_response;
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            InputStream input_stream = null;
//
//            try {
//                URL url = new URL(strings[0]);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.connect();
//                int response = conn.getResponseCode();
//                input_stream = conn.getInputStream();
//                server_response = readStream(input_stream);
//                return server_response;
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (input_stream != null) {
//                    try {
//                        input_stream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String stringRequest) {
//            super.onPostExecute(stringRequest);
//
//            if (!stringRequest.equals(null)) {
//                try {
//                    JSONArray jsonObj = new JSONArray(server_response);
//                    if (jsonObj.length() != 0) {
//                        JSONObject resultObj = null;
//                        double latitude = 0.0, longitude = 0.0;
//                        try {
//                            resultObj = new JSONObject(jsonObj.get(0).toString());
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                        }
//                        if (!resultObj.equals(null)) {
//                            latitude = Double.parseDouble(resultObj.getString("LATITUDE"));
//                            longitude = Double.parseDouble(resultObj.getString("LONGITUDE"));
//                        }
//                        if (latitude != 0.0 && longitude != 0.0) {
//                            display_busLocation(latitude, longitude);
//                            //    String abc = R.string.student_captured+resultObj.getString("TIME");
//                            String student_name = sharedPreference.getValue(getApplicationContext(), "STUDENT_NAME");
//                            student_info.setText(student_name + " := is here At This Time : " + resultObj.getString("TIME"));
//                        } else {
//                            student_info.setText(R.string.student_data_not_recieved);
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
////                Log.e("Response", "" + server_response);
//                student_info.setText(R.string.student_data_not_recieved);
//                Toast.makeText(getApplicationContext(), "No data Fetched", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    // Converting InputStream to String
//    private String readStream(InputStream in) {
//        BufferedReader reader = null;
//        StringBuffer response = new StringBuffer();
//        try {
//            reader = new BufferedReader(new InputStreamReader(in));
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return response.toString();
//    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
//            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
//            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.school_public))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.child))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), MapNavigationActivty.class));
        finish();
        System.exit(1);

    }


}