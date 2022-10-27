package com.yzy.entity;

public class FtpTask{

    private Integer task_id;
    private String ftp_host;
    private Integer ftp_port;
    private String ftp_user;
    private String ftp_pwd;
    private String file_save_catalog = "";

    @Override
    public String toString() {
        return "FtpTask{" +
                "task_id=" + task_id +
                ", ftp_host='" + ftp_host + '\'' +
                ", ftp_port=" + ftp_port +
                ", ftp_user='" + ftp_user + '\'' +
                ", ftp_pwd='" + ftp_pwd + '\'' +
                ", file_save_catalog='" + file_save_catalog + '\'' +
                '}';
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getFile_save_catalog() {
        return file_save_catalog;
    }

    public void setFile_save_catalog(String file_save_catalog) {
        this.file_save_catalog = file_save_catalog;
    }

    public String getFtp_host() {
        return ftp_host;
    }

    public void setFtp_host(String ftp_host) {
        this.ftp_host = ftp_host;
    }

    public Integer getFtp_port() {
        return ftp_port;
    }

    public void setFtp_port(Integer ftp_port) {
        this.ftp_port = ftp_port;
    }

    public String getFtp_user() {
        return ftp_user;
    }

    public void setFtp_user(String ftp_user) {
        this.ftp_user = ftp_user;
    }

    public String getFtp_pwd() {
        return ftp_pwd;
    }

    public void setFtp_pwd(String ftp_pwd) {
        this.ftp_pwd = ftp_pwd;
    }
}
