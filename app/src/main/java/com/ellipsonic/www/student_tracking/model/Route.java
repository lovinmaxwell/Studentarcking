package com.ellipsonic.www.student_tracking.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import com.ellipsonic.www.student_tracking.Modules.Distance;
import com.ellipsonic.www.student_tracking.Modules.Duration;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
