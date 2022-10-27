package com.yzy.entity;

import java.util.List;

//包装类，用于对多个实体类对象进行打包。
public class Param {
    private Task task;
    private MailTask mailTask;
    private FtpTask ftpTask;
    private List<TaskResultFile> taskResultFiles;
    private List<TaskSql> taskSqls;
    private String taskName;
    private Integer pagination;
    private Integer rowNum;
    private String userName;


    @Override
    public String toString() {
        return "Param{" +
                "task=" + task +
                ", mailTask=" + mailTask +
                ", ftpTask=" + ftpTask +
                ", taskResultFiles=" + taskResultFiles +
                ", taskSqls=" + taskSqls +
                ", pagination=" + pagination +
                ", rows=" + rowNum +
                ", user_name='" + userName + '\'' +
                ", task_name='" + taskName + '\'' +
                '}';
    }

    public Integer getPagination() {
        return pagination;
    }

    public void setPagination(Integer pagination) {
        this.pagination = pagination;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<TaskSql> getTaskSqls() {
        return taskSqls;
    }

    public void setTaskSqls(List<TaskSql> taskSqls) {
        this.taskSqls = taskSqls;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public MailTask getMailTask() {
        return mailTask;
    }

    public void setMailTask(MailTask mailTask) {
        this.mailTask = mailTask;
    }

    public FtpTask getFtpTask() {
        return ftpTask;
    }

    public void setFtpTask(FtpTask ftpTask) {
        this.ftpTask = ftpTask;
    }

    public List<TaskResultFile> getTaskResultFiles() {
        return taskResultFiles;
    }

    public void setTaskResultFiles(List<TaskResultFile> taskResultFiles) {
        this.taskResultFiles = taskResultFiles;
    }
}
