package com.yzy.service;

import com.yzy.entity.FtpTask;
import com.yzy.entity.TaskSql;

/**
 * 对任务必要属性的校验以及给用户提供测试功能。
 */
public interface TaskCheckService {

    /**
     * 检查任务的必要项是否已完整，如果不完整则任务不能属于开启状态
     *
     * @param taskId 任务id
     * @return 返回结果码。
     */
    public int checkTaskIntegrity(Integer taskId);

    /**
     * 查询任务结果文件下的sql数量
     *
     * @param fileId 文件id
     * @return 返回数量
     */
    public int checkSqlCount(Integer fileId);

    /**
     * 测试ftp任务中配置的FTP是否能访问
     *
     * @param ftpTask ftp信息
     * @return 返回测试结果，成功则返回success，失败则返回失败原因
     */
    public String ifFtpAccessible(FtpTask ftpTask);

    /**
     * 测试sql脚本是否能编译以及脚本中的表是否有权限访问。只测试，不返回sql运行结果，sql运行超1秒回强行终止
     *
     * @param taskSql sql信息
     * @return 返回测试结果，成功或超时都返回null，失败则返回失败原因。
     */
    public String ifSqlExecutable(TaskSql taskSql);
}
