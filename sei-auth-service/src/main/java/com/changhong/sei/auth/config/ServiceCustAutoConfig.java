package com.changhong.sei.auth.config;

import com.changhong.sei.auth.certification.sso.ch.ChGtSingleSignOnAuthenticator;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.service.TodoTaskService;
import com.changhong.sei.auth.service.client.FlowClient;
import com.changhong.sei.auth.service.cust.DefaultTodoTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 实现功能: 自定义业务逻辑扩展配置
 *
 * @author 王锦光 wangjg
 * @version 2020-02-12 15:46
 */
@Configuration
public class ServiceCustAutoConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceCustAutoConfig.class);

    /**
     * 待办任务扩展实现
     *
     * @return 扩展实现
     */
    @Bean
    @ConditionalOnMissingBean(TodoTaskService.class)
    public TodoTaskService todoTaskServiceCust(AuthProperties authProperties, ChGtSingleSignOnAuthenticator authenticator, FlowClient flowClient) {
        LOG.info("使用默认待办任务推送");
        return new DefaultTodoTaskService(authProperties, authenticator, flowClient);
    }

}
