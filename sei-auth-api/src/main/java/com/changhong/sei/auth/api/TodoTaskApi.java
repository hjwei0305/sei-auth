package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.TodoTaskResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.flow.FlowTask;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 实现功能：待办任务webhook
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-08 16:38
 */
@FeignClient(name = "sei-auth", path = TodoTaskApi.PATH)
public interface TodoTaskApi {
    /**
     * 服务访问目录
     */
    String PATH = "task";

    /**
     * 推送流程模块待办
     *
     * @param taskList 需要推送的待办
     */
    @ResponseBody
    @PostMapping(path = "pushNewTask", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "推送流程模块待办", notes = "推送流程模块待办")
    ResultData<Void> pushNewTask(@RequestBody List<FlowTask> taskList);

    /**
     * 推送流程模块已办（待办转已办）
     *
     * @param taskList 需要推送的已办（待办转已办）
     */
    @ResponseBody
    @PostMapping(path = "pushOldTask", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "推送流程模块已办（待办转已办）", notes = "推送流程模块已办（待办转已办）")
    ResultData<Void> pushOldTask(@RequestBody List<FlowTask> taskList);

    /**
     * 推送流程模块需要删除的待办
     *
     * @param taskList 需要删除的待办
     */
    @ResponseBody
    @PostMapping(path = "pushDelTask", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "推送流程模块需要删除的待办", notes = "推送流程模块需要删除的待办")
    ResultData<Void> pushDelTask(@RequestBody List<FlowTask> taskList);

    /**
     * 推送流程模块归档（正常结束）的待办
     *
     * @param task 需要归档的任务
     */
    @ResponseBody
    @PostMapping(path = "pushEndTask", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "推送流程模块归档（正常结束）的待办", notes = "推送流程模块归档（正常结束）的待办")
    ResultData<Void> pushEndTask(@RequestBody FlowTask task);

    /**
     * 待办任务清单
     */
    @ResponseBody
    @ApiOperation(value = "长虹SSO待办任务清单", notes = "长虹SSO待办任务清单")
    @RequestMapping(value = {"/todoTaskList4CH"}, method = {RequestMethod.GET, RequestMethod.POST})
    TodoTaskResponse todoTaskList4CH(HttpServletRequest request);

    /**
     * 单点登录鉴权跳转
     *
     * @param request  带参数请求
     * @param response 跳转响应
     */
    @RequestMapping(path = "ssoTask", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "单点登录鉴权跳转", notes = "第三方系统通过链接进入,进行登录认证,并跳转到相应页面")
    void ssoTask(HttpServletRequest request, HttpServletResponse response);

}
