package com.yzy.service;

import com.yzy.entity.Param;
import com.yzy.entity.Task;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 任务信息的增删改查、任务的启停处理
 */
@Transactional
public interface TaskService {

    /**
     * 添加新任务并放入调度器
     *
     * @param param 任务信息（包含基本信息和类型信息）
     * @return 返回sql影响行数
     */
    public int saveTask(Param param);

    /**
     * 复制已有任务并放入调度器
     *
     * @param taskId   任务id
     * @param userName 用户名
     * @return 返回sql影响行数
     */
    public int copyTask(Integer taskId, String userName);

    /**
     * 更新任务信息
     *
     * @param param 任务信息（包含基本信息、类型信息、结果文件信息）
     * @return 返回sql影响行数
     */
    public int updateTaskInfo(Param param);

    /**
     * 更新任务状态，开启或关闭
     *
     * @param taskId     任务id
     * @param taskStatus 任务状态
     * @return 返回sql影响行数
     */
    public int updateTaskStatus(Integer taskId, Integer taskStatus);

    /**
     * 更新任务的执行周期
     *
     * @param task 任务基本信息
     * @return 返回sql影响行数
     */
    public int updateTaskPeriod(Task task);

    /**
     * 删除任务
     *
     * @param taskId 任务id
     * @return 返回sql影响行数
     */
    public int deleteTask(Integer taskId);

    /**
     * 根据任务名称分页查询用户管理下的任务信息
     *
     * @param taskName   任务名称
     * @param userName   用户名
     * @param pagination 页码
     * @param rowNum     行数
     * @return 返回任务信息列表
     */
    public List<Task> getByName(String taskName, String userName, Integer pagination, Integer rowNum);

    /**
     * 分页查询用户管理下的任务信息
     *
     * @param userName   用户名
     * @param pagination 页码
     * @param rowNum     行数
     * @return 返回任务信息列表
     */
    public List<Task> getByUser(String userName, Integer pagination, Integer rowNum);

    /**
     * 根据任务id查询任务信息
     *
     * @param taskId 任务id
     * @return 返回任务信息（包含基本信息、类型信息、结果文件信息）
     */
    public Param getById(Integer taskId);

}
