package com.yzy.dao;

import com.yzy.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 为特定需求提供任务信息查询
 */
@Mapper
public interface SpecificNeedsTaskDao {

    /**
     * 查询一个任务中各个结果文件下的sql数量
     *
     * @param taskId 任务id
     * @return 返回文件id与sql数量的对应列表
     */
    @Select("select f.file_id fileId,count(case when s.file_id is not null then 1 end) sqlNum from task_result_file f " +
            "left join task_sql s on s.file_id = f.file_id " +
            "where f.task_id = #{taskId} group by f.file_id")
    public List<HashMap<String, Long>> taskSqlCount(Integer taskId);

    /**
     * 查询一个结果文件下的sql数量
     *
     * @param fileId 文件id
     * @return 返回int
     */
    @Select("select count(*) from task_sql where file_id = #{fileId}")
    public int fileSqlCount(Integer fileId);

    /**
     * 查询一个任务的执行周期（是文字描述，不是cron表达式）
     *
     * @param taskId 任务id
     * @return 返回string
     */
    @Select("select execution_time_period from task where task_id = #{taskId}")
    public String getTaskPeriod(Integer taskId);

    /**
     * 查询一个任务下的结果文件数
     *
     * @param taskId 任务id
     * @return 返回int
     */
    @Select("select count(*) from task_result_file where task_id = #{taskId}")
    public int taskFilesCount(Integer taskId);

    /**
     * 查询当天执行的、执行失败的任务数
     *
     * @param date 日期
     * @return 返回任务的执行数和执行失败数
     */
    @Select("select count(*) executionCount," +
            "count(case when last_task_execution_process like '%执行失败%' then 1 end) failExecutionCount " +
            "from task where last_task_execution_process like concat(#{date},'%')")
    public Map<String, Long> taskExecutionCount(String date);

    /**
     * 查询当天执行失败的任务
     *
     * @param date 日期
     * @return 返回任务信息列表
     */
    @Select("select task_id, task_name, last_task_execution_process " +
            "from task " +
            "where last_task_execution_process like concat(#{date},'%') " +
            "and last_task_execution_process like '%执行失败%'")
    public List<Task> executionFailTasks(String date);
}
