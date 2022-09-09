package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dto.FlowTaskDto;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.service.client.vo.FlowTaskVo;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.flow.FlowTask;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.service.bo.ResponseData;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 实现功能：待办任务
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-08 16:44
 */
public interface TodoTaskService {

    String[] AGENT = {"iphone", "android", "ipad", "phone", "mobile", "wap", "netfront", "java", "operamobi",
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
    default boolean checkAgentIsMobile(HttpServletRequest request) {
        //浏览器客户端信息
        String userAgent = request.getHeader("User-Agent");
        if (LogUtil.isInfoEnabled()) {
            LogUtil.info("User-Agent的类型为: {}", userAgent);
        }
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            // 排除 苹果桌面系统
            if (!userAgent.contains("windows nt") && !userAgent.contains("macintosh")) {
                //移动端
                return StringUtils.containsAny(userAgent, AGENT);
            }
        }
        return false;
    }

    default void toJson(String message, HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        //指定返回的格式为JSON格式
        response.setContentType("application/json;charset=utf-8");
        //setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println(JsonUtils.toJson(ResponseData.build().setSuccess(Boolean.FALSE).setMessage(message)));
            out.flush();
        } catch (IOException e) {
            LogUtil.error("输出JSON数据异常", e);
        }
    }

    /**
     * 获取待办任务处理相对URL地址
     * 不含基地址.
     * 使用时需要追加
     * 如: authProperties.getApiBaseUrl() + request.getContextPath() + getTodoTaskRelativeUrl(task);
     * http://sei.xxx.com/api-gateway/sei-auth/task/ssoTask?taskId=xxx
     *
     * @param taskId 任务id
     * @return 返回待办处理相对url地址."/task/ssoTask?taskId=" + task.getId()
     */
    default String getTodoTaskRelativeUrl(String taskId) {
        return "/task/ssoTask?taskId=" + taskId;
    }

    /**
     * 推送流程模块待办
     *
     * @param taskList 需要推送的待办
     */
    ResultData<Void> pushNewTask(List<FlowTask> taskList, HttpServletRequest request);

    /**
     * 推送流程模块已办（待办转已办）
     *
     * @param taskList 需要推送的已办（待办转已办）
     */
    ResultData<Void> pushOldTask(List<FlowTask> taskList, HttpServletRequest request);

    /**
     * 推送流程模块需要删除的待办
     *
     * @param taskList 需要删除的待办
     */
    ResultData<Void> pushDelTask(List<FlowTask> taskList, HttpServletRequest request);

    /**
     * 推送流程模块归档（正常结束）的待办
     *
     * @param task 需要归档的任务
     */
    ResultData<Void> pushEndTask(FlowTask task, HttpServletRequest request);

    /**
     * 获取待办任务清单
     */
    Object getTodoTasks(HttpServletRequest request);

    /**
     * 单点登录鉴权跳转
     *
     * @param request  带参数请求
     * @param response 跳转响应
     */
    void ssoDoTask(HttpServletRequest request, HttpServletResponse response);
}
