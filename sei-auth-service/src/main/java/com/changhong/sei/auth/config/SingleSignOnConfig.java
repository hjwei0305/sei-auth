package com.changhong.sei.auth.config;

import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.config.properties.SsoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-30 10:37
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties({AuthProperties.class, SsoProperties.class})
public class SingleSignOnConfig {

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
}
