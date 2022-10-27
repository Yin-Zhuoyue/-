package com.yzy.controller;

import com.yzy.entity.Param;
import com.yzy.entity.Task;
import com.yzy.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务信息处理的控制类
 */
@RestController
@RequestMapping("/taskInfo")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 响应新增任务的请求
     *
     * @param param 任务信息（基本信息 + 类型信息）
     * @return 返回保存结果
     */
    @PostMapping
    public JsonResult saveTask(@RequestBody Param param) {
        int flag = taskService.saveTask(param);
        return new JsonResult(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, flag);
    }

    /**
     * 响应复制已有任务的请求
     *
     * @param json 请求参数
     * @return 返回保存结果
     */
    @PostMapping("/copyTask")
    public JsonResult copyTask(@RequestBody Map<String, Object> json) {
        int flag = taskService.copyTask((Integer) json.get("taskId"), (String) json.get("user"));
        return new JsonResult(flag > 0 ? Code.SAVE_OK : Code.SAVE_ERR, flag);
    }

    /**
     * 响应更新任务信息的请求
     *
     * @param param 任务信息（基本信息 + 类型信息）
     * @return 返回更新结果
     */
    @PutMapping
    public JsonResult updateTaskInfo(@RequestBody Param param) {
        int flag = taskService.updateTaskInfo(param);
        return new JsonResult(flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR, flag);
    }

    /**
     * 响应更新任务状态的请求
     *
     * @param task 任务基本信息
     * @return 返回更新结果
     */
    @PutMapping("/taskStatus")
    public JsonResult updateTaskStatus(@RequestBody Task task) {
        int flag = taskService.updateTaskStatus(task.getTask_id(), task.getTask_status());
        return new JsonResult(flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR, flag);
    }

    /**
     * 响应更新任务执行周期的请求
     *
     * @param task 任务基本信息
     * @return 返回更新结果
     */
    @PutMapping("/taskPeriod")
    public JsonResult updateTaskPeriod(@RequestBody Task task) {
        int flag = taskService.updateTaskPeriod(task);
        return new JsonResult(flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR, flag);
    }

    /**
     * 响应删除任务的请求
     *
     * @param taskId 任务id
     * @return 返回删除结果
     */
    @DeleteMapping("/{taskId}")
    public JsonResult deleteTask(@PathVariable Integer taskId) {
        int flag = taskService.deleteTask(taskId);
        return new JsonResult(flag > 0 ? Code.DELETE_OK : Code.DELETE_ERR, flag);
    }

    /**
     * 响应获取任务列表的请求
     *
     * @param param 任务信息（基本信息 + 类型信息）
     * @return 返回任务列表
     */
    @PostMapping("/taskName")
    public JsonResult getByName(@RequestBody Param param) {
        List<Task> task = taskService.getByName(param.getTaskName(), param.getUserName(),
                param.getPagination(), param.getRowNum());

        Integer code = task != null ? Code.GET_OK : Code.GET_ERR;
        String msg = task != null ? "" : "无数据";
        return new JsonResult(code, task, msg);
    }

    /**
     * 响应根据任务id获取任务信息的请求
     *
     * @param taskId 任务id
     * @return 返回任务信息（基本信息 + 类型信息）
     */
    @GetMapping("/{taskId}")
    public JsonResult getById(@PathVariable Integer taskId) {
        Param param = taskService.getById(taskId);
        Integer code = param != null ? Code.GET_OK : Code.GET_ERR;
        String msg = param != null ? "" : "无数据";
        return new JsonResult(code, param, msg);
    }

    /**
     * 响应获取用户管理的任务列表的请求
     *
     * @param param 任务信息（基本信息 + 类型信息）
     * @return 返回任务列表
     */
    @PostMapping("/user")
    public JsonResult getByUser(@RequestBody Param param) {
        List<Task> tasks = taskService.getByUser(param.getUserName(), param.getPagination(), param.getRowNum());
        Integer code = tasks != null ? Code.GET_OK : Code.GET_ERR;
        String msg = tasks != null ? "" : "无数据";
        return new JsonResult(code, tasks, msg);
    }

}
