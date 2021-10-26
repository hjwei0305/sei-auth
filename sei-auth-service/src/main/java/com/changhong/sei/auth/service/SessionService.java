package com.changhong.sei.auth.service;

import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.core.util.JwtTokenUtil;
import com.changhong.sei.util.Signature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 实现功能：会话管理
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 11:48
 */
@Service
public class SessionService {
    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final CacheBuilder cacheBuilder;
    @Autowired
    private OnlineUserService onlineUserService;
    // @Value("${sei.auth.enable.cookie:false}")
    // private boolean enableCookie;
    @Value("${sei.auth.enable.cross-browser-tab:true}")
    private boolean enableCrossBrowserTab = true;

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
        this.addSession(sessionUser, onlineUserService.getExpireTime());
    }

    /**
     * 添加会话
     *
     * @param sessionUser 会话信息
     * @param expire      会话超时时间(毫秒)
     */
    @Transactional(rollbackFor = Exception.class)
    public void addSession(SessionUser sessionUser, long expire) {
        final String sid = sessionUser.getSessionId();
        final String value = sessionUser.getToken();
        cacheBuilder.set(Constants.REDIS_KEY_PREFIX + sid, value, expire);
        onlineUserService.addOnlineUser(sessionUser);

        // if (enableCookie) {
        try {
            HttpServletRequest request = HttpUtils.getRequest();
            HttpServletResponse response = HttpUtils.getResponse();
            if (Objects.nonNull(request) && Objects.nonNull(response)) {
                byte[] encodedCookieBytes = Base64.getEncoder().encode(sid.getBytes());
                String sidBase64 = new String(encodedCookieBytes);
                // sid写入cookie
                HttpUtils.writeCookieValue(Constants.COOKIE_SID, sidBase64, request, response);
                // 用户id写入cookie,以此控制同一个浏览器客户端是否允许登录不同的账号
                HttpUtils.writeCookieValue(Constants.COOKIE_CLIENT, Signature.sign(sessionUser.getUserId()), request, response);
            }
        } catch (Exception e) {
            LOG.error("登录写入cookie异常", e);
        }
        // }
    }

    /**
     * 获取会话并续期
     *
     * @param sid 会话id
     * @return 存在则返回会话信息, 不存在则返回null
     */
    public String touchSession(String sid) {
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
        if (StringUtils.isNotBlank(value) && !enableCrossBrowserTab) {
            try {
                HttpServletRequest request = HttpUtils.getRequest();
                HttpServletResponse response = HttpUtils.getResponse();
                if (Objects.nonNull(request) && Objects.nonNull(response)) {
                    // 用户id写入cookie,以此控制同一个浏览器客户端是否允许登录不同的账号
                    String userIdSign = HttpUtils.readCookieValue(Constants.COOKIE_CLIENT, request);
                    if (StringUtils.isNotBlank(userIdSign)) {
                        // 获取当前用户会话
                        SessionUser sessionUser = ContextUtil.getSessionUser(value);
                        if (!userIdSign.equals(Signature.sign(sessionUser.getUserId()))) {
                            value = null;
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("登录写入cookie异常", e);
            }
        }
        return value;
    }

    /**
     * @param sid     会话id
     * @param timeOut 会话延迟过期时间(秒).为保证后续业务的处理,通常需要延迟会话一定的过期时间
     */
    @Transactional(rollbackFor = Exception.class)
    public void removeSession(final String sid, final long timeOut) {
        LogUtil.bizLog("SID: " + sid);
        onlineUserService.removeOnlineUser(sid);

        CompletableFuture.runAsync(() -> {
            if (timeOut > 0) {
                String value = cacheBuilder.get(Constants.REDIS_KEY_PREFIX + sid);
                // 续期
                cacheBuilder.set(Constants.REDIS_KEY_PREFIX + sid, value, timeOut);
            } else {
                // 立即删除
                cacheBuilder.remove(Constants.REDIS_KEY_PREFIX + sid);
            }

            // 删除cookie
            try {
                HttpServletRequest request = HttpUtils.getRequest();
                if (Objects.nonNull(request)) {
                    // 删除cookie
                    HttpUtils.deleteCookie(Constants.COOKIE_SID, request);
                    // 删除cookie
                    HttpUtils.deleteCookie(Constants.COOKIE_CLIENT, request);
                }
            } catch (Exception e) {
                LOG.error("登录删除cookie异常", e);
            }
        });
    }

    public SessionUser getSessionUser(String sid) {
        String token = cacheBuilder.get(Constants.REDIS_KEY_PREFIX + sid);
        try {
            return ContextUtil.getSessionUser(token);
        } catch (Exception e) {
            return new SessionUser();
        }
    }
}
