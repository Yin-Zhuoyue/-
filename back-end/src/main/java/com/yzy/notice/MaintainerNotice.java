package com.yzy.notice;

import com.yzy.dao.SpecificNeedsTaskDao;
import com.yzy.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class MaintainerNotice {

    @Autowired
    private SpecificNeedsTaskDao specificNeedsTaskDao;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private static Logger logger = LoggerFactory.getLogger(MaintainerNotice.class);

    /**
     * 每天9点半发送当天的自动任务执行情况
     */
    @Scheduled(cron = "00 30 09 * * ?")
    public void sendTaskExecutionConditionNotice() {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Map<String, Long> executionCondition = specificNeedsTaskDao.taskExecutionCount(todayDate);
        List<Task> failTasks = specificNeedsTaskDao.executionFailTasks(todayDate);
        Long executionCount = executionCondition.get("executionCount");
        Long failExecutionCount = executionCondition.get("failExecutionCount");
        long successExecutionCount = executionCount - failExecutionCount;
        StringBuilder failTaskCondition = new StringBuilder();
        for (Task failTask : failTasks) {
            failTaskCondition.append(failTask.getTask_id())
                    .append(failTask.getTask_name())
                    .append("执行日志如下：\n")
                    .append(failTask.getLast_task_execution_process())
                    .append("\n\n");
        }
        String subject = todayDate + "自动任务执行情况：" +
                "应执行" + executionCount + "，成功" + successExecutionCount;

        String[] to = {"15335144622@189.cn", "18066056260@189.cn"};// 维护人员邮箱
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setText(String.valueOf(failTaskCondition));
        } catch (Exception e) {
            logger.error(e.toString());
        }
        mailSender.send(message);
    }
}
