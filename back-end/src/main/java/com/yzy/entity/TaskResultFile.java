package com.yzy.entity;

public class TaskResultFile{
    private Integer file_id;
    private String file_name;
    private String file_type;
    private String data_separator;
    private String date_suffix_type;
    private String start_date;
    private Integer start_date_offset;
    private String end_date;
    private Integer end_date_offset;
    private Integer task_id;

    @Override
    public String toString() {
        return "TaskResultFile{" +
                "file_id=" + file_id +
                ", file_name='" + file_name + '\'' +
                ", file_type='" + file_type + '\'' +
                ", data_separator='" + data_separator + '\'' +
                ", date_suffix_type='" + date_suffix_type + '\'' +
                ", start_date='" + start_date + '\'' +
                ", start_date_offset=" + start_date_offset +
                ", end_date='" + end_date + '\'' +
                ", end_date_offset=" + end_date_offset +
                ", task_id=" + task_id +
                '}';
    }

    public String getDate_suffix_type() {
        return date_suffix_type;
    }

    public void setDate_suffix_type(String date_suffix_type) {
        this.date_suffix_type = date_suffix_type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public Integer getStart_date_offset() {
        return start_date_offset;
    }

    public void setStart_date_offset(Integer start_date_offset) {
        this.start_date_offset = start_date_offset;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Integer getEnd_date_offset() {
        return end_date_offset;
    }

    public void setEnd_date_offset(Integer end_date_offset) {
        this.end_date_offset = end_date_offset;
    }

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getData_separator() {
        return data_separator;
    }

    public void setData_separator(String data_separator) {
        this.data_separator = data_separator;
    }
}
