package com.yzy.controller;

import com.yzy.entity.FtpTask;
import com.yzy.entity.TaskSql;
import com.yzy.service.TaskCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 任务信息验证控制类
 */
@RestController
@RequestMapping("/taskCheck")
public class TaskCheckController {

    @Autowired
    private TaskCheckService taskCheckService;

    /**
     * 响应验证任务信息的完整性的请求
     *
     * @param taskId 任务id
     * @return 返回验证结果
     */
    @GetMapping("/taskIntegrity/{taskId}")
    public JsonResult checkTaskIntegrity(@PathVariable Integer taskId) {
        Integer taskIntegrityFlag = taskCheckService.checkTaskIntegrity(taskId);
        return new JsonResult(Code.GET_OK, taskIntegrityFlag, "");
    }

    /**
     * 响应检查任务下的结果文件对应脚本数量的请求
     *
     * @param fileId 文件id
     * @return 返回脚本数
     */
    @GetMapping("/{fileId}")
    public JsonResult checkSqlCount(@PathVariable Integer fileId) {
        return new JsonResult(Code.GET_OK, taskCheckService.checkSqlCount(fileId), "");
    }

    /**
     * 响应判断脚本可执行性的请求
     *
     * @param taskSql sql信息
     * @return 返回执行结果，如果脚本有错会返回报错信息
     */
    @PostMapping("/sqlCheck")
    public JsonResult ifSqlExecutable(@RequestBody TaskSql taskSql) {
        return new JsonResult(Code.GET_OK, taskCheckService.ifSqlExecutable(taskSql), "");
    }

    /**
     * 响应测试ftp是否可连接的请求
     *
     * @param ftpTask ftp信息
     * @return 返回测试结果，如果不能连接会返回连接失败原因
     */
    @PostMapping("/ftpCheck")
    public JsonResult ifFtpAccessible(@RequestBody FtpTask ftpTask) {
        return new JsonResult(Code.GET_OK, taskCheckService.ifFtpAccessible(ftpTask), "");
    }
}
