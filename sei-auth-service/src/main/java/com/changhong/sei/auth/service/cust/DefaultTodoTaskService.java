package com.changhong.sei.auth.service.cust;

import com.changhong.sei.auth.certification.sso.ch.ChGtSingleSignOnAuthenticator;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.connection.EipConnector;
import com.changhong.sei.auth.dto.*;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.auth.service.TodoTaskService;
import com.changhong.sei.auth.service.client.FlowClient;
import com.changhong.sei.auth.service.client.vo.FlowTaskPageResultVO;
import com.changhong.sei.auth.service.client.vo.FlowTaskVo;
import com.changhong.sei.auth.webservice.eipMall.SvcHdrTypes;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.flow.FlowTask;
import com.changhong.sei.core.dto.serach.PageInfo;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能：待办任务二开扩展类
 * {@see https://www.tapd.cn/55596372/markdown_wikis/show/#1155596372001000122}
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-08 16:48
 */
public class DefaultTodoTaskService implements TodoTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTodoTaskService.class);

    private final AuthProperties authProperties;
    private final ChGtSingleSignOnAuthenticator authenticator;
    private final FlowClient flowClient;
    @Autowired
    private AccountService accountService;

    public DefaultTodoTaskService(AuthProperties authProperties, ChGtSingleSignOnAuthenticator authenticator, FlowClient flowClient) {
        this.authProperties = authProperties;
        this.authenticator = authenticator;
        this.flowClient = flowClient;
    }

    /**
     * 推送流程模块待办
     *
     * @param taskList 需要推送的待办
     */
    @Override
    public ResultData<Void> pushNewTask(List<FlowTask> taskList, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(taskList)) {
            return ResultData.success();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("接收到的待办任务: {}", JsonUtils.toJson(taskList));
        }

        try {
            // TODO 按项目实际情况集成
            String data = "";
            for (FlowTask task : taskList) {
                EipMailDto dto = new EipMailDto();
                String url = authProperties.getApiBaseUrl() +"/sei-auth" + getTodoTaskRelativeUrl(task.getId())+
                        "&account="+task.getExecutorAccount() +
                        "&tenant="+task.getTenantCode()+
                        "&id="+task.getFlowInstance().getBusinessId();
                // 待办处理地址
                dto.setAccount(task.getExecutorAccount());
                dto.setMailID(task.getId());
                dto.setUrl(url);
                dto.setMailBody(task.getFlowName());
                dto.setMailSubject(task.getTaskName());
                SvcHdrTypes flag = EipConnector.addEipMall(dto);
                LogUtil.bizLog("EIP日志"+flag.getRDESC());
                LogUtil.bizLog("EIP日志"+flag.getESBCODE());
                LOG.info("待办消息处理URL: {}", url);
            }
            LOG.info("待办消息推送内容: {}", data);
            // String result = HttpUtils.sendPost(authProperties.getTaskPushUrl(), data);
            // LOG.info("待办消息推送结果: {}", result);
            return ResultData.success();
        } catch (Exception e) {
            LOG.error("待办消息推送异常", e);
            return ResultData.fail("待办消息推送异常: " + e.getMessage());
        }
    }

    /**
     * 推送流程模块已办（待办转已办）
     *
     * @param taskList 需要推送的已办（待办转已办）
     */
    @Override
    public ResultData<Void> pushOldTask(List<FlowTask> taskList, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(taskList)) {
            return ResultData.success();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("接收到的待办转已办任务: {}", JsonUtils.toJson(taskList));
        }

        try {
            // TODO 按项目实际情况集成
            String data = "";
            LOG.info("已办消息推送内容: {}", data);
            // String result = HttpUtils.sendPost(authProperties.getTaskPushUrl(), data);
            // LOG.info("已办消息推送结果: {}", result);
            return ResultData.success();
        } catch (Exception e) {
            LOG.error("已办消息推送异常", e);
            return ResultData.fail("已办消息推送异常: " + e.getMessage());
        }
    }

    /**
     * 推送流程模块需要删除的待办
     *
     * @param taskList 需要删除的待办
     */
    @Override
    public ResultData<Void> pushDelTask(List<FlowTask> taskList, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(taskList)) {
            return ResultData.success();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("接收到的删除待办任务: {}", JsonUtils.toJson(taskList));
        }

        try {
            // TODO 按项目实际情况集成
            String data = "";
            LOG.info("删除待办推送内容: {}", data);
            // String result = HttpUtils.sendPost(authProperties.getTaskPushUrl(), data);
            // LOG.info("删除待办推送结果: {}", result);
            return ResultData.success();
        } catch (Exception e) {
            LOG.error("删除待办推送异常", e);
            return ResultData.fail("删除待办推送异常: " + e.getMessage());
        }
    }

    /**
     * 推送流程模块归档（正常结束）的待办
     *
     * @param task 需要归档的任务
     */
    @Override
    public ResultData<Void> pushEndTask(FlowTask task, HttpServletRequest request) {

        return ResultData.success();
    }

    /**
     * 获取待办任务清单
     */
    @Override
    public TodoTaskResponse getTodoTasks(HttpServletRequest request) {
        String token = request.getParameter("token");
        LogUtil.bizLog("接收SSO待办调用, token: {}", token);
        TodoTaskResponse result = new TodoTaskResponse();
        try {

            ResultData<TodoTaskRequest> resultData = authenticator.getTaskListAuth(request);
            if (resultData.successful()) {
                TodoTaskRequest taskRequest = resultData.getData();
                if (LOG.isInfoEnabled()) {
                    LOG.info("接收SSO待办调用 SessionUser: {}", taskRequest);
                }

                Search search = new Search();
                //重新设置pageInfo消息 pageinfo 必不可少，否则报错
                PageInfo pageInfo = new PageInfo();
                pageInfo.setPage(Objects.isNull(taskRequest.getPage()) ? 1 : taskRequest.getPage());
                pageInfo.setRows(Objects.isNull(taskRequest.getRows()) ? 10 : taskRequest.getRows());
                search.setPageInfo(pageInfo);

                FlowTaskPageResultVO<FlowTask> flowTaskPageResult = flowClient.findByBusinessModelIdWithAllCount("", search);
                if (Objects.nonNull(flowTaskPageResult) && CollectionUtils.isNotEmpty(flowTaskPageResult.getRows())) {
                    List<TodoTaskInfo> todoTaskInfos = result.getTodolist();
                    //数据条目数
                    result.setCount(String.valueOf(flowTaskPageResult.getRows().size()));

                    //当前时间戳
                    String stamp = String.valueOf(System.currentTimeMillis());
                    StringBuilder sb = new StringBuilder(100);
                    TodoTaskInfo unDoTaskInfo;
                    for (FlowTask flowTask : flowTaskPageResult.getRows()) {
                        unDoTaskInfo = new TodoTaskInfo();

                        sb.delete(0, sb.length());
                        //会话token
                        sb.append(authProperties.getApiBaseUrl()).append(request.getContextPath());
                        sb.append(getTodoTaskRelativeUrl(flowTask.getId()));
                        sb.append("&token=").append(token);
                        sb.append("&stamp=").append(stamp);

                        unDoTaskInfo.setTodoId(String.valueOf(System.currentTimeMillis()));
                        unDoTaskInfo.setTodoNo(flowTask.getFlowInstance().getBusinessCode());
                        unDoTaskInfo.setStartTime(DateUtils.formatDate(flowTask.getCreatedDate(), DateUtils.DEFAULT_TIME_FORMAT));
                        unDoTaskInfo.setStartUser(flowTask.getCreatorName());
                        unDoTaskInfo.setTodoTitle("【" + flowTask.getFlowInstance().getBusinessCode() + "】" + flowTask.getFlowName() + "-" + flowTask.getTaskName());
                        unDoTaskInfo.setTodoUrl(sb.toString());

                        todoTaskInfos.add(unDoTaskInfo);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error("待办任务清单异常.", e);
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("接收SSO待办调用成功，返回数据 {}", result);
        }
        return result;
    }

    /**
     * 单点登录鉴权跳转
     *
     * @param request  带参数请求
     * @param response 跳转响应
     */
    @Override
    public void ssoDoTask(HttpServletRequest request, HttpServletResponse response) {
        // 参考todoTaskContrller实现
        //当前登录账号
        String account = request.getParameter("account");
        String taskId = request.getParameter("taskId");
        //当前时间戳
        String stamp = request.getParameter("stamp");
        //会话token
        String sign = request.getParameter("sign");
        if (LOG.isInfoEnabled()) {
            LOG.info("loginid ：{}, taskId ：{}, stamp ：{}, sign ：{}", account, taskId, stamp, sign);
        }
        //获取账号,并模拟用户
        Account accountObj = accountService.getByAccountAndTenantCode(account, "DONLIM");
    }

}
