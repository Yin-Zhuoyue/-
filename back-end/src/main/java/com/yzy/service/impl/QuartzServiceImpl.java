package com.yzy.service.impl;

import com.yzy.dao.TaskDao;
import com.yzy.entity.Task;
import com.yzy.service.QuartzService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定时器的具体实现
 */
@Service
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private TaskDao taskDao;


    @PostConstruct
    public void initAndStart() {
        try {
            List<Task> tasks = taskDao.getGeneralTasks();
            for (Task task : tasks) {
                addTask(task.getTask_id(), task.getCron_expression());
                if (task.getTask_status() == 1)
                    resumeTask(task.getTask_id().toString());
            }
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTask(Integer taskId, String cronExpression) {
        try {
            String scheduleTaskName = Integer.toString(taskId);     // 调度器中的任务名称即为数据库中的任务id

            // 创建任务主体
            JobDetail taskDetail = JobBuilder
                    .newJob(TaskExecute.class)                      // 目前所有任务都使用TaskExecute类作为任务的具体实现
                    .withIdentity(scheduleTaskName)
                    .usingJobData("taskId", taskId)       // 只传递任务id这一个参数
                    .build();

            // 创建任务触发器
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(scheduleTaskName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();

            // 将jobdetail和触发器添加至调度器
            scheduler.scheduleJob(taskDetail, cronTrigger);

            // 默认新添加的任务都处于关闭状态
            JobKey jobKey = JobKey.jobKey(scheduleTaskName);
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTaskPeriod(String scheduleTaskName, String cronExpression) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(scheduleTaskName);    // 获取任务对应的触发器

            // 重新构建任务的触发器
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            cronTrigger = cronTrigger
                    .getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();
            scheduler.rescheduleJob(triggerKey, cronTrigger);       // 重置调度器中的任务
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteTask(String scheduleTaskName) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleTaskName);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pauseTask(String scheduleTaskName) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleTaskName);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resumeTask(String scheduleTaskName) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleTaskName);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
