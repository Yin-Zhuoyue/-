package com.yzy.entity;

public class TaskSql {
    private Integer sql_id;
    private String sql_name = "";
    private String sql_text;
    private String database_name;
    private Integer file_id;
    private Integer task_id;

    @Override
    public String toString() {
        return "TaskSql{" +
                "sql_id=" + sql_id +
                ", sql_name='" + sql_name + '\'' +
                ", sql_text='" + sql_text + '\'' +
                ", database_name='" + database_name + '\'' +
                ", file_id=" + file_id +
                ", task_id=" + task_id +
                '}';
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public Integer getSql_id() {
        return sql_id;
    }

    public void setSql_id(Integer sql_id) {
        this.sql_id = sql_id;
    }

    public String getSql_name() {
        return sql_name;
    }

    public void setSql_name(String sql_name) {
        this.sql_name = sql_name;
    }

    public String getSql_text() {
        return sql_text;
    }

    public void setSql_text(String sql_text) {
        this.sql_text = sql_text;
    }

    public String getDatabase_name() {
        return database_name;
    }

    public void setDatabase_name(String database_name) {
        this.database_name = database_name;
    }

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }
}
