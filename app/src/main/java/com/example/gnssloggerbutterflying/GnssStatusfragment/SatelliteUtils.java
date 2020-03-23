/*
 * Copyright (C) 2013 Sean J. Barbeau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gnssloggerbutterflying.GnssStatusfragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.GnssStatus;
import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

import static com.example.gnssloggerbutterflying.GnssStatusfragment.ConstellationType.BEIDOU;
import static com.example.gnssloggerbutterflying.GnssStatusfragment.ConstellationType.GALILEO;
import static com.example.gnssloggerbutterflying.GnssStatusfragment.ConstellationType.GLONASS;
import static com.example.gnssloggerbutterflying.GnssStatusfragment.ConstellationType.GPS;
import static com.example.gnssloggerbutterflying.GnssStatusfragment.ConstellationType.IRNSS;
import static com.example.gnssloggerbutterflying.GnssStatusfragment.ConstellationType.QZSS;
import static com.example.gnssloggerbutterflying.GnssStatusfragment.ConstellationType.SBAS;
import static com.example.gnssloggerbutterflying.GnssStatusfragment.ConstellationType.UNKNOWN;

/**
 * Utilities to manage GNSS signal and satellite information
 */
public class SatelliteUtils {

    private static final String TAG = "SatelliteUtils";

    private static final int CONSTELLATION_IRNSS_TEMP = 7;

    /**
     * Returns the Global Navigation Satellite System (GNSS) for a satellite given the PRN.  For
     * Android 6.0.1 (API Level 23) and lower.  Android 7.0 and higher should use getGnssConstellationType()
     *
     * @param prn PRN value provided by the GpsSatellite.getPrn() method
     * @return GnssType for the given PRN
     */
    @Deprecated
    public static ConstellationType getGnssType(int prn) {
        if (prn >= 1 && prn <= 32) {
            return GPS;
        } else if (prn == 33) {
            return SBAS;
        } else if (prn == 39) {
            // See Issue #205
            return SBAS;
        } else if (prn >= 40 && prn <= 41) {
            // See Issue #92
            return SBAS;
        } else if (prn == 46) {
            return SBAS;
        } else if (prn == 48) {
            return SBAS;
        } else if (prn == 49) {
            return SBAS;
        } else if (prn == 51) {
            return SBAS;
        } else if (prn >= 65 && prn <= 96) {
            // See Issue #26 for details
            return GLONASS;
        } else if (prn >= 193 && prn <= 200) {
            // See Issue #54 for details
            return QZSS;
        } else if (prn >= 201 && prn <= 235) {
            // See Issue #54 for details
            return BEIDOU;
        } else if (prn >= 301 && prn <= 336) {
            // See https://github.com/barbeau/gpstest/issues/58#issuecomment-252235124 for details
            return GALILEO;
        } else {
            return UNKNOWN;
        }
    }

    /**
     * Returns the Global Navigation Satellite System (GNSS) for a satellite given the GnssStatus
     * constellation type.  For Android 7.0 and higher.  This is basically a translation to our
     * own GnssType enumeration that we use for Android 6.0.1 and lower.  Note that
     * getSbasConstellationType() should be used to get the particular SBAS constellation type
     *
     * @param gnssConstellationType constellation type provided by the GnssStatus.getConstellationType()
     *                              method
     * @return GnssType for the given GnssStatus constellation type
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ConstellationType getGnssConstellationType(int gnssConstellationType) {
        switch (gnssConstellationType) {
            case GnssStatus.CONSTELLATION_GPS:
                return GPS;
            case GnssStatus.CONSTELLATION_GLONASS:
                return GLONASS;
            case GnssStatus.CONSTELLATION_BEIDOU:
                return BEIDOU;
            case GnssStatus.CONSTELLATION_QZSS:
                return QZSS;
            case GnssStatus.CONSTELLATION_GALILEO:
                return GALILEO;
            case CONSTELLATION_IRNSS_TEMP:
                // FIX ME - We can't use the GnssStatus.CONSTELLATION_IRNSS Android SDK constant in
                // this switch statement until this Android bug is fixed - https://issuetracker.google.com/issues/134611316
                // For now, we define CONSTELLATION_IRNSS_TEMP to be the same value of 7 so we can
                // still support IRNSS.
                return IRNSS;
            case GnssStatus.CONSTELLATION_SBAS:
                return SBAS;
            case GnssStatus.CONSTELLATION_UNKNOWN:
                return UNKNOWN;
            default:
                return UNKNOWN;
        }
    }

    /**
     * Returns true if this device supports the Sensor.TYPE_ROTATION_VECTOR sensor, false if it
     * doesn't
     *
     * @return true if this device supports the Sensor.TYPE_ROTATION_VECTOR sensor, false if it
     * doesn't
     */
    public static boolean isRotationVectorSensorSupported(Context context) {
        SensorManager sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD &&
                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null;
    }

    /**
     * Returns true if the device supports the Gnss status listener, false if it does not
     *
     * @return true if the device supports the Gnss status listener, false if it does not
     */
    public static boolean isGnssStatusListenerSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * Returns true if the platform supports providing carrier frequencies for each satellite, false if it does not
     *
     * @return true if the platform supports providing carrier frequencies for each satellite, false if it does not
     */
    public static boolean isGnssCarrierFrequenciesSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * Returns true if the platform supports providing vertical accuracy values and this location
     * has vertical accuracy information, false if it does not
     *
     * @return true if the platform supports providing vertical accuracy values and this location
     *  has vertical accuracy information, false if it does not
     */
    public static boolean isVerticalAccuracySupported(Location location) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && location.hasVerticalAccuracy();
    }

    /**
     * Returns true if the platform supports providing speed and bearing accuracy values, false if it does not
     *
     * @return true if the platform supports providing speed and bearing accuracy values, false if it does not
     */
    public static boolean isSpeedAndBearingAccuracySupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
