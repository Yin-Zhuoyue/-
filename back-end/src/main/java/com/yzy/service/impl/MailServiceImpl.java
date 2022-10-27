package com.yzy.service.impl;

import com.yzy.entity.MailTask;
import com.yzy.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMail(MailTask mailTask, String taskName, List<String> filePaths) throws Exception {
        // 获取邮件需要的参数：收件人、抄送人、主题、正文
        String[] to = mailTask.getRecipient_mail_address().split(";\\n");
        String[] cc = mailTask.getRecipient_cc_mail_address().split(";\\n");
        String text = mailTask.getMail_text();
        String mailSubject = mailTask.getMail_subject();

        // 如果表中邮件主题为空则用任务名作为邮件主题，后跟发送日日期和自动发送标识
        mailSubject = (!mailSubject.equals("") ? mailSubject : taskName)
                + new SimpleDateFormat("yyyy.MM.dd").format(new Date()) + "（自动发送）";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        if (!cc[0].equals("")) // 如果有抄送人就设置抄送人，没有就不设置
            helper.setCc(cc);
        helper.setSubject(mailSubject);
        helper.setText(text);
        for (String filePath : filePaths) {
            FileSystemResource file = new FileSystemResource(filePath);
            String fileName = file.getFilename();
            helper.addAttachment(fileName, file);   // 添加附件
        }
        mailSender.send(message);
    }
}
