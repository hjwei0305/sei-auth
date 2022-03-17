package com.changhong.sei.auth.config;

import com.changhong.sei.auth.service.TodoTaskService;
import com.changhong.sei.auth.service.cust.DefaultTodoTaskService;
import com.changhong.sei.auth.service.cust.WorkPlusTodoTaskService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 实现功能: 自定义业务逻辑扩展配置
 *
 * @author 王锦光 wangjg
 * @version 2020-02-12 15:46
 */
@Configuration
public class ServiceCustAutoConfig {


    /**
     * 待办任务扩展实现
     *
     * @return 扩展实现
     */
    @Bean
    @ConditionalOnMissingBean(TodoTaskService.class)
    @ConditionalOnProperty(name = "sei.auth.flow.push.type", havingValue = "workplus")
    public TodoTaskService workPlusTodoTaskServiceCust() {
        return new WorkPlusTodoTaskService();
    }
    /**
     * 待办任务扩展实现
     *
     * @return 扩展实现
     */
    @Bean
    @ConditionalOnMissingBean(TodoTaskService.class)
    public TodoTaskService todoTaskServiceCust() {
        return new DefaultTodoTaskService();
    }
}
