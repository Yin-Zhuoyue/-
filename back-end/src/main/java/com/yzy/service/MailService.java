package com.yzy.service;

import com.yzy.entity.MailTask;

import java.util.List;

/**
 * 处理发送邮件任务
 */
public interface MailService {
    /**
     * 发邮件
     *
     * @param mailTask  邮件信息
     * @param taskName  任务名称
     * @param filePaths 附件所在路径集合
     */
    public void sendMail(MailTask mailTask, String taskName, List<String> filePaths) throws Exception;
}
