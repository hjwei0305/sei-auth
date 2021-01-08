package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.TodoTaskApi;
import com.changhong.sei.auth.service.TodoTaskService;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.flow.FlowTask;
import com.changhong.sei.core.log.Level;
import com.changhong.sei.core.log.annotation.Log;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 实现功能：待办任务
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-08 16:32
 */
@RestController
@Api(value = "TodoTaskApi", tags = "待办任务webhook服务")
@RequestMapping(path = TodoTaskApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class TodoTaskController implements TodoTaskApi {

    @Autowired
    private TodoTaskService service;

    /**
     * 推送流程模块待办
     *
     * @param taskList 需要推送的待办
     */
    @Override
    @Log(value = "新待办任务", level = Level.INFO)
    public ResultData<Void> pushNewTask(List<FlowTask> taskList) {
        return service.pushNewTask(taskList);
    }

    /**
     * 推送流程模块已办（待办转已办）
     *
     * @param taskList 需要推送的已办（待办转已办）
     */
    @Override
    @Log(value = "待办转已办", level = Level.INFO)
    public ResultData<Void> pushOldTask(List<FlowTask> taskList) {
        return service.pushOldTask(taskList);
    }

    /**
     * 推送流程模块需要删除的待办
     *
     * @param taskList 需要删除的待办
     */
    @Override
    @Log(value = "删除待办任务", level = Level.INFO)
    public ResultData<Void> pushDelTask(List<FlowTask> taskList) {
        return service.pushDelTask(taskList);
    }

    /**
     * 推送流程模块归档（正常结束）的待办
     *
     * @param task 需要归档的任务
     */
    @Override
    @Log(value = "办结任务", level = Level.INFO)
    public ResultData<Void> pushEndTask(FlowTask task) {
        return service.pushEndTask(task);
    }
}
