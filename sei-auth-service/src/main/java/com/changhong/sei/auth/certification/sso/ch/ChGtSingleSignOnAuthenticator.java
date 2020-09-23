package com.changhong.sei.auth.certification.sso.ch;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.dto.TodoTaskRequest;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.config.properties.SsoProperties;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.exception.ServiceException;
import com.changhong.sei.util.IdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-14 14:31
 */
@Component(SingleSignOnAuthenticator.AUTH_TYPE_CH_GT)
public class ChGtSingleSignOnAuthenticator extends AbstractTokenAuthenticator implements SingleSignOnAuthenticator {
    private static final Logger LOG = LoggerFactory.getLogger(ChGtSingleSignOnAuthenticator.class);

    private static final String EXP = "exp";

    private static final String PAYLOAD = "payload";

    private static String SECURITY;
    private final AuthProperties authProperties;
    private final SsoProperties properties;
    private final AccountService accountService;

    public ChGtSingleSignOnAuthenticator(AuthProperties authProperties, SsoProperties properties, AccountService accountService) {
        this.authProperties = authProperties;
        this.properties = properties;
        this.accountService = accountService;

        //配置中心读取秘钥
        SECURITY = properties.getSecurity();
        if (StringUtils.isBlank(SECURITY)) {
            LOG.error("未配置单点认证秘钥[sei.sso.security]！");
        }
    }

    /**
     * 前端web根url地址.必须
     * 如:http://tsei.changhong.com:8090/sei-portal-web
     */
    @Override
    public String getWebBaseUrl() {
        return authProperties.getWebBaseUrl();
    }

    /**
     * APP根url地址.必须
     * 如:http://tsei.changhong.com:8090/sei-app
     */
    @Override
    public String getAppBaseUrl() {
        return authProperties.getAppBaseUrl();
    }

    /**
     * 登录成功url地址
     */
    @Override
    public String getIndexUrl(SessionUserResponse userResponse, boolean agentIsMobile) {
        String url = properties.getIndex();
        // PC登录：跳转到新版(react)的页面
        if (StringUtils.isBlank(url)) {
            url = getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + userResponse.getSessionId();
        }
        LOG.info("单点登录跳转地址: {}", url);
        return url;
    }

    /**
     * 登录失败url地址
     *
     * @param userResponse 用户登录失败返回信息.可能为空,注意检查
     */
    @Override
    public String getLogoutUrl(SessionUserResponse userResponse, boolean agentIsMobile) {
        return properties.getLogout();
    }

    /**
     * 获取用户信息
     */
    @Override
    public ResultData<SessionUserResponse> auth(HttpServletRequest request) {
        //获取单点参数
        String ssoToken = request.getParameter("token");
        LOG.debug("SSO token {}", ssoToken);

        // 获取当前登录账号
        String account = unsign(ssoToken);
        LOG.debug("SSO登录用户: {}", account);
        //超时或其他异常,返回登录页
        if (Objects.isNull(account)) {
            return ResultData.fail("单点登录异常.");
        }

        Account accountObj = accountService.getByAccountAndTenantCode(account, properties.getTenant());
        if (Objects.nonNull(accountObj)) {

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setTenant(accountObj.getTenantCode());
            loginRequest.setAccount(accountObj.getAccount());
            loginRequest.setReqId(IdGenerator.uuid2());
            return login(loginRequest, accountObj);
        } else {
            LOG.error("账号[{}]不存在，单点登录失败.", account);
            return ResultData.fail("账号[" + account + "]不存在，单点登录失败.");
        }
    }

    /**
     * 获取用户信息
     *
     * @param loginParam 授权参数
     * @return LoginDTO
     */
    @Override
    public ResultData<SessionUserResponse> auth(LoginRequest loginParam) {
        return ResultData.fail("认证类型错误.");
    }


    /**
     * JWT获取 -- 通过用户信息及过期时间长度以及密钥，获取TOKEN
     *
     * @param object 用户信息对象 里面包含数据如：{"name":"80000001"}
     * @param maxAge token过期时间长度，当过期时，解析token时，应该返回null
     */
    private static <T> String sign(T object, long maxAge) {
        if (StringUtils.isBlank(SECURITY)) {
            throw new ServiceException("未配置单点认证秘钥[sei.sso.security]！");
        }
        try {
            final JWTSigner signer = new JWTSigner(SECURITY);
            final Map<String, Object> claims = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(object);
            claims.put(PAYLOAD, jsonString);
            //获取系统时间+过期时间段毫秒数
            claims.put(EXP, System.currentTimeMillis() + maxAge);
            return signer.sign(claims);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * JWT解析 -- 通过密钥解析token，获取是否过期以及用户信息
     *
     * @param jwt 需解析的token参数
     */
    private static String unsign(String jwt) {
        if (StringUtils.isBlank(SECURITY)) {
            throw new ServiceException("未配置单点认证秘钥[sei.sso.security]！");
        }
        final JWTVerifier verifier = new JWTVerifier(SECURITY);
        try {
            //解析jwt 并转换token信息
            final Map<String, Object> claims = verifier.verify(jwt);
            //判断是否存在相应信息
            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
                //token时间
                long exp = (Long) claims.get(EXP);
                //系统当前时间
                long currentTimeMillis = System.currentTimeMillis();
                //验证token是否过期
                if (exp > currentTimeMillis) {
                    String json = (String) claims.get(PAYLOAD);
                    JSONObject jsonObject = new JSONObject(json);
                    return jsonObject.getString("name");
                } else {
                    LOG.error("token过期！");
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 待办任务token解析 -- 通过密钥解析token，获取是否过期以及用户信息
     *
     * @param jwt 需解析的token参数
     */
    public static TodoTaskRequest unsignTodoTaskVo(String jwt) {
        if (StringUtils.isBlank(SECURITY)) {
            throw new ServiceException("未配置单点认证秘钥[sei.sso.security]！");
        }
        final JWTVerifier verifier = new JWTVerifier(SECURITY);
        try {
            //解析jwt 并转换token信息
            final Map<String, Object> claims = verifier.verify(jwt);
            //判断是否存在相应信息
            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
                //token时间
                long exp = (Long) claims.get(EXP);
                //系统当前时间
                long currentTimeMillis = System.currentTimeMillis();
                //验证token是否过期
                if (exp > currentTimeMillis) {
                    String json = (String) claims.get(PAYLOAD);
                    return JsonUtils.fromJson(json, TodoTaskRequest.class);
                } else {
                    LOG.error("token过期！");
                }
            }
        } catch (Exception e) {
            LOG.error("待办任务token解析异常", e);
        }
        return null;
    }
}
