package com.yzy.service.impl;

import com.yzy.dao.*;
import com.yzy.entity.Param;
import com.yzy.entity.Task;
import com.yzy.entity.TaskResultFile;
import com.yzy.entity.TaskSql;
import com.yzy.service.QuartzService;
import com.yzy.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 任务相关操作的具体实现
 */

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private MailTaskDao mailTaskDao;

    @Autowired
    private FtpTaskDao ftpTaskDao;

    @Autowired
    private TaskResultFileDao taskResultFileDao;

    @Autowired
    private TaskSqlDao taskSqlDao;

    @Autowired
    private QuartzService quartzService;


    @Override
    public int saveTask(Param param) {
        Task task = param.getTask();    // 获得数据包中的task类数据
        task.setTask_add_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));  // 记录当前时间
        taskDao.saveTaskBasicInfo(task);     // 将任务基本信息写入数据库，自增的主键task_id会返回到task中。
        quartzService.addTask(task.getTask_id(), task.getCron_expression());   // 将任务添加至调度器中
        if (task.getTask_type().equals("邮件")) {       // 如果是邮件类型任务，则将邮件相关信息写入数据库中对应的表。
            param.getMailTask().setTask_id(task.getTask_id());      // 将主键写入mailtask对象
            return mailTaskDao.saveMailTaskInfo(param.getMailTask());
        } else if (task.getTask_type().equals("FTP")) {
            param.getFtpTask().setTask_id(task.getTask_id());
            return ftpTaskDao.saveFtpTaskInfo(param.getFtpTask());
        } else {
            return -1;
        }
    }

    @Override
    public int copyTask(Integer taskId, String userName) {
        Param param = getById(taskId);
        param.getTask().setTask_creator(userName);      // 复制的任务的创建人不是原任务的创建人，而是当前用户
        param.getTask().setTask_name(param.getTask().getTask_name() + "(副本)");
        param.getTask().setTask_status(0);
        List<TaskResultFile> taskResultFiles = taskResultFileDao.getTaskResultFiles(taskId);
        List<TaskSql> taskSqls = taskSqlDao.getTaskSqls(taskId);
        saveTask(param);        // 将当前任务的副本存入数据库
        int newTaskId = param.getTask().getTask_id();   // 获取新任务的task_id

        // 遍历原任务的结果文件列表，将所有结果文件和脚本都复制一遍
        for (TaskResultFile taskResultFile : taskResultFiles) {
            taskResultFile.setTask_id(newTaskId);
            Integer originalFileId = taskResultFile.getFile_id();

            // 将更新了task_id的任务结果文件信息存入数据库，存入之后taskResultFile中的file_id会更新
            taskResultFileDao.saveTaskResultFile(taskResultFile);
            for (TaskSql taskSql : taskSqls) {
                taskSql.setTask_id(newTaskId);
                if (taskSql.getFile_id().equals(originalFileId)) {  // 根据原file_id找该文件下的脚本，并更新成新的file_id
                    taskSql.setFile_id(taskResultFile.getFile_id());
                }
            }
        }
        taskSqlDao.saveTaskSqls(taskSqls);// 将更新了task_id、file_id的任务脚本批量存入数据库
        return 1;
    }

    @Override
    public int updateTaskInfo(Param param) {
        Task task = param.getTask();
        task.setLast_edit_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));  // 记录当前时间
        taskDao.updateTaskBasicInfo(task);  // 更新任务基本信息
        param.getMailTask().setTask_id(param.getTask().getTask_id());   // 将主键传给mailTask
        param.getFtpTask().setTask_id(param.getTask().getTask_id());    // 将主键传给ftpTask
        if (task.getTask_type().equals("邮件")) {   // 如果任务类型为邮件，则更新mail表中的记录
            if (mailTaskDao.updateMailTaskInfo(param.getMailTask()) == 0) {   // 如果任务原本不是邮件任务，则将数据插入mail表
                return mailTaskDao.saveMailTaskInfo(param.getMailTask());
            } else return 1;
        } else if (task.getTask_type().equals("FTP")) {
            if (ftpTaskDao.updateFtpTaskInfo(param.getFtpTask()) == 0) {   // 如果任务原本不是FTP任务，则将数据插入FTP表
                return ftpTaskDao.saveFtpTaskInfo(param.getFtpTask());
            } else return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int updateTaskStatus(Integer taskId, Integer taskStatus) {
        String scheduleTaskName = taskId.toString();
        if (taskStatus == 1)
            quartzService.resumeTask(scheduleTaskName);
        else if (taskStatus == 0)
            quartzService.pauseTask(scheduleTaskName);
        return taskDao.updateTaskStatus(taskId, taskStatus);
    }

    @Override
    public int updateTaskPeriod(Task task) {
        task.setLast_edit_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));  // 记录当前时间
        quartzService.updateTaskPeriod(task.getTask_id().toString(), task.getCron_expression());   // 更新调度器中对应任务的cron
        return taskDao.updateTaskPeriod(task);
    }

    @Override
    public int deleteTask(Integer taskId) {
        quartzService.deleteTask(taskId.toString());
        return taskDao.deleteTask(taskId);
    }

    @Override
    public List<Task> getByName(String taskName, String userName, Integer pagination, Integer rowNum) {
        return taskDao.getTaskBasicInfoByName(taskName, userName, (pagination - 1) * rowNum, rowNum);
    }

    @Override
    public List<Task> getByUser(String userName, Integer pagination, Integer rowNum) {
        return taskDao.getUserTaskBasicInfo(userName, (pagination - 1) * rowNum, rowNum);
    }

    @Override
    public Param getById(Integer taskId) {
        Param param = new Param();
        Task task = taskDao.getTaskBasicInfoById(taskId);
        param.setTask(task);
        if (task.getTask_type().equals("邮件")) {
            param.setMailTask(mailTaskDao.getMailInfoById(taskId));
        } else if (task.getTask_type().equals("FTP")) {
            param.setFtpTask(ftpTaskDao.getFtpTaskInfoById(taskId));
        } else {
            return null;
        }
        return param;
    }

}
