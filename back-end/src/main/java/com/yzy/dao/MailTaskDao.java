package com.yzy.dao;

import com.yzy.entity.MailTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 邮件相关信息处理
 */
@Mapper
public interface MailTaskDao {

    /**
     * 保存邮件任务的邮件相关信息（发件人、抄送人、邮件主题等等）
     *
     * @param mailTask 任务的邮件信息
     * @return 返回sql影响的行数
     */
    @Insert("insert into mail_task values(#{task_id},#{recipient_mail_address},#{recipient_cc_mail_address}," +
            "#{mail_text},#{mail_subject})")
    public int saveMailTaskInfo(MailTask mailTask);

    /**
     * 更新邮件任务的邮件相关信息（发件人、抄送人、邮件主题等等）
     *
     * @param mailTask 任务的邮件信息
     * @return 返回sql影响的行数
     */
    @Update("update mail_task set recipient_mail_address = #{recipient_mail_address}, " +
            "recipient_cc_mail_address = #{recipient_cc_mail_address}, mail_text = #{mail_text} where task_id = #{task_id}")
    public int updateMailTaskInfo(MailTask mailTask);

    /**
     * 更新邮件任务的邮件相关信息（发件人、抄送人、邮件主题等等）
     *
     * @param taskId 任务的邮件信息
     * @return 返回任务的邮件相关信息
     */
    @Select("select * from mail_task where task_id = #{taskId}")
    public MailTask getMailInfoById(Integer taskId);
}
