package com.example.gnssloggerbtf;

import android.location.GnssMeasurementsEvent;
import android.location.LocationListener;

public interface GnssMeasurementListener extends LocationListener {


    void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs);


}
