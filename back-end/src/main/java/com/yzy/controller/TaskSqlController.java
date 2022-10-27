package com.yzy.controller;

import com.yzy.entity.TaskSql;
import com.yzy.service.TaskSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务sql信息处理的控制类
 */
@RestController
@RequestMapping("/taskSql")
public class TaskSqlController {
    @Autowired
    private TaskSqlService taskSqlService;

    /**
     * 响应新增sql信息的请求
     *
     * @param taskSql sql信息
     * @return 返回保存结果
     */
    @PostMapping
    public JsonResult saveTaskSql(@RequestBody TaskSql taskSql) {
        int flag = taskSqlService.saveTaskSql(taskSql);
        return new JsonResult(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, flag);
    }

    /**
     * 响应更新sql信息的请求
     *
     * @param taskSql sql信息
     * @return 返回更新结果
     */
    @PutMapping
    public JsonResult updateTaskSql(@RequestBody TaskSql taskSql) {
        int flag = taskSqlService.updateTaskSql(taskSql);
        return new JsonResult(flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR, flag);
    }

    /**
     * 响应删除sql信息的请求
     *
     * @param sqlId 脚本id
     * @return 返回删除结果
     */
    @DeleteMapping("/{sqlId}")
    public JsonResult deleteTaskSql(@PathVariable Integer sqlId) {
        int flag = taskSqlService.deleteTaskSql(sqlId);
        return new JsonResult(flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR, flag);
    }

    /**
     * 响应获取一个文件下的sql列表的请求
     *
     * @param fileId 文件id
     * @return 返回sql信息列表
     */
    @GetMapping("/sqls/{fileId}")
    public JsonResult getFileSqls(@PathVariable Integer fileId) {
        List<TaskSql> taskSqls = taskSqlService.getFileSqls(fileId);
        Integer code = taskSqls != null ? Code.GET_OK : Code.GET_ERR;
        String msg = taskSqls != null ? "" : "无数据";
        return new JsonResult(code, taskSqls, msg);
    }

    /**
     * 响应根据脚本id获取sql信息的请求
     *
     * @param sqlId 脚本id
     * @return 返回sql信息
     */
    @GetMapping("/{sqlId}")
    public JsonResult getTaskSql(@PathVariable Integer sqlId) {
        TaskSql taskSql = taskSqlService.getSqlById(sqlId);
        Integer code = taskSql != null ? Code.GET_OK : Code.GET_ERR;
        String msg = taskSql != null ? "" : "无数据";
        return new JsonResult(code, taskSql, msg);
    }
}
