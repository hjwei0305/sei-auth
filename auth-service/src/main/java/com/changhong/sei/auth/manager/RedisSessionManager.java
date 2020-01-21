package com.changhong.sei.auth.manager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:01
 */
@Component
public class RedisSessionManager implements SessionManager {

    @Value("${server.servlet.session.timeout:36000}")
    private Integer sessionTimeout;
    //@Value("${sei.auth.session.redis.prefix}")
    private final static String REDIS_KEY_PREFIX = "auth:login:";

    @Autowired
//    private CacheManager cacheManager;
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加会话
     *
     * @param sid   会话id
     * @param value 会话信息,通常为token
     */
    @Override
    public void addSession(String sid, String value) {
        BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps(REDIS_KEY_PREFIX + sid);
        operations.set(value, sessionTimeout, TimeUnit.SECONDS);
    }

    /**
     * 会话续期
     *
     * @param sid 会话id
     */
    @Override
    public void touchSession(String sid) {
        BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps(REDIS_KEY_PREFIX + sid);
        String token = operations.get();
        if (StringUtils.isNotBlank(token)) {
            // 续期
            operations.set(token, sessionTimeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 获取会话并续期
     *
     * @param sid 会话id
     * @return 存在则返回会话信息, 不存在则返回null
     */
    @Override
    public String getAndTouchSession(String sid) {
        BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps(REDIS_KEY_PREFIX + sid);
        String token = operations.get();
        if (StringUtils.isNotBlank(token)) {
            // 续期
            operations.set(token, sessionTimeout, TimeUnit.SECONDS);
        }
        return token;
    }

    /**
     * @param sid     会话id
     * @param timeOut 会话延迟过期时间(秒).为保证后续业务的处理,通常需要延迟会话一定的过期时间
     */
    @Override
    public void removeSession(String sid, long timeOut) {
        if (timeOut > 0) {
            BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps(REDIS_KEY_PREFIX + sid);
            String token = operations.get();
            if (StringUtils.isNotBlank(token)) {
                // 延迟删除
                operations.set(token, timeOut, TimeUnit.SECONDS);
            }
        } else {
            // 立即删除
            stringRedisTemplate.delete(REDIS_KEY_PREFIX + sid);
        }
    }
}
