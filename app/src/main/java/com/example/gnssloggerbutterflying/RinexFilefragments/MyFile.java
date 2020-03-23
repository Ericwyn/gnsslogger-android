package com.example.gnssloggerbutterflying.RinexFilefragments;


public class MyFile  {
    private int imageId;
    private String fileName;
    private String fileSize;

    private String fileDate;



    public MyFile(String fileName, String fileSize, String fileDate) {


        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
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
}


