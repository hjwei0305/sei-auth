package com.changhong.sei.auth.websocket.config;

import com.changhong.sei.auth.websocket.OnlineWebSocketHandler;
import com.changhong.sei.core.commoms.constant.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 实现功能：WebSocket配置
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-08 17:21
 */
@Configuration
@EnableWebSocket
// 单元测试排除
@ConditionalOnProperty(value = "org.springframework.boot.test.context.SpringBootTestContextBootstrapper", havingValue = "false", matchIfMissing = true)
public class WebsocketAutoConfig implements WebSocketConfigurer {

    /**
     * Register {@link WebSocketHandler WebSocketHandlers} including SockJS fallback options if desired.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                //添加消息处理对象，和websocket访问地址
                .addHandler(onlineWebSocketHandler(), "/ws/online")
                //设置允许跨域访问
                .setAllowedOrigins("*")
                //添加拦截器可实现用户链接前进行权限校验等操作
                .addInterceptors(new HandshakeInterceptor() {
                    /**
                     * 前置拦截一般用来注册用户信息，绑定 WebSocketSession
                     * 握手之前，若返回false，则不建立链接
                     */
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        //将用户id放入socket处理器的会话(WebSocketSession)中
                        ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
                        //获取参数
                        String token = serverHttpRequest.getServletRequest().getHeader(Constants.HEADER_TOKEN_KEY);
                        //可以在此处进行权限验证，当用户权限验证通过后，进行握手成功操作，验证失败返回false
                        if (StringUtils.isBlank(token)) {
                            System.out.println("握手失败.....");
                            return false;
                        }
                        System.out.println("开始握手。。。。。。。");
                        return true;
                    }

                    /**
                     * Invoked after the handshake is done. The response status and headers indicate
                     * the results of the handshake, i.e. whether it was successful or not.
                     *
                     * @param request   the current request
                     * @param response  the current response
                     * @param wsHandler the target WebSocket handler
                     * @param exception an exception raised during the handshake, or {@code null} if none
                     */
                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                        System.out.println("握手成功啦。。。。。。");
                    }
                });
    }

    @Bean
    public WebSocketHandler onlineWebSocketHandler() {
        return new OnlineWebSocketHandler();
    }
}
