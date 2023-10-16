package com.project.demo.payload;

public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    private String success_unzip;
    private String flag;
    private String have_json;

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size, String success_unzip, String flag, String have_json) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.success_unzip = success_unzip;
        this.flag = flag;
        this.have_json = have_json;
    }
//    getter and setter
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSuccess_unzip() {
        return success_unzip;
    }

    public void setSuccess_unzip(String success_unzip) {
        this.success_unzip = success_unzip;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getHave_json() {
        return have_json;
    }

    public void setHave_json(String have_json) {
        this.have_json = have_json;
    }
}