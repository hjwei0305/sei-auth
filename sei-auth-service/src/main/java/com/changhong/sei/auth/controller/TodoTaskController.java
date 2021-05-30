package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.TodoTaskApi;
import com.changhong.sei.auth.certification.sso.ch.ChGtAuthUtil;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.config.properties.SsoProperties;
import com.changhong.sei.auth.dto.TodoTaskInfo;
import com.changhong.sei.auth.dto.TodoTaskRequest;
import com.changhong.sei.auth.dto.TodoTaskResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.auth.service.SessionService;
import com.changhong.sei.auth.service.TodoTaskService;
import com.changhong.sei.auth.service.client.FlowClient;
import com.changhong.sei.auth.service.client.vo.FlowTaskPageResultVO;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.flow.FlowTask;
import com.changhong.sei.core.dto.serach.PageInfo;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.log.Level;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.log.annotation.Log;
import com.changhong.sei.core.service.bo.ResponseData;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.DateUtils;
import com.changhong.sei.util.Signature;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能：待办任务
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-08 16:32
 */
@Controller
@Api(value = "TodoTaskApi", tags = "待办任务webhook服务")
@RequestMapping(path = TodoTaskApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class TodoTaskController implements TodoTaskApi {

    @Autowired
    private TodoTaskService service;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private FlowClient flowClient;

    private AuthProperties authProperties;
    private SsoProperties properties;

    public TodoTaskController(AuthProperties authProperties, SsoProperties properties) {
        this.authProperties = authProperties;
        this.properties = properties;
    }


    private static final String[] AGENT = {"iphone", "android", "ipad", "phone", "mobile", "wap", "netfront", "java", "operamobi",
            "operamini", "ucweb", "windowsce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
            "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
            "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
            "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
            "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
            "pantech", "gionee", "portalmmm", "jigbrowser", "hiptop", "benq", "haier", "^lct", "320x320",
            "240x320", "176x220", "w3c", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
            "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
            "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
            "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
            "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
            "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
            "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
            "googlebot-mobile"};


    /**
     * 判断User-Agent 是不是来自于手机
     */
    private boolean checkAgentIsMobile(String ua) {
        LogUtil.error("User-Agent的类型为:" + ua);
        if (ua != null) {
            ua = ua.toLowerCase();
            // 排除 苹果桌面系统
            if (!ua.contains("windows nt") && !ua.contains("macintosh")) {
                //移动端
                return StringUtils.containsAny(ua, AGENT);
            }
        }
        return false;
    }

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

    /**
     * 待办任务清单
     *
     * @param request 带参数请求
     */
    @Override
    public TodoTaskResponse todoTaskList4CH(HttpServletRequest request) {
        String tocken = request.getParameter("token");
        LogUtil.bizLog("接收SSO待办调用, token: {}", tocken);
        TodoTaskResponse result = new TodoTaskResponse();
        try {
            TodoTaskRequest todoTaskVo = ChGtAuthUtil.unsignTodoTaskVo(tocken, properties.getSecurity());
            LogUtil.bizLog("todoTaskVo: {}", todoTaskVo);
            if (Objects.nonNull(todoTaskVo)) {
                //获取账号,并模拟用户
                Account accountObj = accountService.getByAccountAndTenantCode(todoTaskVo.getAccount(), properties.getTenant());
                //根据当前账号获取用户会话
                ResultData<SessionUser> resultData = accountService.getSessionUser(accountObj, HttpUtils.getClientIP(request),
                        request.getLocale().toLanguageTag());
                SessionUser sessionUser = resultData.getData();
                ContextUtil.generateToken(sessionUser);
                // 会话id关联token(redis或db等)
                sessionService.addSession(sessionUser.getSessionId(), sessionUser.getToken());

                LogUtil.bizLog("接收SSO待办调用 SessionUser: {}", sessionUser);

                Search search = new Search();
                //重新设置pageInfo消息 pageinfo 必不可少，否则报错
                PageInfo pageInfo = new PageInfo();
                pageInfo.setPage(todoTaskVo.getPage());
                pageInfo.setRows(todoTaskVo.getRows());
                search.setPageInfo(pageInfo);

                FlowTaskPageResultVO<FlowTask> flowTaskPageResult = flowClient.findByBusinessModelIdWithAllCount(null, search);
                if (Objects.nonNull(flowTaskPageResult) && CollectionUtils.isNotEmpty(flowTaskPageResult.getRows())) {
                    List<TodoTaskInfo> todoTaskInfos = result.getTodolist();
                    //数据条目数
                    result.setCount(String.valueOf(flowTaskPageResult.getRows().size()));

                    //当前登录账号
                    String account = sessionUser.getAccount();
                    //当前时间戳
                    String stamp = String.valueOf(System.currentTimeMillis());
                    StringBuilder sb = new StringBuilder(100);
                    TodoTaskInfo unDoTaskInfo;
                    for (FlowTask flowTask : flowTaskPageResult.getRows()) {
                        unDoTaskInfo = new TodoTaskInfo();

                        sb.delete(0, sb.length());
                        //会话token
                        String sign = Signature.sign("sei" + account + flowTask.getId() + stamp);
                        sb.append(authProperties.getApiBaseUrl()).append(request.getContextPath()).append("/ssoTask");
                        sb.append("?sign=").append(sign);
                        sb.append("&taskId=").append(flowTask.getId());
                        sb.append("&account=").append(account);
                        sb.append("&stamp=").append(stamp);
                        sb.append("&flowTypeId=").append(flowTask.getFlowInstance().getFlowDefVersion().getFlowDefination().getFlowType().getId());
                        sb.append("&id=").append(flowTask.getFlowInstance().getBusinessId());
                        sb.append("&trustState=").append(flowTask.getTrustState());
                        sb.append("&instanceId=").append(flowTask.getFlowInstance().getId());

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
        LogUtil.bizLog("接收SSO待办调用成功，返回数据" + result);
        return result;
    }

    /**
     * 单点登录鉴权跳转
     *
     * @param request  带参数请求
     * @param response 跳转响应
     */
    @Override
    public void ssoTask(HttpServletRequest request, HttpServletResponse response) {
        //当前登录账号
        String account = request.getParameter("account");
        String taskId = request.getParameter("taskId");
        //当前时间戳
        String stamp = request.getParameter("stamp");
        //会话token
        String sign = request.getParameter("sign");
        LogUtil.debug("loginid ：{}", account);
        LogUtil.debug("taskId ：{}", taskId);
        LogUtil.debug("stamp ：{}", stamp);
        LogUtil.debug("sign ：{}", sign);
        //获取账号,并模拟用户
        Account accountObj = accountService.getByAccountAndTenantCode(account, properties.getTenant());
        //根据当前账号获取用户会话
        ResultData<SessionUser> resultData = accountService.getSessionUser(accountObj, HttpUtils.getClientIP(request),
                request.getLocale().toLanguageTag());
        SessionUser sessionUser = resultData.getData();
        ContextUtil.generateToken(sessionUser);

        try {
            // 会话id关联token(redis或db等)
            sessionService.addSession(sessionUser.getSessionId(), sessionUser.getToken());
            //待办,已办区分标志 对应todo-待办 done-已办
            String todoFlag = request.getParameter("flag");
            todoFlag = StringUtils.isBlank(todoFlag) ? "todo" : todoFlag;
            //浏览器客户端信息
            String ua = request.getHeader("User-Agent");
            ResultData<String> getPage;
            if (checkAgentIsMobile(ua)) {
                getPage = buildMobileRedirectUrl(request, sessionUser, todoFlag);
            } else {
                getPage = "todo".equals(todoFlag) ? buildRedirectUrl(request, sessionUser) :
                        buildDoneRedirectUrl(request, sessionUser);
            }
            if (getPage.failed()) {
                errorMsg(response, getPage.getMessage());
                return;
            }
            //跳转页面
            response.setContentType("application/json;UTF-8");
            response.sendRedirect(getPage.getData());

        } catch (Exception e) {
            String msg = "登录认证异常:" + e.getMessage();
            LogUtil.error(msg, e);
            errorMsg(response, msg);
        }
    }

    /**
     * 构造待办跳转页面
     *
     * @param request     请求（含参数）
     * @param sessionUser 登录用户
     * @return 页面地址
     */
    private ResultData<String> buildRedirectUrl(HttpServletRequest request, SessionUser sessionUser) {
        //待办任务id
        String taskId = request.getParameter("taskId");
        //待办url
        FlowTask flowTask = flowClient.findTaskById(taskId);
        if (flowTask == null) {
            return ResultData.fail(taskId);
        }
        //系统基地址 + 待办相对地址
        StringBuilder redirectUrl = new StringBuilder(flowTask.getTaskFormUrl());
        Enumeration<String> keys = request.getParameterNames();
        boolean first = true;
        if (redirectUrl.indexOf("?") > 0) {
            first = false;
        }
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (StringUtils.endsWithAny(key, "stamp", "token")) {
                break;
            }
            redirectUrl.append(first ? "?" : "&").append(key).append("=").append(request.getParameter(key));
            first = false;
        }
        LogUtil.bizLog("请求token:" + ContextUtil.getToken());
        redirectUrl.append(first ? "?" : "&").append("sessionId").append("=").append(sessionUser.getSessionId());
        String page = redirectUrl.toString().replace("businessId", "id");
        LogUtil.error("OA登录验证成功，跳转页面：{}", page);
        return ResultData.success(page);

    }

    /**
     * 构造已办跳转页面
     *
     * @param request     请求（含参数）
     * @param sessionUser 登录用户
     * @return 页面地址
     */
    private ResultData<String> buildDoneRedirectUrl(HttpServletRequest request, SessionUser sessionUser) {
        //待办任务单号
        String businessCode = request.getParameter("businessCode");

        String redirectUrl = authProperties.getWebBaseUrl().replace("/sei-portal-web", "") + "/sei-flow-task-web/#/sei-flow-task-web/task/workDone/" +
                //拼接单号
                businessCode +
                "?sessionId=" + sessionUser.getSessionId();
        LogUtil.error("OA登录验证成功，跳转页面：{}", redirectUrl);
        return ResultData.success(redirectUrl);
    }

    /**
     * 构造移动端待办/已办跳转页面地址
     *
     * @param request     请求（含参数）
     * @param sessionUser 登录用户
     * @param todoFlag    已办/待办区分标识
     * @return 页面地址
     */
    private ResultData<String> buildMobileRedirectUrl(HttpServletRequest request, SessionUser sessionUser, String todoFlag) {
        //待办任务单号
        String businessCode = request.getParameter("businessCode");
        if (StringUtils.isBlank(businessCode) && "todo".equals(todoFlag)) {
            //待办任务id
            String taskId = request.getParameter("taskId");
            //待办url
            FlowTask flowTask = flowClient.findTaskById(taskId);
            if (flowTask != null) {
                businessCode = flowTask.getFlowInstance().getBusinessCode();
            } else {
                LogUtil.error("移动端获取待办信息为空");
            }
        }

        String redirectUrl = authProperties.getAppBaseUrl() + "/#/MyWork?sid="
                + sessionUser.getSessionId() + "&flag=" + todoFlag;
        if (StringUtils.isNotBlank(businessCode)) {
            redirectUrl = redirectUrl + "&businessCode=" + businessCode;
        }
        LogUtil.error("OA登录验证成功，跳转页面：{}", redirectUrl);
        return ResultData.success(redirectUrl);
    }

    /**
     * 返回错误响应
     *
     * @param response 响应
     */
    private void errorMsg(HttpServletResponse response, String msg) {
        //指定返回的格式为JSON格式
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(JsonUtils.toJson(ResponseData.build().setSuccess(Boolean.FALSE).setMessage(msg)));
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

}
