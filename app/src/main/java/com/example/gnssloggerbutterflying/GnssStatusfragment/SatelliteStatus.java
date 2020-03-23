package com.example.gnssloggerbutterflying.GnssStatusfragment;

public class SatelliteStatus {

    /*
    卫星号
     */
    private int svid;

    public int getSvid() {
        return svid;
    }

    public void setSvid(int svid) {
        this.svid = svid;
    }

    /*
    星座类型
     */
    private ConstellationType mConstellationType;

    public ConstellationType getConstellationType() {
        return mConstellationType;
    }

    public void setConstellationType(ConstellationType constellationType) {
        mConstellationType = constellationType;
    }
    /*
    载波噪声
     */
    private float cn0DbHz;

    public float getCn0DbHz() {
        return cn0DbHz;
    }

    public void setCn0DbHz(float cn0DbHz) {
        this.cn0DbHz = cn0DbHz;
    }
    /**
     * Reports whether the satellite at the specified index has ephemeris data.
     *
     * @param satIndex the index of the satellite in the list.
     */
    private boolean hasEphemeris;

    public boolean isHasEphemeris() {
        return hasEphemeris;
    }

    public void setHasEphemeris(boolean hasEphemeris) {
        this.hasEphemeris = hasEphemeris;
    }

    /**
     * Reports whether the satellite at the specified index has almanac data.
     *
     * @param satIndex the index of the satellite in the list.
     */

    private boolean hasAlmanac;

    public boolean isHasAlmanac() {
        return hasAlmanac;
    }

    public void setHasAlmanac(boolean hasAlmanac) {
        this.hasAlmanac = hasAlmanac;
    }

    /**
     * Reports whether the satellite at the specified index was used in the calculation of the most
     * recent position fix.
     *
     * @param satIndex the index of the satellite in the list.
     */
   private boolean usedInFix;

    public boolean isUsedInFix() {
        return usedInFix;
    }

    public void setUsedInFix(boolean usedInFix) {
        this.usedInFix = usedInFix;
    }

    /*
        高度角
         */
    private float elevationDegrees;

    public void setElevationDegrees(float elevationDegrees) {
        this.elevationDegrees = elevationDegrees;
    }

    public float getElevationDegrees() {
        return elevationDegrees;
    }
    /*
    方位角
     */
    private float azimuthDegrees;

    public void setAzimuthDegrees(float azimuthDegrees) {
        this.azimuthDegrees = azimuthDegrees;
    }

    public float getAzimuthDegrees() {
        return azimuthDegrees;
    }
    /*
    是否携带频率
     */
    private boolean hasCarrierFrequency;

    public boolean isHasCarrierFrequency() {
        return hasCarrierFrequency;
    }

    public void setHasCarrierFrequency(boolean hasCarrierFrequency) {
        this.hasCarrierFrequency = hasCarrierFrequency;
    }

    /*
        携带的频率是啥
        */
    private float carrierFrequencyHz;

    public float getCarrierFrequencyHz() {
        return carrierFrequencyHz;
    }

    public void setCarrierFrequencyHz(float carrierFrequencyHz) {
        this.carrierFrequencyHz = carrierFrequencyHz;
    }
}
