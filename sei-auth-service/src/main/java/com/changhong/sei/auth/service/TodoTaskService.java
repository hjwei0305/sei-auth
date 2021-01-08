package com.changhong.sei.auth.service;

import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.flow.FlowTask;

import java.util.List;

/**
 * 实现功能：待办任务
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-08 16:44
 */
public interface TodoTaskService {

    /**
     * 推送流程模块待办
     *
     * @param taskList 需要推送的待办
     */
    ResultData<Void> pushNewTask(List<FlowTask> taskList);

    /**
     * 推送流程模块已办（待办转已办）
     *
     * @param taskList 需要推送的已办（待办转已办）
     */
    ResultData<Void> pushOldTask(List<FlowTask> taskList);

    /**
     * 推送流程模块需要删除的待办
     *
     * @param taskList 需要删除的待办
     */
    ResultData<Void> pushDelTask(List<FlowTask> taskList);

    /**
     * 推送流程模块归档（正常结束）的待办
     *
     * @param task 需要归档的任务
     */
    ResultData<Void> pushEndTask(FlowTask task);
}
