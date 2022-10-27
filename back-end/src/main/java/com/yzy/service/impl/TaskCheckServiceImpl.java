package com.yzy.service.impl;

import com.yzy.aop.ThreadTimeLimit;
import com.yzy.dao.SpecificNeedsTaskDao;
import com.yzy.dao.TaskSqlExecuteDao;
import com.yzy.entity.FtpTask;
import com.yzy.entity.TaskSql;
import com.yzy.service.TaskCheckService;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 任务检查、测试功能的具体实现
 */
@Service
public class TaskCheckServiceImpl implements TaskCheckService {

    @Autowired
    private SpecificNeedsTaskDao specificNeedsTaskDao;

    @Autowired
    private TaskSqlExecuteDao taskSqlExecuteDao;

    @Override
    public int checkTaskIntegrity(Integer taskId) {
        if (specificNeedsTaskDao.getTaskPeriod(taskId).equals("无"))     // 任务执行周期未设置
            return 1;
        else if (specificNeedsTaskDao.taskFilesCount(taskId) == 0)      // 任务结果文件未添加
            return 2;
        else {
            List<HashMap<String, Long>> sqlCountResults = specificNeedsTaskDao.taskSqlCount(16);
            for (HashMap<String, Long> sqlCountResult : sqlCountResults) {   // 判断每个结果文件对应的脚本数是否为0
                if (sqlCountResult.get("sqlNum") == 0)      // 存在某个结果文件的任务sql未添加
                    return 3;
            }
            return 10;
        }
    }

    @Override
    public int checkSqlCount(Integer fileId) {
        return specificNeedsTaskDao.fileSqlCount(fileId);
    }

    @Override
    public String ifFtpAccessible(FtpTask ftpTask) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            ftpClient.connect(ftpTask.getFtp_host(), ftpTask.getFtp_port());
            if (ftpClient.login(ftpTask.getFtp_user(), ftpTask.getFtp_pwd()))
                return "success";
            else
                return "用户名或密码不正确，登录失败";
        } catch (Exception e) {
            return "" + e.getMessage();
        }
    }

    @Override
    @ThreadTimeLimit(1)
    public String ifSqlExecutable(TaskSql taskSql) {
        try {
            switch (taskSql.getDatabase_name()) {
                case "综调":
                    taskSqlExecuteDao.getZdResultData(taskSql.getSql_text());
                    return null;
                case "综告":
                    taskSqlExecuteDao.getZgResultData(taskSql.getSql_text());
                    return null;
                case "统一网元库":
                    taskSqlExecuteDao.getTyResultData(taskSql.getSql_text());
                    return null;
                default:
                    return "无匹配数据库！";
            }
        }
        catch (Exception e) {
            return "" + e.getCause();
        }
    }
}
