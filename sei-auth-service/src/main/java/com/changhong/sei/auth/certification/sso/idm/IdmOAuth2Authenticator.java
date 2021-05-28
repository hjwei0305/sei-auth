package com.changhong.sei.auth.certification.sso.idm;

import com.changhong.sei.auth.certification.AbstractTokenAuthenticator;
import com.changhong.sei.auth.certification.sso.Oauth2Authenticator;
import com.changhong.sei.auth.certification.sso.SingleSignOnAuthenticator;
import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 实现功能：IDM OAuth2单点集成
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-23 17:27
 */
@Lazy
@Component(SingleSignOnAuthenticator.AUTH_TYPE_IDM_OAUTH2)
public class IdmOAuth2Authenticator extends AbstractTokenAuthenticator implements Oauth2Authenticator, SingleSignOnAuthenticator, Constants {
    private static final Logger LOG = LoggerFactory.getLogger(IdmOAuth2Authenticator.class);

    public final String authorizeUrl;
    public final String accessUrl;
    public final String profileUrl;

    @Value("${sei.auth.sso.idm.client:AUTH_CGFSSC}")
    private String clientId;
    @Value("${sei.auth.sso.idm.secret:f811ba58gewqtj9i}")
    private String clientSecret;

    private final AuthProperties properties;

    public IdmOAuth2Authenticator(AuthProperties properties) {
        this.properties = properties;

        this.authorizeUrl = ContextUtil.getProperty("sei.auth.sso.idm.authorize", "https://loginuatin.newhopedairy.cn/siam/oauth2.0/authorize");
        this.accessUrl = ContextUtil.getProperty("sei.auth.sso.idm.access", "https://loginuatin.newhopedairy.cn/siam/oauth2.0/accessTokenByJson");
        this.profileUrl = ContextUtil.getProperty("sei.auth.sso.idm.profile", "https://loginuatin.newhopedairy.cn/siam/oauth2.0/profileByJson");
    }

    /**
     * 前端web根url地址
     * 如:http://tsei.changhong.com:8090/sei-portal-web
     */
    @Override
    public String getWebBaseUrl() {
        return properties.getWebBaseUrl();
    }

    /**
     * APP根url地址
     * 如:http://tsei.changhong.com:8090/sei-app
     */
    @Override
    public String getAppBaseUrl() {
        return properties.getAppBaseUrl();
    }

    /**
     * 服务网关根url地址
     * 如:http://tsei.changhong.com:8090/api-gateway
     */
    @Override
    public String getApiBaseUrl() {
        return properties.getApiBaseUrl();
    }

    /**
     * 登录成功url地址
     */
    @Override
    public String getIndexUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        String url;
        if (agentIsMobile) {
            // 移动
            url = getAppBaseUrl() + "/#/main?sid=" + userResponse.getSessionId();
        } else {
            // PC
            url = getWebBaseUrl() + "/#/sso/ssoWrapperPage?sid=" + userResponse.getSessionId();
        }
        LOG.info("单点登录跳转地址: {}", url);

        //单点错误页面
        return url;
    }

    /**
     * openId绑定失败,需要跳转到绑定页面
     *
     * @param userResponse 用户登录失败返回信息.可能为空,注意检查
     */
    @Override
    public String getLogoutUrl(SessionUserResponse userResponse, boolean agentIsMobile, HttpServletRequest request) {
        String url = properties.getSso().getLogoutUrl();
        LOG.info("单点登录失败：需要绑定平台账号！跳转至: {}", url);
        return url;
    }

    /**
     * 返回获取授权码页面地址
     * https://loginuatin.newhopedairy.cn/siam/oauth2.0/authorize?client_id=AUTH_CGFSSC&redirect_uri=http://10.200.16.10/api-gateway/sei-auth/sso/login?authType=idmOAuth2&response_type=code
     */
    @Override
    public String getAuthorizeEndpoint(HttpServletRequest request) {
        Map<String, String> data = getAuthorizeData(request).getData();
        //这个方法的三个参数分别是授权后的重定向url、获取用户信息类型和state
        String url = "%s?client_id=%s&redirect_uri=%s&response_type=code&state=%s";

        String redirectUrl = String.format(url, authorizeUrl, clientId, data.get("redirect_uri"), data.get("state"));
        LOG.info("获取授权码页面地址: {}", redirectUrl);
        return redirectUrl;
    }

    @Override
    public ResultData<Map<String, String>> getAuthorizeData(HttpServletRequest request) {
        // 定义的一个参数，用户可以传入自定义的参数
        String state = "sei";
        // 用户IDM授权登录后重定向的页面路由
        String redirectUri = String.format("%s%s%s?authType=%s", getApiBaseUrl(), request.getContextPath(), SSO_LOGIN_ENDPOINT, SingleSignOnAuthenticator.AUTH_TYPE_IDM_OAUTH2);
        //String redirectUri = getApiBaseUrl() + request.getContextPath() + SSO_LOGIN_ENDPOINT + "?authType=" + SingleSignOnAuthenticator.AUTH_TYPE_IDM_OAUTH2;
        LOG.debug("获取授权码后重定向的页面路由: {}", redirectUri);
        try {
            redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, String> data = new HashMap<>();
        // 重定向地址，需要进行UrlEncode
        data.put("redirect_uri", redirectUri);
        // 用于保持请求和回调的状态，授权请求后原样带回给企业。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议企业带上该参数，可设置为简单的随机数加session进行校验
        data.put("state", state);
        return ResultData.success(data);
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
     * 获取用户信息
     */
    @Override
    public ResultData<SessionUserResponse> auth(HttpServletRequest request) {
        String code = request.getParameter("code");
        if (StringUtils.isNotBlank(code)) {
            // 单点登录地址
            String redirectUri = String.format("%s%s%s?authType=%s", getApiBaseUrl(), request.getContextPath(), SSO_LOGIN_ENDPOINT, SingleSignOnAuthenticator.AUTH_TYPE_IDM_OAUTH2);

            try {
                StringBuilder url = new StringBuilder();
                url.append(accessUrl)
                        .append("?client_id=").append(clientId)
                        .append("&client_secret=").append(clientSecret)
                        .append("&grant_type=authorization_code&redirect_uri=").append(redirectUri)
                        .append("&code=").append(code);
                // 取得有效的AccessToken值
                Map<String, String> data = httpRequest(url.toString(), "POST", null);
                if (data != null && StringUtils.equalsIgnoreCase("true", data.get("status"))) {
                    // access_token：访问令牌
                    String accessToken = data.get("access_token");
                    if (StringUtils.isNotBlank(accessToken) && accessToken.contains("access_token=")) {
                        accessToken = accessToken.replaceAll("access_token", "");
                    }
                    url.delete(0, url.length());
                    url.append(profileUrl).append("?access_token=").append(accessToken);
                    // 用户信息
                    Map<String, String> userMap = httpRequest(url.toString(), "POST", null);
                    LOG.info("UserInfo: {}", JsonUtils.toJson(userMap));
                    // IDM用户信息
                    String openId = userMap.get("id");

                    SessionUserResponse userResponse = new SessionUserResponse();
                    userResponse.setLoginStatus(SessionUserResponse.LoginStatus.failure);
                    userResponse.setOpenId(openId);
                    LOG.info("OpenId: {}", openId);

                    // 检查是否有账号绑定
                    ResultData<Account> resultData = accountService.checkAccount(ChannelEnum.SEI, openId);
                    LOG.info("检查是否有账号绑定: {}", resultData);
                    if (resultData.successful()) {
                        Account account = resultData.getData();

                        LoginRequest loginRequest = new LoginRequest();
                        loginRequest.setTenant(account.getTenantCode());
                        loginRequest.setAccount(account.getAccount());
                        loginRequest.setReqId(code);
                        ResultData<SessionUserResponse> result = login(loginRequest, account);
                        LOG.info("微信关联账号登录验证: {}", result);
                        userResponse.setTenantCode(account.getTenantCode());
                        userResponse.setAccount(account.getAccount());
                        userResponse.setLoginAccount(account.getOpenId());
                        SessionUserResponse sessionUserResponse = result.getData();
                        if (Objects.nonNull(sessionUserResponse)) {
                            userResponse.setSessionId(sessionUserResponse.getSessionId());
                            userResponse.setLoginStatus(sessionUserResponse.getLoginStatus());
                        }
                    }
                    return ResultData.success(userResponse);
                } else {
                    return ResultData.fail("单点登录失败:" + JsonUtils.toJson(data));
                }
            } catch (Exception e) {
                return ResultData.fail("单点登录异常: " + ExceptionUtils.getRootCauseMessage(e));
            }
        } else {
            return ResultData.fail("单点登录失败:未获取到IDM授权码");
        }
    }

    /**
     * 绑定账号
     */
    @Override
    public ResultData<SessionUserResponse> bindingAccount(LoginRequest loginRequest, boolean agentIsMobile) {
        return ResultData.fail("IDM不需要.");
    }

    @Override
    public ResultData<Map<String, String>> jsapi_ticket() {
        return ResultData.fail("IDM不需要.");
    }

    private String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            LOG.error("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }


    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static Map<String, String> httpRequest(String requestUrl, String requestMethod, String outputStr) {
        // log.error("发起https请求并获取结果 :"+requestUrl+","+requestMethod+","+outputStr);
        Map<String, String> map = new HashMap<>();
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setConnectTimeout(60 * 1000);
            httpUrlConn.setReadTimeout(60 * 1000);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            httpUrlConn.connect();
            // if ("GET".equalsIgnoreCase(requestMethod))
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

            // 返回map
            map = JsonUtils.fromJson(buffer.toString(), Map.class);
        } catch (ConnectException ce) {
            LOG.error("Connection Timed Out......", ce);
        } catch (Exception e) {
            String result = String.format("Https Request Error:%s", e);
            LOG.error(result, e);
        }
        return map;
    }
}
