package com.yzy.service;

import com.yzy.entity.TaskSql;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 处理任务sql相关业务
 */
public interface TaskSqlService {

    /**
     * 保存任务sql
     *
     * @param taskSql sql信息
     * @return 返回sql影响行数
     */
    @Transactional
    public int saveTaskSql(TaskSql taskSql);

    /**
     * 更新任务sql
     *
     * @param taskSql sql信息
     * @return 返回sql影响行数
     */
    @Transactional
    public int updateTaskSql(TaskSql taskSql);

    /**
     * 删除任务sql
     *
     * @param sqlId 脚本id
     * @return 返回sql影响行数
     */
    @Transactional
    public int deleteTaskSql(Integer sqlId);

    /**
     * 根据文件id获取此文件下的所有sql信息
     *
     * @param fileId 文件id
     * @return 返回sql信息列表
     */
    public List<TaskSql> getFileSqls(Integer fileId);

    /**
     * 根据脚本id获取sql信息
     *
     * @param sqlId 脚本id
     * @return 返回sql信息
     */
    public TaskSql getSqlById(Integer sqlId);

    /**
     * 根据数据库名称连接相对应的数据库并运行用户填入的脚本
     *
     * @param taskSql sql信息
     * @return 返回sql查询结果
     */
    public List<LinkedHashMap<String, Object>> getDataByTaskSql(TaskSql taskSql) throws Exception;

}
