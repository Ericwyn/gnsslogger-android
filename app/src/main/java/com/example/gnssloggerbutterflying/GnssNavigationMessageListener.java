package com.example.gnssloggerbutterflying;

import android.location.GnssNavigationMessage;

/**  A class representing an interface for logging GPS information. */
public interface GnssNavigationMessageListener {
    /**
     * @see GnssNavigationMessage.Callback#
     * onGnssNavigationMessageReceived(GnssNavigationMessage)  #
     */
    void onGnssNavigationMessageReceived(GnssNavigationMessage event);
    /** @see GnssNavigationMessage.Callback#onStatusChanged(int) */
    void onGnssNavigationMessageStatusChanged(int status);
}
