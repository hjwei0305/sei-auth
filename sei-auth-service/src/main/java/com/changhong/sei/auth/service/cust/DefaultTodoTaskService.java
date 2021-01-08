package com.changhong.sei.auth.service.cust;

import com.changhong.sei.auth.service.TodoTaskService;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.flow.FlowTask;

import java.util.List;

/**
 * 实现功能：待办任务二开扩展类
 * {@see https://www.tapd.cn/55596372/markdown_wikis/show/#1155596372001000122}
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-08 16:48
 */
public class DefaultTodoTaskService implements TodoTaskService {

    /**
     * 推送流程模块待办
     *
     * @param taskList 需要推送的待办
     */
    @Override
    public ResultData<Void> pushNewTask(List<FlowTask> taskList) {

        return ResultData.success();
    }

    /**
     * 推送流程模块已办（待办转已办）
     *
     * @param taskList 需要推送的已办（待办转已办）
     */
    @Override
    public ResultData<Void> pushOldTask(List<FlowTask> taskList) {

        return ResultData.success();
    }

    /**
     * 推送流程模块需要删除的待办
     *
     * @param taskList 需要删除的待办
     */
    @Override
    public ResultData<Void> pushDelTask(List<FlowTask> taskList) {

        return ResultData.success();
    }

    /**
     * 推送流程模块归档（正常结束）的待办
     *
     * @param task 需要归档的任务
     */
    @Override
    public ResultData<Void> pushEndTask(FlowTask task) {

        return ResultData.success();
    }
}
