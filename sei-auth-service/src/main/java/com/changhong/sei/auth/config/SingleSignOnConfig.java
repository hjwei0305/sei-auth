package com.changhong.sei.auth.config;

import com.changhong.sei.auth.common.validatecode.IVerifyCodeGen;
import com.changhong.sei.auth.common.validatecode.SimpleCharVerifyCodeGenImpl;
import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.config.properties.SsoProperties;
import com.changhong.sei.auth.event.listener.LoginListener;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.encryption.provider.Md5EncryptProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-30 10:37
 */
@Configuration
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties({AuthProperties.class, SsoProperties.class})
public class SingleSignOnConfig implements WebMvcConfigurer {

//    private final RedisConnectionFactory redisConnectionFactory;
//
//    //////////////////redis mq config start/////////////////////
//
//    @Autowired
//    public SingleSignOnConfig(RedisConnectionFactory redisConnectionFactory) {
//        this.redisConnectionFactory = redisConnectionFactory;
//    }
//
//    /**
//     * 配置消息监听器
//     */
//    @Bean
//    public OnlineSubscribeListener orderStateListener() {
//        return new OnlineSubscribeListener();
//    }
//
//    /**
//     * 将消息监听器绑定到消息容器
//     */
//    @Bean
//    public RedisMessageListenerContainer messageListenerContainer() {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(redisConnectionFactory);
//
//        //  MessageListener 监听数据
//        container.addMessageListener(orderStateListener(), ChannelTopic.of(OnlineSubscribeListener.TOPIC));
//        return container;
//    }

    /**
     * 添加静态资源的路径映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler( "/js/**", "/css/**", "/images/**")
                .addResourceLocations("classpath:/static/js/", "classpath:/static/css/", "classpath:/static/images/");
    }

    @Bean
    @ConditionalOnMissingBean(IEncrypt.class)
    public IEncrypt encrypt() {
        return new Md5EncryptProvider("");
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(IVerifyCodeGen.class)
    public IVerifyCodeGen verifyCodeGen() {
        return new SimpleCharVerifyCodeGenImpl();
    }

    @Bean
    @ConditionalOnMissingBean(LoginListener.class)
    public LoginListener loginListener() {
        return new LoginListener();
    }
}
