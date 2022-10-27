package com.yzy.service.impl;

import com.yzy.dao.FtpTaskDao;
import com.yzy.dao.MailTaskDao;
import com.yzy.dao.TaskDao;
import com.yzy.entity.FtpTask;
import com.yzy.entity.MailTask;
import com.yzy.entity.Task;
import com.yzy.service.FtpService;
import com.yzy.service.MailService;
import com.yzy.service.TaskResultFileService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 任务执行类
 */
public class TaskExecute extends QuartzJobBean {
    @Autowired
    private TaskDao taskDao;

    @Autowired
    private MailTaskDao mailTaskDao;

    @Autowired
    private FtpTaskDao ftpTaskDao;

    @Autowired
    private TaskResultFileService taskResultFileService;

    @Autowired
    private MailService mailService;

    @Autowired
    private FtpService ftpService;

    private String taskExecutionProcess;        // 任务执行流程记录

    private static Logger logger = LoggerFactory.getLogger(TaskExecute.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        int taskId = context.getJobDetail().getJobDataMap().getInt("taskId");
        Task task = taskDao.getTaskBasicInfoById(taskId);   // 获取任务信息
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        try {
            taskExecutionProcess = currentTime + " " + taskId + task.getTask_name() + "任务开始执行" + "\n";
            logger.info(taskId + task.getTask_name() + "任务开始执行");
            List<String> resultFilesPath = taskResultFileService.createResultFiles(taskId); // 获取任务结果文件
            logger.info(taskId + task.getTask_name() + "任务的结果文件生成完毕");
            taskExecutionProcess += currentTime + " " + taskId + task.getTask_name() + "任务的结果文件生成完毕" + "\n";
            if (task.getTask_type().equals("邮件")) {
                MailTask mailTask = mailTaskDao.getMailInfoById(taskId);
                mailService.sendMail(mailTask, task.getTask_name(), resultFilesPath);
                logger.info(taskId + task.getTask_name() + "任务执行成功，邮件已发送");
                taskExecutionProcess += currentTime + " " + taskId + task.getTask_name() + "任务执行成功，邮件已发送" + "\n";
            } else if (task.getTask_type().equals("FTP")) {
                FtpTask ftpTask = ftpTaskDao.getFtpTaskInfoById(taskId);
                ftpService.uploadFilesToFtp(ftpTask, resultFilesPath);
                logger.info(taskId + task.getTask_name() + "任务执行成功，文件已上传ftp");
                taskExecutionProcess += currentTime + " " + taskId + task.getTask_name() + "任务执行成功，文件已上传ftp" + "\n";
            }
        } catch (Exception e) {
            logger.error(taskId + task.getTask_name() + "任务执行失败，异常信息如下：");
            logger.error(e.toString());
            taskExecutionProcess += currentTime + " " + taskId + task.getTask_name() + "任务执行失败，原因：" + "\n";
            taskExecutionProcess += e.toString();
        } finally {
            taskDao.updateLastTaskExecutionProcess(taskExecutionProcess, taskId);
        }
    }
}
