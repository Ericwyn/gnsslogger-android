package com.example.gnssloggerbtf.RinexFilefragments;


public class MyFile  {
    private int imageId;
    private String fileName;
    private String fileSize;

    private String fileDate;
    private String fileVersion;
    private String fileType;



    public MyFile(String fileName, String fileSize, String fileDate,String fileVersion,String fileType) {


        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
        this.fileVersion=fileVersion;
        this.fileType=fileType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }
    public String getFileDate() {
        return fileDate;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getFileVersion() {
        return fileVersion;
    }
}


