package com.changhong.sei.auth.service;

import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * 实现功能：会话管理
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 11:48
 */
@Service
public class SessionService {

    private final JwtTokenUtil jwtTokenUtil;
    private final CacheBuilder cacheBuilder;
    @Autowired
    private OnlineUserService onlineUserService;

    public SessionService(JwtTokenUtil jwtTokenUtil, CacheBuilder cacheBuilder) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.cacheBuilder = cacheBuilder;
    }

    /**
     * 添加会话
     *
     * @param sessionUser 会话信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void addSession(SessionUser sessionUser) {
        final String sid = sessionUser.getSessionId();
        final String value = sessionUser.getToken();
        cacheBuilder.set(Constants.REDIS_KEY_PREFIX + sid, value, onlineUserService.getExpireTime());
        onlineUserService.addSession(sessionUser);
    }

    /**
     * 获取会话并续期
     *
     * @param sid 会话id
     * @return 存在则返回会话信息, 不存在则返回null
     */
    public String getAndTouchSession(String sid) {
        String value = cacheBuilder.get(Constants.REDIS_KEY_PREFIX + sid);
        try {
            if (StringUtils.isNotBlank(value)) {
                Date date = jwtTokenUtil.getExpirationDateFromToken(value);
                long s = date.getTime() - (new Date()).getTime();
                if (s > 0) {
                    // 小于5分钟
                    if (s <= 300000) {
                        value = jwtTokenUtil.refreshToken(value, sid);
                    }
                    // 续期
                    cacheBuilder.set(Constants.REDIS_KEY_PREFIX + sid, value, onlineUserService.getExpireTime());
                } else {
                    value = null;
                }
            }
        } catch (Exception e) {
            value = null;
        }
        return value;
    }

    /**
     * 会话续期
     *
     * @param sid 会话id
     */
    public void touchSession(String sid) {
        getAndTouchSession(sid);
    }

    /**
     * @param sid     会话id
     * @param timeOut 会话延迟过期时间(秒).为保证后续业务的处理,通常需要延迟会话一定的过期时间
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeSession(final String sid, final long timeOut) {
        CompletableFuture.runAsync(() -> {
            if (timeOut > 0) {
                String value = cacheBuilder.get(Constants.REDIS_KEY_PREFIX + sid);
                // 续期
                cacheBuilder.set(Constants.REDIS_KEY_PREFIX + sid, value, timeOut);
            } else {
                // 立即删除
                cacheBuilder.remove(Constants.REDIS_KEY_PREFIX + sid);
            }
        });
        onlineUserService.removeSession(sid);
    }

}
