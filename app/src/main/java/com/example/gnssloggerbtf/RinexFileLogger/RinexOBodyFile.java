package com.example.gnssloggerbtf.RinexFileLogger;

import android.annotation.SuppressLint;

import com.example.gnssloggerbtf.RinexFilefragments.Constellations.Satellites.BdsSatellite;
import com.example.gnssloggerbtf.RinexFilefragments.Constellations.Satellites.EpochMeasurement;
import com.example.gnssloggerbtf.RinexFilefragments.Constellations.Satellites.GalileoSatellite;
import com.example.gnssloggerbtf.RinexFilefragments.Constellations.Satellites.GlonassSatellite;
import com.example.gnssloggerbtf.RinexFilefragments.Constellations.Satellites.GpsSatellite;
import com.example.gnssloggerbtf.RinexFilefragments.Constellations.Satellites.QzssSatellite;

import java.util.List;

/**
 * 这个是将处理得到的历元数据，转换为rinex3.03格式
 * 2020/3/16
 * butterflying10
 */
public class RinexOBodyFile {

    private List<EpochMeasurement> mEpochMeasurementList;
    private StringBuilder rinex3bodyString;
    private StringBuilder rinex2bodyString;

    public StringBuilder getRinex3bodyString() {

        rinex3bodyString = EpochMeasurementListTo3BodyString();
        return rinex3bodyString;
    }

    public StringBuilder getRinex2bodyString() {
        rinex2bodyString = EpochMeasurementListTo2BodyString();
        return rinex2bodyString;
    }


    public RinexOBodyFile(List<EpochMeasurement> epochMeasurementList) {
        this.mEpochMeasurementList = epochMeasurementList;
    }

    @SuppressLint("DefaultLocale")
    private StringBuilder EpochMeasurementListTo3BodyString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (EpochMeasurement epochMeasurement : mEpochMeasurementList) {
            /*
            每一个历元的开始
             */
            stringBuilder.append(String.format("> %d %2d %2d %2d %2d%11.7f  0%3d", epochMeasurement.getEpochTime().getYear(), epochMeasurement.getEpochTime().getMonth(), epochMeasurement.getEpochTime().getDay(), epochMeasurement.getEpochTime().getHour(), epochMeasurement.getEpochTime().getMinute(), epochMeasurement.getEpochTime().getSecond(), epochMeasurement.getSatelliteNum()));

            stringBuilder.append('\n');

            /*
            开始一个历元的卫星数据
             */
            for (GpsSatellite gpsSatellite : epochMeasurement.getGpsSatelliteList()) {


                if (gpsSatellite.isHasC1() && gpsSatellite.isHasC5()) {
                    stringBuilder.append(String.format("%s%14.3f  %14.3f  %14.3f  %14.3f  %14.3f  %14.3f  %14.3f  %14.3f", gpsSatellite.getPrn(), gpsSatellite.getC1(), gpsSatellite.getL1(), gpsSatellite.getD1(), gpsSatellite.getS1(), gpsSatellite.getC5(), gpsSatellite.getL5(), gpsSatellite.getD5(), gpsSatellite.getS5()));

                    stringBuilder.append('\n');
                }
                if (gpsSatellite.isHasC1() && !gpsSatellite.isHasC5()) {
                    stringBuilder.append(String.format("%s%14.3f  %14.3f  %14.3f  %14.3f", gpsSatellite.getPrn(), gpsSatellite.getC1(), gpsSatellite.getL1(), gpsSatellite.getD1(), gpsSatellite.getS1()));

                    stringBuilder.append('\n');
                }
                if (!gpsSatellite.isHasC1() && gpsSatellite.isHasC5()) {
                    stringBuilder.append(String.format("%s%14s  %14s  %14s  %14s  %14.3f  %14.3f  %14.3f  %14.3f", gpsSatellite.getPrn(), " ", " ", " ", " ", gpsSatellite.getC5(), gpsSatellite.getL5(), gpsSatellite.getD5(), gpsSatellite.getS5()));

                    stringBuilder.append('\n');
                }


            }
            for (QzssSatellite qzssSatellite : epochMeasurement.getQzssSatelliteList()) {
                if (qzssSatellite.isHasC1() && qzssSatellite.isHasC5()) {
                    stringBuilder.append(String.format("%s%14.3f  %14.3f  %14.3f  %14.3f  %14.3f  %14.3f  %14.3f  %14.3f", qzssSatellite.getPrn(), qzssSatellite.getC1(), qzssSatellite.getL1(), qzssSatellite.getD1(), qzssSatellite.getS1(), qzssSatellite.getC5(), qzssSatellite.getL5(), qzssSatellite.getD5(), qzssSatellite.getS5()));

                    stringBuilder.append('\n');
                }
                if (qzssSatellite.isHasC1() && !qzssSatellite.isHasC5()) {
                    stringBuilder.append(String.format("%s%14.3f  %14.3f  %14.3f  %14.3f", qzssSatellite.getPrn(), qzssSatellite.getC1(), qzssSatellite.getL1(), qzssSatellite.getD1(), qzssSatellite.getS1()));

                    stringBuilder.append('\n');
                }
                if (!qzssSatellite.isHasC1() && qzssSatellite.isHasC5()) {
                    stringBuilder.append(String.format("%s%14s  %14s  %14s  %14s  %14.3f  %14.3f  %14.3f  %14.3f", qzssSatellite.getPrn(), " ", " ", " ", " ", qzssSatellite.getC5(), qzssSatellite.getL5(), qzssSatellite.getD5(), qzssSatellite.getS5()));

                    stringBuilder.append('\n');
                }
            }

            for (BdsSatellite bdsSatellite : epochMeasurement.getBdsSatelliteList()) {

                stringBuilder.append(String.format("%s%14.3f  %14.3f  %14.3f  %14.3f", bdsSatellite.getPrn(), bdsSatellite.getC2I(), bdsSatellite.getL2I(), bdsSatellite.getD2I(), bdsSatellite.getS2I()));
                stringBuilder.append('\n');
            }

            for (GalileoSatellite galileoSatellite : epochMeasurement.getGalileoSatelliteList()) {
                if (galileoSatellite.isHasC1() && galileoSatellite.isHasC5()) {
                    stringBuilder.append(String.format("%s%14.3f  %14.3f  %14.3f  %14.3f  %14.3f  %14.3f  %14.3f  %14.3f", galileoSatellite.getPrn(), galileoSatellite.getC1(), galileoSatellite.getL1(), galileoSatellite.getD1(), galileoSatellite.getS1(), galileoSatellite.getC5(), galileoSatellite.getL5(), galileoSatellite.getD5(), galileoSatellite.getS5()));

                    stringBuilder.append('\n');
                }
                if (galileoSatellite.isHasC1() && !galileoSatellite.isHasC5()) {
                    stringBuilder.append(String.format("%s%14.3f  %14.3f  %14.3f  %14.3f", galileoSatellite.getPrn(), galileoSatellite.getC1(), galileoSatellite.getL1(), galileoSatellite.getD1(), galileoSatellite.getS1()));

                    stringBuilder.append('\n');
                }
                if (!galileoSatellite.isHasC1() && galileoSatellite.isHasC5()) {
                    stringBuilder.append(String.format("%s%14s  %14s  %14s  %14s  %14.3f  %14.3f  %14.3f  %14.3f", galileoSatellite.getPrn(), " ", " ", " ", " ", galileoSatellite.getC5(), galileoSatellite.getL5(), galileoSatellite.getD5(), galileoSatellite.getS5()));

                    stringBuilder.append('\n');
                }
            }
            for (GlonassSatellite glonassSatellite : epochMeasurement.getGlonassSatelliteList()) {
                stringBuilder.append(String.format("%s%14.3f  %14.3f  %14.3f  %14.3f", glonassSatellite.getPrn(), glonassSatellite.getC1C(), glonassSatellite.getL1C(), glonassSatellite.getD1C(), glonassSatellite.getS1C()));
                stringBuilder.append('\n');
            }


        }

        return stringBuilder;

    }

    @SuppressLint("DefaultLocale")
    private StringBuilder EpochMeasurementListTo2BodyString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (EpochMeasurement epochMeasurement : mEpochMeasurementList) {
            /*
            每一个历元的开始,, 19 11 26  0  0  0.0000000  0    这个共29个字符  之后是卫星prn  12颗卫星   36个字符
             */
            stringBuilder.append(String.format(" %2d %2d %2d %2d %2d%11.7f  0%3d", epochMeasurement.getEpochTime().getYearSimplify(), epochMeasurement.getEpochTime().getMonth(), epochMeasurement.getEpochTime().getDay(), epochMeasurement.getEpochTime().getHour(), epochMeasurement.getEpochTime().getMinute(), epochMeasurement.getEpochTime().getSecond(), epochMeasurement.getSatelliteNum2()));

            /**
             * 我觉得应该不会有接收卫星个数超过36的情况，不然我就GG了，算了  加个try   catch吧
             */
            if (epochMeasurement.getSatelliteNum2() <= 12) {
                for (int i = 0; i < epochMeasurement.getSatelliteNum2(); i++) {
                    stringBuilder.append(epochMeasurement.getPrnlist().get(i));
                }
                stringBuilder.append('\n');
            }

            if (epochMeasurement.getSatelliteNum2() > 12 && epochMeasurement.getSatelliteNum2() <= 24) {
                for (int i = 0; i < 12; i++) {
                    stringBuilder.append(epochMeasurement.getPrnlist().get(i));
                }
                stringBuilder.append('\n');
                stringBuilder.append("                                ");
                for (int i = 12; i < epochMeasurement.getSatelliteNum2(); i++) {
                    stringBuilder.append(epochMeasurement.getPrnlist().get(i));
                }
                stringBuilder.append('\n');
            }
            if (epochMeasurement.getSatelliteNum2() > 24 && epochMeasurement.getSatelliteNum2() <= 36) {
                for (int i = 0; i < 12; i++) {
                    stringBuilder.append(epochMeasurement.getPrnlist().get(i));
                }
                stringBuilder.append('\n');
                stringBuilder.append("                                ");
                for (int i = 12; i < 24; i++) {

                    stringBuilder.append(epochMeasurement.getPrnlist().get(i));
                }
                stringBuilder.append('\n');
                stringBuilder.append("                                ");
                for (int i = 24; i < epochMeasurement.getSatelliteNum2(); i++) {

                    stringBuilder.append(epochMeasurement.getPrnlist().get(i));
                }
                stringBuilder.append('\n');
            }

            /*
            开始一个历元的卫星数据
             */
            for (GpsSatellite gpsSatellite : epochMeasurement.getGpsSatelliteList()) {
                //有L1频率
                if (gpsSatellite.isHasC1()) {

                    stringBuilder.append(String.format("%14.3f  %14.3f  %14.3f  %14.3f", gpsSatellite.getC1(), gpsSatellite.getL1(), gpsSatellite.getD1(), gpsSatellite.getS1()));
                }
                //有L5频率
                if (gpsSatellite.isHasC5()) {
                    stringBuilder.append(String.format("  %14.3f", gpsSatellite.getC5()));
                    stringBuilder.append('\n');
                    stringBuilder.append(String.format("%14.3f  %14.3f  %14.3f", gpsSatellite.getL5(), gpsSatellite.getD5(), gpsSatellite.getS5()));
                    stringBuilder.append('\n');
                }
                else
                {
                    stringBuilder.append('\n');
                    stringBuilder.append('\n');
                }
            }
            for (GalileoSatellite galileoSatellite : epochMeasurement.getGalileoSatelliteList()) {

                //有L1频率
                if (galileoSatellite.isHasC1()) {

                    stringBuilder.append(String.format("%14.3f  %14.3f  %14.3f  %14.3f", galileoSatellite.getC1(), galileoSatellite.getL1(), galileoSatellite.getD1(), galileoSatellite.getS1()));
                }
                //有L5频率
                if (galileoSatellite.isHasC5()) {
                    stringBuilder.append(String.format("  %14.3f", galileoSatellite.getC5()));
                    stringBuilder.append('\n');
                    stringBuilder.append(String.format("%14.3f  %14.3f  %14.3f", galileoSatellite.getL5(), galileoSatellite.getD5(), galileoSatellite.getS5()));
                    stringBuilder.append('\n');
                }
                else
                {
                    stringBuilder.append('\n');
                    stringBuilder.append('\n');
                }

            }
            for (GlonassSatellite glonassSatellite : epochMeasurement.getGlonassSatelliteList()) {
                stringBuilder.append(String.format("%14.3f  %14.3f  %14.3f  %14.3f", glonassSatellite.getC1C(), glonassSatellite.getL1C(), glonassSatellite.getD1C(), glonassSatellite.getS1C()));
                stringBuilder.append('\n');
            }


        }

        return stringBuilder;

    }


    public List<EpochMeasurement> getEpochMeasurementList() {
        return mEpochMeasurementList;
    }

    public void setEpochMeasurementList(List<EpochMeasurement> epochMeasurementList) {
        mEpochMeasurementList = epochMeasurementList;
    }
}
