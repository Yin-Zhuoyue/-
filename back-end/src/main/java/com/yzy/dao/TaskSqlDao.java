package com.yzy.dao;

import com.yzy.entity.TaskSql;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 任务sql信息处理
 */
@Mapper
public interface TaskSqlDao {
    /**
     * 保存任务sql信息
     *
     * @param taskSql 任务sql信息
     * @return 返回sql影响的行数
     */
    @Insert("insert into task_sql values(null, #{sql_name}, #{sql_text}, #{database_name}, #{file_id}, #{task_id})")
    public int saveTaskSql(TaskSql taskSql);

    /**
     * 批量添加任务sql，目前用在任务的复制功能上，当一个任务被复制时，该任务下每个结果文件所包含的sql用批量的方式添加到数据库中
     *
     * @param taskSqls 任务sql信息列表
     * @return 返回sql影响的行数
     */
    @Insert("<script>insert into task_sql values " +
            "<foreach collection='taskSqls' item='taskSql' separator=','>" +
            "(null, #{taskSql.sql_name}, #{taskSql.sql_text}, #{taskSql.database_name}, " +
            "#{taskSql.file_id}, #{taskSql.task_id})" +
            "</foreach></script>")
    public int saveTaskSqls(@Param("taskSqls") List<TaskSql> taskSqls);

    /**
     * 更新任务sql信息
     *
     * @param taskSql 任务sql信息
     * @return 返回sql影响的行数
     */
    @Update("update task_sql set sql_name = #{sql_name}, sql_text = #{sql_text}, database_name = #{database_name}, " +
            "file_id = #{file_id} where sql_id = #{sql_id}")
    public int updateTaskSql(TaskSql taskSql);

    /**
     * 根据文件id查询此文件下的sql信息
     *
     * @param fileId 文件id
     * @return 返回sql信息列表
     */
    @Select("select * from task_sql where file_id = #{fileId}")
    public List<TaskSql> getFileSqls(Integer fileId);

    /**
     * 根据任务id查询此任务的所有sql
     *
     * @param taskId 任务id
     * @return 返回sql信息列表
     */
    @Select("select * from task_sql where task_id = #{taskId}")
    public List<TaskSql> getTaskSqls(Integer taskId);

    /**
     * 根据脚本id查sql信息
     *
     * @param sqlId 脚本id
     * @return 返回sql信息
     */
    @Select("select * from task_sql where sql_id = #{sqlId}")
    public TaskSql getSqlById(Integer sqlId);

    /**
     * 根据脚本id删除sql
     *
     * @param sqlId 脚本
     * @return 返回sql影响的行数
     */
    @Delete("delete from task_sql where sql_id = #{sqlId}")
    public int deleteTaskSql(Integer sqlId);
}
