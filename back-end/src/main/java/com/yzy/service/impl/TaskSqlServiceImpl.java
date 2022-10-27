package com.yzy.service.impl;

import com.yzy.dao.TaskSqlDao;
import com.yzy.dao.TaskSqlExecuteDao;
import com.yzy.entity.TaskSql;
import com.yzy.service.TaskSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TaskSqlServiceImpl implements TaskSqlService {

    @Autowired
    private TaskSqlDao taskSqlDao;

    @Autowired
    private TaskSqlExecuteDao taskSqlExecuteDao;

    @Override
    public int saveTaskSql(TaskSql taskSql) {
        return taskSqlDao.saveTaskSql(taskSql);
    }

    @Override
    public int updateTaskSql(TaskSql taskSql) {
        return taskSqlDao.updateTaskSql(taskSql);
    }

    @Override
    public int deleteTaskSql(Integer sqlId) {
        return taskSqlDao.deleteTaskSql(sqlId);
    }

    @Override
    public List<TaskSql> getFileSqls(Integer fileId) {
        return taskSqlDao.getFileSqls(fileId);
    }

    @Override
    public TaskSql getSqlById(Integer sqlId) {
        return taskSqlDao.getSqlById(sqlId);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getDataByTaskSql(TaskSql taskSql) throws Exception{
        switch (taskSql.getDatabase_name()) {
            case "综调":
                return taskSqlExecuteDao.getZdResultData(taskSql.getSql_text());
            case "综告":
                return taskSqlExecuteDao.getZgResultData(taskSql.getSql_text());
            case "统一网元库":
                return taskSqlExecuteDao.getTyResultData(taskSql.getSql_text());
            default:
                return null;
        }
    }
}
