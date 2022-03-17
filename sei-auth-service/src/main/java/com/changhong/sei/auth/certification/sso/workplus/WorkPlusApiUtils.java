package com.changhong.sei.auth.certification.sso.workplus;

import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author <a href="mailto:xiaogang.su@changhong.com">粟小刚</a>
 * @description 实现功能: workPlus工具类
 * @date 2022/03/17 14:52
 */
public class WorkPlusApiUtils {

    private static final Logger LOG = LoggerFactory.getLogger(WorkPlusApiUtils.class);

    /**
     * 获取Token
     */
    private static final String GET_TOKEN_URL = "/v1/token";
    /**
     * ticket验证
     */
    private static final String TICKET_CHECK_URL = "/v1/tickets/%s?access_token=%s";
    /**
     * 获取用户信息
     */
    private static final String GET_USER_INFO_URL = "/v1/users/%s?access_token=%s&type=id";
    /**
     * 推送消息
     */
    private static final String SEND_MSG_URL = "/v1/apps/mbox?access_token=%s";
    /**
     * 获取accessToken
     *
     * @param host         API基地址
     * @param domainID     域ID
     * @param orgId        组织ID
     * @param clientSecret APP Secret
     * @param clientId     APP key
     * @return token
     */
    public static ResultData<String> getAccessToken(String host, String domainID, String orgId, String clientSecret, String clientId) {
        // 域ID
        if (StringUtils.isBlank(domainID)) {
            return ResultData.fail("domain_id不能为空.");
        }
        // 组织ID
        if (StringUtils.isBlank(orgId)) {
            return ResultData.fail("org_id不能为空.");
        }
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("grant_type", "client_credentials");
        paramMap.put("scope", "app");
        paramMap.put("domain_id", domainID);
        paramMap.put("org_id", orgId);
        paramMap.put("client_secret", clientSecret);
        paramMap.put("client_id", clientId);
        String url = host + GET_TOKEN_URL;
        String paramString = JsonUtils.toJson(paramMap);
        LOG.debug("WorkPlus认证请求: {},参数: {}", url, paramString);
        try {
            String result = HttpUtils.sendPost(url, paramString);
            LOG.debug("WorkPlus认证请求结果: {}", result);
            if (StringUtils.isBlank(result)) {
                return ResultData.fail("WorkPlus认证失败,返回Token结果为空.");
            }
            Map<String, Object> resultMap = JsonUtils.fromJson(result, HashMap.class);
            if (Objects.isNull(resultMap)) {
                return ResultData.fail("WorkPlus认证失败,返回结果Map为空.");
            }
            String token = (String) ((Map) resultMap.get("result")).get("access_token");
            if (StringUtils.isBlank(token)) {
                return ResultData.fail("WorkPlus认证失败,返回Token为空.");
            }
            return ResultData.success(token);
        } catch (Exception e) {
            LOG.error("发起WorkPlus平台请求[" + url + "]异常.", e);
            return ResultData.fail("发起WorkPlus平台请求[" + url + "]异常.");
        }
    }

    /**
     * 校验ticket
     *
     * @param host   API基地址
     * @param ticket 票据
     * @param token  token
     * @return ResultData userId
     */
    public static ResultData<String> checkTicket(String host, String ticket, String token) {
        // ticket
        if (StringUtils.isBlank(ticket)) {
            return ResultData.fail("ticket不能为空.");
        }
        if (StringUtils.isBlank(token)) {
            return ResultData.fail("token不能为空.");
        }
        String url = String.format(host + TICKET_CHECK_URL, ticket, token);
        LOG.debug("WorkPlus验证Ticket请求: {}", url);
        try {
            String result = HttpUtils.sendGet(url);
            LOG.debug("WorkPlus验证Ticket请求结果: {}", result);
            if (StringUtils.isBlank(result)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回结果为空.");
            }
            Map<String, Object> resultMap = JsonUtils.fromJson(result, HashMap.class);
            if (Objects.isNull(resultMap)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回结果Map为空.");
            }
            String status = String.valueOf(resultMap.get("status"));
            if (StringUtils.isBlank(status)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回Status为空.");
            }
            if (!Objects.equals("0", status)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回Status为: " + status);
            }
            String userId = (String) ((Map) resultMap.get("result")).get("client_id");
            if (StringUtils.isBlank(userId)) {
                return ResultData.fail("WorkPlus验证Ticket失败,返回client_id为空.");
            }
            return ResultData.success(userId);
        } catch (Exception e) {
            LOG.error("WorkPlus验证Ticket请求[" + url + "]异常.", e);
            return ResultData.fail("WorkPlus验证Ticket请求[" + url + "]异常.");
        }
    }

    /**
     * 获取用户信息
     *
     * @param host   API基地址
     * @param userId 用户ID
     * @param token  token
     * @return ResultData userId
     */
    public static ResultData<String> getUserInfo(String host, String userId, String token) {
        // userId
        if (StringUtils.isBlank(userId)) {
            return ResultData.fail("userId不能为空.");
        }
        if (StringUtils.isBlank(token)) {
            return ResultData.fail("token不能为空.");
        }
        String url = String.format(host + GET_USER_INFO_URL, userId, token);
        LOG.debug("WorkPlus获取用户信息请求: {}", url);
        try {
            String result = HttpUtils.sendGet(url);
            LOG.debug("WorkPlus获取用户信息请求结果: {}", result);
            if (StringUtils.isBlank(result)) {
                return ResultData.fail("WorkPlus获取用户信息失败,返回结果为空.");
            }
            Map<String, Object> resultMap = JsonUtils.fromJson(result, HashMap.class);
            if (Objects.isNull(resultMap)) {
                return ResultData.fail("WorkPlus获取用户信息失败,返回结果Map为空.");
            }
            String userName = (String) ((Map) resultMap.get("result")).get("username");
            if (StringUtils.isBlank(userName)) {
                return ResultData.fail("WorkPlus获取用户信息失败,返回userName为空.");
            }
            return ResultData.success(userName);
        } catch (Exception e) {
            LOG.error("WorkPlus获取用户信息[" + url + "]异常.", e);
            return ResultData.fail("WorkPlus获取用户信息[" + url + "]异常.");
        }
    }

    /**
     * 发送消息
     * @param host API基地址
     * @param message 消息参数
     * @param token token
     * @return
     */
    public static ResultData<String> sendMsg(String host,Map message, String token){
        String url = String.format(host + SEND_MSG_URL, token);
        String paramString = JsonUtils.toJson(message);
        LOG.debug("WorkPlus消息推送: {},参数: {}", url, paramString);
        try {
            String result = HttpUtils.sendPost(url,paramString);
            LOG.debug("WorkPlus消息推送结果: {}", result);
            if (StringUtils.isBlank(result)) {
                return ResultData.fail("WorkPlus消息推送失败,返回结果为空.");
            }
            Map<String, Object> resultMap = JsonUtils.fromJson(result, HashMap.class);
            if (Objects.isNull(resultMap)) {
                return ResultData.fail("WorkPlus消息推送失败,返回结果Map为空.");
            }
            String status = String.valueOf(resultMap.get("status"));
            if (StringUtils.isBlank(status)) {
                return ResultData.fail("WorkPlus消息推送失败,返回Status为空.");
            }
            if (!Objects.equals("0", status)) {
                String errorMessage = (String) resultMap.get("message");
                return ResultData.fail("WorkPlus消息推送失败,返回消息为: " + errorMessage);
            }
            return ResultData.success();
        } catch (Exception e) {
            LOG.error("WorkPlus消息推送失败[" + url + "]异常.", e);
            return ResultData.fail("WorkPlus消息推送失败[" + url + "]异常.");
        }
    }

}
