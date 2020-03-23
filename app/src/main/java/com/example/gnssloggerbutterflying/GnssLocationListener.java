package com.example.gnssloggerbutterflying;

import android.location.GnssMeasurementsEvent;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;

/**
 * Interface used by GpsTestActivity to communicate with Gps*Fragments
 */
public interface GnssLocationListener extends LocationListener {

    //void gpsStart();

    //void gpsStop();


    void onGnssFirstFix(int ttffMillis);

    void onSatelliteStatusChanged(GnssStatus status);

    void onGnssStarted();

    void onGnssStopped();

    void onGnssMeasurementsReceived(GnssMeasurementsEvent event);

    void onLocationChanged(Location location);

//    void onOrientationChanged(double orientation, double tilt);

    void onNmeaMessage(String message, long timestamp);
}