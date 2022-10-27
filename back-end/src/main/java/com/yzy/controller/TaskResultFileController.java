package com.yzy.controller;


import com.yzy.entity.TaskResultFile;
import com.yzy.service.TaskResultFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务结果文件信息处理的控制类
 */
@RestController
@RequestMapping("/taskResultFile")
public class TaskResultFileController {
    @Autowired
    private TaskResultFileService taskResultFileService;

    /**
     * 响应新增结果文件信息的请求
     *
     * @param taskResultFile 任务结果文件信息
     * @return 返回添加结果
     */
    @PostMapping
    public JsonResult saveTaskResultFile(@RequestBody TaskResultFile taskResultFile) {
        int flag = taskResultFileService.saveTaskResultFile(taskResultFile);
        return new JsonResult(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, flag);
    }

    /**
     * 响应更新任务结果文件信息的请求
     *
     * @param taskResultFile 任务结果文件信息
     * @return 返回更新结果
     */
    @PutMapping
    public JsonResult updateTaskResultFile(@RequestBody TaskResultFile taskResultFile) {
        int flag = taskResultFileService.updateTaskResultFile(taskResultFile);
        return new JsonResult(flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR, flag);
    }

    /**
     * 响应删除结果文件信息的请求
     *
     * @param fileId 文件id
     * @return 返回删除结果
     */
    @DeleteMapping("/{fileId}")
    public JsonResult deleteTaskResultFile(@PathVariable Integer fileId) {
        int flag = taskResultFileService.deleteTaskResultFile(fileId);
        return new JsonResult(flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR, flag);
    }

    /**
     * 响应获取结果文件列表的请求
     *
     * @param taskId 任务id
     * @return 返回结果文件列表
     */
    @GetMapping("/files/{taskId}")
    public JsonResult getTaskResultFiles(@PathVariable Integer taskId) {
        List<TaskResultFile> taskResultFiles = taskResultFileService.getTaskResultFiles(taskId);
        Integer code = taskResultFiles != null ? Code.GET_OK : Code.GET_ERR;
        String msg = taskResultFiles != null ? "" : "无数据";
        return new JsonResult(code, taskResultFiles, msg);
    }

    /**
     * 响应根据id获取结果文件信息的请求
     *
     * @param fileId 文件id
     * @return 返回文件信息
     */
    @GetMapping("/{fileId}")
    public JsonResult getTaskResultFileInfo(@PathVariable Integer fileId) {
        TaskResultFile taskResultFile = taskResultFileService.getTaskResultFileInfoById(fileId);
        Integer code = taskResultFile != null ? Code.GET_OK : Code.GET_ERR;
        String msg = taskResultFile != null ? "" : "无数据";
        return new JsonResult(code, taskResultFile, msg);
    }
}
