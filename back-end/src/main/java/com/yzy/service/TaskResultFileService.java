package com.yzy.service;

import com.yzy.entity.TaskResultFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 任务结果文件信息处理
 */
public interface TaskResultFileService {

    /**
     * 保存任务结果文件信息
     *
     * @param taskResultFile 任务结果文件信息
     * @return 返回sql影响行数
     */
    @Transactional
    public int saveTaskResultFile(TaskResultFile taskResultFile);

    /**
     * 更新任务结果文件信息
     *
     * @param taskResultFile 任务结果文件信息
     * @return 返回sql影响行数
     */
    @Transactional
    public int updateTaskResultFile(TaskResultFile taskResultFile);

    /**
     * 删除任务结果文件信息
     *
     * @param fileId 文件id
     * @return 返回sql影响行数
     */
    @Transactional
    public int deleteTaskResultFile(Integer fileId);

    /**
     * 根据任务id获取任务结果文件信息列表
     *
     * @param taskId 任务id
     * @return 返回任务结果文件信息
     */
    public List<TaskResultFile> getTaskResultFiles(Integer taskId);

    /**
     * 根据文件id获取任务结果文件信息
     *
     * @param fileId 文件id
     * @return 返回任务结果文件信息
     */
    public TaskResultFile getTaskResultFileInfoById(Integer fileId);

    /**
     * 根据任务id获取此任务下的所有结果文件信息，并在服务器中生成所有结果文件，返回文件路径集合
     *
     * @param taskId 任务id
     * @return 返回文件路径集合
     */
    public List<String> createResultFiles(Integer taskId);
}
