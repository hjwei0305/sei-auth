package com.changhong.sei.auth.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-30 10:38
 */
@ConfigurationProperties("sei.auth")
public class AuthProperties {
    /**
     * 前端web根url地址
     * 如:http://tsei.changhong.com:8090/sei-portal-web
     */
    private String webBaseUrl;
    /**
     * APP根url地址
     * 如:http://tsei.changhong.com:8090/sei-app
     */
    private String appBaseUrl;
    /**
     * 服务网关根url地址
     * 如:http://tsei.changhong.com:8090/api-gateway
     */
    private String apiBaseUrl;
    /**
     * 默认密码
     */
    private String password = "123456";
    /**
     * 密码默认过期天数
     */
    private Integer passwordExpire = 180;

    private SingleSignOnProperties sso = new SingleSignOnProperties();

    public String getWebBaseUrl() {
        return webBaseUrl;
    }

    public void setWebBaseUrl(String webBaseUrl) {
        this.webBaseUrl = webBaseUrl;
    }

    public String getAppBaseUrl() {
        return appBaseUrl;
    }

    public void setAppBaseUrl(String appBaseUrl) {
        this.appBaseUrl = appBaseUrl;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPasswordExpire() {
        return passwordExpire;
    }

    public void setPasswordExpire(Integer passwordExpire) {
        this.passwordExpire = passwordExpire;
    }

    public SingleSignOnProperties getSso() {
        return sso;
    }

    public void setSso(SingleSignOnProperties sso) {
        this.sso = sso;
    }

    public static class SingleSignOnProperties {
        /**
         * 登录成功url地址
         */
        private String index;
        /**
         * 登录失败url地址
         */
        private String logout;
        /**
         * 认证类型
         * QQ\微信\企业微信\钉钉\微博\长虹SSO\泛微OA\蓝凌OA
         */
        private String authType;
        /**
         * 社交平台分发给SEI平台的id
         * 企业微信可视为corpId
         */
        private String appId;
        /**
         * 企业微信:自建应用id
         */
        private String agentId;
        /**
         * 访问密钥
         */
        private String cropSecret;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getLogout() {
            return logout;
        }

        public void setLogout(String logout) {
            this.logout = logout;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getCropSecret() {
            return cropSecret;
        }

        public void setCropSecret(String cropSecret) {
            this.cropSecret = cropSecret;
        }
    }
}
