package com.yzy.service.impl;

import com.yzy.dao.TaskResultFileDao;
import com.yzy.entity.TaskResultFile;
import com.yzy.entity.TaskSql;
import com.yzy.service.TaskResultFileService;
import com.yzy.service.TaskSqlService;
import com.yzy.util.CreateDataSuffixUtil;
import com.yzy.util.DataImportFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaskResultFileServiceImpl implements TaskResultFileService {

    @Autowired
    private TaskResultFileDao taskResultFileDao;

    @Autowired
    private TaskSqlService taskSqlService;

    private static Logger logger = LoggerFactory.getLogger(TaskExecute.class);

    @Override
    public int saveTaskResultFile(TaskResultFile taskResultFile) {
        return taskResultFileDao.saveTaskResultFile(taskResultFile);
    }

    @Override
    public int updateTaskResultFile(TaskResultFile taskResultFile) {
        return taskResultFileDao.updateTaskResultFile(taskResultFile);
    }

    @Override
    public int deleteTaskResultFile(Integer fileId) {
        return taskResultFileDao.deleteTaskResultFile(fileId);
    }

    @Override
    public List<TaskResultFile> getTaskResultFiles(Integer taskId) {
        return taskResultFileDao.getTaskResultFiles(taskId);
    }

    @Override
    public TaskResultFile getTaskResultFileInfoById(Integer fileId) {
        return taskResultFileDao.getTaskResultFileInfoById(fileId);
    }

    @Override
    public List<String> createResultFiles(Integer taskId) {
        List<TaskResultFile> taskResultFiles = taskResultFileDao.getTaskResultFiles(taskId);    // 获取任务需要生成的文件列表
        List<String> resultFilePaths = new ArrayList<>();
        for (TaskResultFile taskResultFile : taskResultFiles) {     // 遍历文件列表并生成列表中的所有文件
            logger.info(taskResultFile.getFile_name() + taskResultFile.getFile_type() + "文件开始生成");
            String dataSuffix = CreateDataSuffixUtil.createDataSuffix(taskResultFile); // 根据表中信息获取完整文件名
            List<TaskSql> taskSqls = taskSqlService.getFileSqls(taskResultFile.getFile_id());   // 获取当前的文件需要执行的脚本

            // 根据文件类型不同，用不同的数据写入方式
            if (taskResultFile.getFile_type().equals("Excel")) {

                // dataMap是一个用于存放sheet名称与sheet数据内容映射关系的哈希表（LinkedHashMap）
                LinkedHashMap<String, List<LinkedHashMap<String, Object>>> dataMap = new LinkedHashMap<>();
                AtomicInteger sheetCount = new AtomicInteger(1);
                for (TaskSql taskSql : taskSqls) {
                    String sheetName = !taskSql.getSql_name().equals("") ? taskSql.getSql_name() : "sheet" + sheetCount;
                    List<LinkedHashMap<String, Object>> resultData = getResultData(taskSql, taskResultFile);
                    sheetCount.getAndIncrement();
                    dataMap.put(sheetName, resultData);
                }
                String fileName = taskResultFile.getFile_name() + dataSuffix + ".xlsx";
                resultFilePaths.add(DataImportFileUtil.dataImportExcel(dataMap, fileName)); // 保存生成的excel文件的路径
                logger.info(taskResultFile.getFile_name() + taskResultFile.getFile_type() + "文件已生成");
            } else if (taskResultFile.getFile_type().equals("TXT")) {
                TaskSql taskSql = taskSqls.get(0);
                List<LinkedHashMap<String, Object>> resultData = getResultData(taskSql, taskResultFile);
                String separator = taskResultFile.getData_separator();
                String fileName = taskResultFile.getFile_name() + dataSuffix + ".txt";
                resultFilePaths.add(DataImportFileUtil.dataImportTxt(resultData, fileName, separator));
                logger.info(taskResultFile.getFile_name() + taskResultFile.getFile_type() + "文件已生成");
            }

        }
        return resultFilePaths;
    }

    /**
     * 获取脚本运行结果，如果脚本运行异常，如超时、数据库连接失败等等，则将异常原因写入结果并返回
     *
     * @param taskSql        任务sql信息
     * @param taskResultFile 任务结果文件信息
     * @return 返回结果列表
     */
    private List<LinkedHashMap<String, Object>> getResultData(TaskSql taskSql, TaskResultFile taskResultFile) {
        List<LinkedHashMap<String, Object>> resultData = new ArrayList<>(); // 初始化一个存脚本运行结果的对象
        logger.info(taskResultFile.getFile_name() + " " + taskSql.getSql_name() + "脚本开始运行");
        try {
            resultData = taskSqlService.getDataByTaskSql(taskSql);
        } catch (Exception e) {
            LinkedHashMap<String, Object> errorResultData = new LinkedHashMap<>();
            errorResultData.put("sqlExecutionException", e.toString());
            resultData.add(errorResultData);
            logger.error(taskResultFile.getFile_name() + " " + taskSql.getSql_name() + "脚本运行异常，异常信息如下：");
            logger.error(e.toString());
        }
        logger.info(taskResultFile.getFile_name() + " " + taskSql.getSql_name() + "脚本运行结束");
        return resultData;
    }
}
