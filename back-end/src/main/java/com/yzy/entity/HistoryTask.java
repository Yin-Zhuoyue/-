package com.yzy.entity;

import java.util.Date;

public class HistoryTask {
    private Integer task_id;
    private String task_name;
    private String task_type;
    private Date task_delete_date;
    private Integer run_number;

    @Override
    public String toString() {
        return "HistoryTask{" +
                "task_id=" + task_id +
                ", task_name='" + task_name + '\'' +
                ", task_type='" + task_type + '\'' +
                ", task_delete_date=" + task_delete_date +
                ", run_number=" + run_number +
                '}';
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public Date getTask_delete_date() {
        return task_delete_date;
    }

    public void setTask_delete_date(Date task_delete_date) {
        this.task_delete_date = task_delete_date;
    }

    public Integer getRun_number() {
        return run_number;
    }

    public void setRun_number(Integer run_number) {
        this.run_number = run_number;
    }
}
