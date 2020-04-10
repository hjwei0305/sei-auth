package com.changhong.sei.auth.service;

import com.changhong.sei.core.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:01
 */
@Component
public class RedisSessionService implements SessionService {

    @Value("${server.servlet.session.timeout:36000}")
    private Integer sessionTimeout;
    private final static String REDIS_KEY_PREFIX = "auth:login:";

    @Autowired
    private CacheBuilder cacheBuilder;

    /**
     * 添加会话
     *
     * @param sid   会话id
     * @param value 会话信息,通常为token
     */
    @Override
    public void addSession(String sid, String value) {
        cacheBuilder.set(REDIS_KEY_PREFIX + sid, value, getExpireTime());
    }

    /**
     * 会话续期
     *
     * @param sid 会话id
     */
    @Override
    public void touchSession(String sid) {
        String value = cacheBuilder.get(REDIS_KEY_PREFIX + sid);
        // 续期
        cacheBuilder.set(REDIS_KEY_PREFIX + sid, value, getExpireTime());
    }

    /**
     * 获取会话并续期
     *
     * @param sid 会话id
     * @return 存在则返回会话信息, 不存在则返回null
     */
    @Override
    public String getAndTouchSession(String sid) {
        String value = cacheBuilder.get(REDIS_KEY_PREFIX + sid);
        // 续期
        cacheBuilder.set(REDIS_KEY_PREFIX + sid, value, getExpireTime());

        return value;
    }

    /**
     * @param sid     会话id
     * @param timeOut 会话延迟过期时间(秒).为保证后续业务的处理,通常需要延迟会话一定的过期时间
     */
    @Override
    public void removeSession(String sid, long timeOut) {
        if (timeOut > 0) {
            String value = cacheBuilder.get(REDIS_KEY_PREFIX + sid);
            // 续期
            cacheBuilder.set(REDIS_KEY_PREFIX + sid, value, timeOut);
        } else {
            // 立即删除
            cacheBuilder.remove(REDIS_KEY_PREFIX + sid);
        }
    }

    private long getExpireTime() {
        return sessionTimeout * 1000;
    }
}
