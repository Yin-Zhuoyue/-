package com.yzy.dao;

import com.yzy.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 任务信息处理
 */
@Mapper
public interface TaskDao {

    /**
     * 保存任务基本信息
     *
     * @param task 任务信息
     * @return 返回sql影响的行数，并将自增的task_id写入task中
     */
    @Options(useGeneratedKeys = true, keyProperty = "task_id")      // 插入结束后会返回自增主键
    @Insert("insert into task values(null, #{task_name}, #{task_type}, #{execution_time_period}, #{cron_expression}, " +
            "#{task_status}, #{task_add_date}, #{demand_department}, #{demander}, #{task_creator}, #{task_add_date}, '')")
    public int saveTaskBasicInfo(Task task);

    /**
     * 更新任务基本信息
     *
     * @param task 任务信息
     * @return 返回sql影响的行数
     */
    @Update("update task set task_name = #{task_name}, task_type = #{task_type}, task_status = 0, " +
            "demand_department = #{demand_department}, demander = #{demander}, last_edit_time = #{last_edit_time}" +
            "where task_id = #{task_id}")
    public int updateTaskBasicInfo(Task task);

    /**
     * 更新任务执行状态
     *
     * @param taskId     任务id
     * @param taskStatus 任务状态
     * @return 返回sql影响的行数
     */
    @Update("update task set task_status = #{taskStatus} where task_id = #{taskId}")
    public int updateTaskStatus(@Param("taskId") Integer taskId, @Param("taskStatus") Integer taskStatus);

    /**
     * 更新任务执行周期
     *
     * @param task 任务信息
     * @return 返回sql影响的行数
     */
    @Update("update task set execution_time_period = #{execution_time_period}, cron_expression = #{cron_expression}, " +
            "last_edit_time = #{last_edit_time}" +
            "where task_id = #{task_id}")
    public int updateTaskPeriod(Task task);

    /**
     * 更新任务最新一次的执行流程日志
     *
     * @param taskExecutionProcess 任务执行流程记录
     * @return 返回sql影响的行数
     */
    @Update("update task set last_task_execution_process = #{taskExecutionProcess} where task_id = #{taskId}")
    public int updateLastTaskExecutionProcess(String taskExecutionProcess, Integer taskId);

    /**
     * 删除任务
     *
     * @param taskId 任务id
     * @return 返回sql影响行数
     */
    @Delete("delete from task where task_id = #{taskId};" +
            "delete from mail_task where task_id = #{taskId};" +
            "delete from ftp_task where task_id = #{taskId};" +
            "delete from task_sql where task_id = #{taskId};" +
            "delete from task_result_file where task_id = #{taskId};")
    public int deleteTask(Integer taskId);

    /**
     * 根据任务名称分页查询用户管理下的任务基本信息
     *
     * @param taskName 任务名称
     * @param userName 用户名称
     * @param start    开始位置
     * @param rowNum   行数
     * @return 返回任务信息列表
     */
    @Select("<script> select task_id, task_name, task_type, execution_time_period, task_status, task_add_date, " +
            "demand_department, demander, task_creator from task where task_name like concat('%',#{taskName},'%') " +
            "<if test=\"userName != 'admin'\">and task_creator = #{userName}</if> " +
            "order by task_add_date desc limit #{start}, #{rowNum} </script>")
    public List<Task> getTaskBasicInfoByName(String taskName, String userName, Integer start, Integer rowNum);

    /**
     * 分页查询用户管理下的任务基本信息
     *
     * @param userName 用户名称
     * @param start    开始位置
     * @param rowNum   行数
     * @return 返回任务信息列表
     */
    @Select("<script> select task_id, task_name, task_type, execution_time_period, task_status from task where 1 = 1" +
            "<if test=\"userName != 'admin'\">and task_creator = #{userName}</if> " +
            "order by task_add_date desc limit #{start}, #{rowNum} </script>")
    public List<Task> getUserTaskBasicInfo(String userName, Integer start, Integer rowNum);

    /**
     * 根据任务id查询任务的基本信息
     *
     * @param taskId 任务id
     * @return 返回任务基本信息
     */
    @Select("select task_id, task_name, task_type, execution_time_period, task_status, task_add_date, " +
            "demand_department, demander, task_creator, last_edit_time, last_task_execution_process " +
            "from task where task_id = #{taskId}")
    public Task getTaskBasicInfoById(Integer taskId);

    /**
     * 查询已设置执行周期的任务
     *
     * @return 返回任务列表
     */
    @Select("select * from task where execution_time_period != '无'")
    public List<Task> getGeneralTasks();


}
