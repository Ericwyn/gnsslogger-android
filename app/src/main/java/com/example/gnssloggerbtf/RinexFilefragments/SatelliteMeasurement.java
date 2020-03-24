package com.example.gnssloggerbtf.RinexFilefragments;

/**
 * 这个类打算就是来存放历元数据的，但是呢，时间那一块我还没想好怎么写，所以时间方法先搁置
 */

public class SatelliteMeasurement {


    /*
    UTC时间
     */
    private UTCtime utctime;

    public UTCtime getUtCtime() {
        return utctime;
    }

    public void setUtctime(UTCtime utctime) {
        this.utctime = utctime;
    }


    /*
            伪距
             */
    private double pseudorange;

    public double getPseudorange() {
        return pseudorange;
    }

    public void setPseudorange(double pseudorange) {
        this.pseudorange = pseudorange;
    }

    /*
    卫星的id号
     */
    private int satId;

    public void setSatId(int satId) {
        this.satId = satId;
    }

    public int getSatId() {
        return satId;
    }
    /*
    构造方法,待定，看是否可以去掉
     */
//    public SatelliteMeasurement(int satId,double pseudorange)
//    {
//        this.satId=satId;
//        this.pseudorange=pseudorange;
//    }


    /*
    信号强度
     */
    private double signalStrength;

    public void setSignalStrength(double  signalStrength) {
        this.signalStrength = signalStrength;
    }

    public double  getSignalStrength() {
        return signalStrength;
    }
    /*
    星座类型
     */
    private int constellationType;

    public void setConstellationType(int constellationType) {
        this.constellationType = constellationType;
    }

    public String getConstellationType() {
        if(constellationType==1)
            return "GPS";
        if(constellationType==2)
            return "SBAS";
        if(constellationType==3)
            return "GLONASS";
        if(constellationType==4)
            return "QZSS";
        if(constellationType==5)
            return "BDS";
        if(constellationType==6)
            return "GALILEO";
        else
            return "UNKNOWN";
    }
    /*
    频率
     */
    private double carrierFrequency;

    public void setCarrierFrequency(double carrierFrequency) {
        this.carrierFrequency = carrierFrequency;
    }

    public double getCarrierFrequency() {
        return carrierFrequency;
    }
    /*
    时间，UTC，待定
     */




    /*
    载波相位观测值
     */
    private double phase;

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }
    /*
    SNR,信噪比
     */
    private double snr;

    public double getSnr() {
        return snr;
    }

    public void setSnr(double snr) {
        this.snr = snr;
    }
    /*
    多普勒值
     */
    private double doppler;

    public void setDoppler(double doppler) {
        this.doppler = doppler;
    }

    public double getDoppler() {
        return doppler;
    }

    /*
    卫星的唯一ID，可区别的ID
     */
    private String satUniqueId;

    public void setSatUniqueId(String satUniqueId) {
        this.satUniqueId = satUniqueId;
    }

    public String getSatUniqueId() {
        return satUniqueId;
    }

}
