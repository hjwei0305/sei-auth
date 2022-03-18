package com.changhong.sei.auth.service.cust;

import com.changhong.sei.auth.certification.sso.workplus.WorkPlusApiUtils;
import com.changhong.sei.auth.service.TodoTaskService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.flow.FlowTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 实现功能：WorkPlus待办任务二开扩展类
 * {@see https://www.tapd.cn/55596372/markdown_wikis/show/#1155596372001000122}
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-08 16:48
 */
public class WorkPlusTodoTaskService implements TodoTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkPlusTodoTaskService.class);

    /**
     * 推送流程模块待办
     *
     * @param taskList 需要推送的待办
     */
    @Override
    public ResultData<Void> pushNewTask(List<FlowTask> taskList) {
        LOG.info("进入WorkPlus推送流程模块待办");
        String templateId = ContextUtil.getProperty("sei.auth.sso.workplus.flow.push.templateId");
        List<Map> messages = new ArrayList<>();
        for (FlowTask flowTask : taskList) {
            //组装消息
            Map<String, Object> message = new HashMap<>();
            message.put("type", "TEMPLATE");
            Map<String, Object> body = new HashMap<>();
            body.put("template_id",templateId);
            List<Map> data = new ArrayList<>();
            Map<String, Object> param1 = new HashMap<>();
            param1.put("key","orderNo");
            param1.put("value",flowTask.getFlowInstance().getBusinessCode());
            data.add(param1);
            Map<String, Object> param2 = new HashMap<>();
            param2.put("key","creatorName");
            param2.put("value",flowTask.getFlowInstance().getCreatorName());
            data.add(param2);
            Map<String, Object> param3 = new HashMap<>();
            param3.put("key","content");
            param3.put("value",flowTask.getFlowInstance().getBusinessModelRemark());
            data.add(param3);
            Map<String, Object> param4 = new HashMap<>();
            param4.put("key","orderId");
            param4.put("value",flowTask.getFlowInstance().getBusinessId());
            data.add(param4);
            body.put("data", data);
            message.put("body",body);
            message.put("usernames", Arrays.asList(flowTask.getExecutorAccount()));
            messages.add(message);
        }
        String workPlusApiHost = ContextUtil.getProperty("sei.auth.sso.workplus.api.host");
        String domainID = ContextUtil.getProperty("sei.auth.sso.workplus.flow.push.domainId");
        String orgId = ContextUtil.getProperty("sei.auth.sso.workplus.flow.push.orgId");
        String clientSecret = ContextUtil.getProperty("sei.auth.sso.workplus.flow.push.appSecret");
        String clientId = ContextUtil.getProperty("sei.auth.sso.workplus.flow.push.appKey");
        //获取accessToken
        ResultData<String> resultData = WorkPlusApiUtils.getAccessToken(workPlusApiHost, domainID, orgId, clientSecret, clientId);
        if (resultData.failed()) {
            LOG.error("推送流程待办至WorkPlus消息失败:" + resultData.getMessage());
        }
        String token = resultData.getData();
        for (Map message : messages) {
            resultData = WorkPlusApiUtils.sendMsg(workPlusApiHost,message,token);
            if (resultData.failed()) {
                LOG.error("推送流程待办至WorkPlus消息失败:" + resultData.getMessage());
            }
        }
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
