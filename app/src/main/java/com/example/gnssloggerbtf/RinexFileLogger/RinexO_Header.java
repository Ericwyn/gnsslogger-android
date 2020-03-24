package com.example.gnssloggerbtf.RinexFileLogger;

/**
 * rinexo文件的头文件，但缺少了历元开始时间。
 */
public class RinexO_Header {

    private String FileName,MarkerName,MarkerNumber,ObserverName,ObsAgencyName,RecNumber,RecType,RecVersion,AntType,AntName;

    private String  Version;

    private String  approX,approY,approZ;

    private String  AntDeltaH,AntDeltaN,AntDeltaE;

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileName() {
        return FileName;
    }

    public void setMarkerName(String markerName) {
        MarkerName = markerName;
    }

    public String getMarkerName() {
        return MarkerName;
    }

    public void setMarkerNumber(String markerNumber) {
        MarkerNumber = markerNumber;
    }

    public String getMarkerNumber() {
        return MarkerNumber;
    }

    public void setObserverName(String obsName) {
        ObserverName = obsName;
    }

    public String getObserverName() {
        return ObserverName;
    }

    public void setObsAgencyName(String obsAgencyName) {
        ObsAgencyName = obsAgencyName;
    }

    public String getObsAgencyName() {
        return ObsAgencyName;
    }

    public void setRecNumber(String recNumber) {
        RecNumber = recNumber;
    }

    public String getRecNumber() {
        return RecNumber;
    }

    public void setRecType(String recType) {
        RecType = recType;
    }

    public String getRecType() {
        return RecType;
    }

    public void setRecVersion(String recVersion) {
        RecVersion = recVersion;
    }

    public String getRecVersion() {
        return RecVersion;
    }

    public void setAntDeltaE(String antDeltaE) {
        AntDeltaE = antDeltaE;
    }

    public String getAntDeltaE() {
        return AntDeltaE;
    }

    public void setAntDeltaH(String antDeltaH) {
        AntDeltaH = antDeltaH;
    }

    public String getAntDeltaH() {
        return AntDeltaH;
    }

    public void setAntDeltaN(String antDeltaN) {
        AntDeltaN = antDeltaN;
    }

    public String getAntDeltaN() {
        return AntDeltaN;
    }

    public void setAntName(String antName) {
        AntName = antName;
    }

    public String getAntName() {
        return AntName;
    }

    public void setAntType(String antType) {
        AntType = antType;
    }

    public String getAntType() {
        return AntType;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getVersion() {
        return Version;
    }

    public void setApproX(String approX) {
        this.approX = approX;
    }

    public String getApproX() {
        return approX;
    }

    public void setApproY(String approY) {
        this.approY = approY;
    }

    public String getApproY() {
        return approY;
    }

    public void setApproZ(String approZ) {
        this.approZ = approZ;
    }

    public String getApproZ() {
        return approZ;
    }


    public StringBuilder HeaderConvertRinexO3()
    {
        StringBuilder stringBuilder_header=new StringBuilder();
        stringBuilder_header.append ( "     3.03           OBSERVATION DATA    M (MIXED)           RINEX VERSION / TYPE")   ;
        stringBuilder_header.append('\n');

        //-60s就是不足60个字符  给右边补上空格
        stringBuilder_header.append(String.format("%-60sMARKER NAME", this.getMarkerName()));

        stringBuilder_header.append('\n');

        stringBuilder_header.append(String.format("%s                                                   MARKER NUMBER", this.getMarkerNumber()));

        stringBuilder_header.append('\n');

        //添加rec number20  type20  version20
        stringBuilder_header.append(String.format("%-20s%-20s%-20sREC # / TYPE / VERS", this.getMarkerName(),this.getRecType(),this.getRecVersion()));

        stringBuilder_header.append('\n');

        //添加ant name20 type40
        stringBuilder_header.append(String.format("%-20s%-40sANT # / TYPE",this.getAntName(),this.getAntType()));

        stringBuilder_header.append('\n');

        //添加APPROX POSITION XYZ,,, -2994427.7762  4951307.2376  2674497.9713                  APPROX POSITION XYZ

        stringBuilder_header.append(String.format("%14s%14s%14s%18sAPPROX POSITION XYZ", this.getApproX(),this.getApproY(),this.approZ," "));

        stringBuilder_header.append('\n');

        //添加        0.0000        0.0000        0.0000                  ANTENNA: DELTA H/E/N
        stringBuilder_header.append(String.format("%14s%14s%14s%18sANT # / TYPE",this.getAntDeltaH(),this.getAntDeltaE(),this.getAntDeltaN(),""));

        stringBuilder_header.append('\n');

        //添加观测值类型

        stringBuilder_header.append("G    8 C1C L1C D1C S1C C5Q L5Q D5Q S5Q                      SYS / # / OBS TYPES");

        stringBuilder_header.append('\n');

        stringBuilder_header.append("E    8 C1C L1C D1C S1C C5Q L5Q D5Q S5Q                      SYS / # / OBS TYPES");

        stringBuilder_header.append('\n');

        stringBuilder_header.append("R    4 C1C L1C D1C S1C                                      SYS / # / OBS TYPES");

        stringBuilder_header.append('\n');

        stringBuilder_header.append("C    4 C2I L2I D2I S2I                                      SYS / # / OBS TYPES");

        stringBuilder_header.append('\n');

        stringBuilder_header.append("J    8 C1C L1C D1C S1C C5Q L5Q D5Q S5Q                      SYS / # / OBS TYPES");

        stringBuilder_header.append('\n');

        //添加采样间隔    30.0000                                                 INTERVAL

        stringBuilder_header.append("     1.0000                                                 INTERVAL");
        stringBuilder_header.append('\n');
//
//        //添加                                                            END OF HEADER
//
//        stringBuilder_header.append("                                                            END OF HEADER");

        return stringBuilder_header;

    }
    public StringBuilder HeaderConvertRinexO2()
    {
        StringBuilder stringBuilder_header=new StringBuilder();
        stringBuilder_header.append(  "     2.11           OBSERVATION DATA    M (MIXED)           RINEX VERSION / TYPE");
        stringBuilder_header.append('\n');

        //-60s就是不足60个字符  给右边补上空格
        stringBuilder_header.append(String.format("%-60sMARKER NAME", this.getMarkerName()));

        stringBuilder_header.append('\n');

        stringBuilder_header.append(String.format("%s                                                   MARKER NUMBER", this.getMarkerNumber()));

        stringBuilder_header.append('\n');

        //添加rec number20  type20  version20
        stringBuilder_header.append(String.format("%-20s%-20s%-20sREC # / TYPE / VERS", this.getMarkerName(),this.getRecType(),this.getRecVersion()));

        stringBuilder_header.append('\n');

        //添加ant name20 type40
        stringBuilder_header.append(String.format("%-20s%-40sANT # / TYPE",this.getAntName(),this.getAntType()));

        stringBuilder_header.append('\n');

        //添加APPROX POSITION XYZ,,, -2994427.7762  4951307.2376  2674497.9713                  APPROX POSITION XYZ

        stringBuilder_header.append(String.format("%14s%14s%14s%18sAPPROX POSITION XYZ", this.getApproX(),this.getApproY(),this.approZ," "));

        stringBuilder_header.append('\n');

        //添加        0.0000        0.0000        0.0000                  ANTENNA: DELTA H/E/N
        stringBuilder_header.append(String.format("%14s%14s%14s%18sANT # / TYPE",this.getAntDeltaH(),this.getAntDeltaE(),this.getAntDeltaN(),""));

        stringBuilder_header.append('\n');
        //    0表示接收机是单频仪器，对于android 手机来说，应该设置为0
        stringBuilder_header.append("     1     0                                                WAVELENGTH FACT L1/2");

        stringBuilder_header.append('\n');

        //添加观测值类型

        stringBuilder_header.append("     8    C1    L1    D1    S1    C5    L5    D5    S5       # / TYPES OF OBSERV");

        stringBuilder_header.append('\n');


        //添加采样间隔    1.0000                                                 INTERVAL

        stringBuilder_header.append("     1.0000                                                 INTERVAL");
        stringBuilder_header.append('\n');
//
//        //添加                                                            END OF HEADER
//
//        stringBuilder_header.append("                                                            END OF HEADER");

        return stringBuilder_header;

    }
}
