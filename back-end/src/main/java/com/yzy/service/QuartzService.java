package com.yzy.service;

/**
 * 任务定时业务
 */
public interface QuartzService {
    /**
     * 在服务启动时初始化数据库中已有任务
     */
    public void initAndStart();

    /**
     * 向调度器中添加定时任务
     *
     * @param taskId         既作为调度器中的任务名称，又是传入任务具体实现方法的参数
     * @param cronExpression 任务的执行周期表达式
     */
    public void addTask(Integer taskId, String cronExpression);

    /**
     * 更新调度器中任务的执行周期
     *
     * @param scheduleTaskName 统一使用task_id作为调度器中的任务名称
     * @param cronExpression   cron表达式
     */
    public void updateTaskPeriod(String scheduleTaskName, String cronExpression);

    /**
     * 在调度器中删除任务
     *
     * @param scheduleTaskName 统一使用task_id作为调度器中的任务名称
     */
    public void deleteTask(String scheduleTaskName);

    /**
     * 根据任务名称暂停任务
     *
     * @param scheduleTaskName 统一使用task_id作为调度器中的任务名称
     */
    public void pauseTask(String scheduleTaskName);

    /**
     * 根据任务名称重新启动任务
     *
     * @param scheduleTaskName 统一使用task_id作为调度器中的任务名称
     */
    public void resumeTask(String scheduleTaskName);

}
