package com.yzy.entity;

// task表中保存一个任务的基本属性，名称，类型，执行周期等等
public class Task {
    private Integer task_id;
    private String task_name;
    private String task_type;
    private String execution_time_period = "无";
    private String cron_expression = "* * * * * ?";
    private Integer task_status = 0;
    private String demand_department = null;
    private String demander = null;
    private String task_add_date = null;
    private String task_creator = null;
    private String last_edit_time = null;
    private String last_task_execution_process = "";

    @Override
    public String toString() {
        return "Task{" +
                "task_id=" + task_id +
                ", task_name='" + task_name + '\'' +
                ", task_type='" + task_type + '\'' +
                ", execution_time_period='" + execution_time_period + '\'' +
                ", cron_expression='" + cron_expression + '\'' +
                ", task_status=" + task_status +
                ", demand_department='" + demand_department + '\'' +
                ", demander='" + demander + '\'' +
                ", task_add_date='" + task_add_date + '\'' +
                ", task_creator='" + task_creator + '\'' +
                ", last_edit_time='" + last_edit_time + '\'' +
                ", last_task_execution_process='" + last_task_execution_process + '\'' +
                '}';
    }

    public String getLast_task_execution_process() {
        return last_task_execution_process;
    }

    public void setLast_task_execution_process(String last_task_execution_process) {
        this.last_task_execution_process = last_task_execution_process;
    }

    public String getCron_expression() {
        return cron_expression;
    }

    public void setCron_expression(String cron_expression) {
        this.cron_expression = cron_expression;
    }

    public Integer getTask_status() {
        return task_status;
    }

    public void setTask_status(Integer task_status) {
        this.task_status = task_status;
    }

    public String getTask_creator() {
        return task_creator;
    }

    public void setTask_creator(String task_creator) {
        this.task_creator = task_creator;
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

    public String getExecution_time_period() {
        return execution_time_period;
    }

    public void setExecution_time_period(String execution_time_period) {
        this.execution_time_period = execution_time_period;
    }

    public String getDemand_department() {
        return demand_department;
    }

    public void setDemand_department(String demand_department) {
        this.demand_department = demand_department;
    }

    public String getDemander() {
        return demander;
    }

    public void setDemander(String demander) {
        this.demander = demander;
    }

    public String getTask_add_date() {
        return task_add_date;
    }

    public void setTask_add_date(String task_add_date) {
        this.task_add_date = task_add_date;
    }

    public String getLast_edit_time() {
        return last_edit_time;
    }

    public void setLast_edit_time(String last_edit_time) {
        this.last_edit_time = last_edit_time;
    }
}
