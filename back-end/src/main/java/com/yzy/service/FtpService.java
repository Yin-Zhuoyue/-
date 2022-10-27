package com.yzy.service;

import com.yzy.entity.FtpTask;

import java.util.List;

/**
 * 处理ftp任务
 */
public interface FtpService {
    /**
     * 将文件全部上传至目标ftp服务器
     *
     * @param ftpTask   ftp信息
     * @param filePaths 文件路径
     */
    public void uploadFilesToFtp(FtpTask ftpTask, List<String> filePaths) throws Exception;
}
