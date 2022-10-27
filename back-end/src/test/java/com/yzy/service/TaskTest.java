package com.yzy.service;

import com.yzy.dao.*;
import com.yzy.entity.MailTask;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.*;

@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootTest
public class TaskTest {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private MailTaskDao mailTaskDao;

    @Autowired
    private TaskSqlExecuteDao taskSqlExecuteDao;

    @Autowired
    private TaskSqlDao taskSqlDao;

    @Autowired
    private SpecificNeedsTaskDao specificNeedsTaskDao;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TaskResultFileService taskResultFileService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TaskCheckService taskCheckService;

    @Autowired
    private TaskService taskService;

    @Value("${spring.mail.username}")
    private String from;

    private static Logger logger = LoggerFactory.getLogger(TaskTest.class);

    @Test
    public void copyTaskTest() {
        taskService.copyTask(16, "");
    }

    @Test
    public void sqlCountTest() {
        List<HashMap<String, Long>> result = specificNeedsTaskDao.taskSqlCount(16);
        System.out.println(result.get(0).get("sqlNum").getClass());
        System.out.println(result.get(0).get("sqlNum") == 1);
    }

    @Test
    public void sendMailTest() throws Exception {
        MailTask mailTask = mailTaskDao.getMailInfoById(118);
        List<String> filesPath = new ArrayList<>();
        filesPath.add("D:/自动任务的结果文件/装移机不履约统计2022.09.01-2022.09.30.xlsx");
        mailService.sendMail(mailTask, "run", filesPath);

    }

    @Test
    public void oracleConnectTest() {
        System.out.println();
    }

    private List<LinkedHashMap<String, Object>> getResultData(String databaseName, String sql) throws Exception {
        switch (databaseName) {
            case "综调":
                return taskSqlExecuteDao.getZdResultData(sql);
            case "综告":
                return taskSqlExecuteDao.getZgResultData(sql);
            case "统一网元库":
                return taskSqlExecuteDao.getTyResultData(sql);
            default:
                return null;
        }
    }
}
