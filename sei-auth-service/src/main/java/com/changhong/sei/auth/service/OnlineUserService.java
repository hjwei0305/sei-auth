package com.changhong.sei.auth.service;

import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.common.OSUtil;
import com.changhong.sei.auth.dao.OnlineUserDao;
import com.changhong.sei.auth.entity.OnlineUser;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.limiter.support.lock.SeiLock;
import com.changhong.sei.core.service.BaseEntityService;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:01
 */
@Service
public class OnlineUserService extends BaseEntityService<OnlineUser> {
    private static final Logger LOG = LoggerFactory.getLogger(OnlineUserService.class);
    /**
     * 超时时间(秒)
     */
    @Value("${server.servlet.session.timeout:36000}")
    private Integer sessionTimeout;

    @Autowired
    private OnlineUserDao dao;
    @Autowired
    private LoginHistoryService loginHistoryService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected BaseEntityDao<OnlineUser> getDao() {
        return dao;
    }

    /**
     * @return 获取会话超时时间
     */
    public long getExpireTime() {
        return sessionTimeout * 1000;
    }

    /**
     * 添加会话
     *
     * @param sessionUser 会话信息
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void addSession(SessionUser sessionUser) {
        OnlineUser session = new OnlineUser();
        // 会话id
        session.setSid(sessionUser.getSessionId());
        session.setTenantCode(sessionUser.getTenantCode());
        session.setUserId(sessionUser.getUserId());
        session.setUserAccount(sessionUser.getAccount());
        session.setUserName(sessionUser.getUserName());
        // 登录时间
        session.setLoginDate(LocalDateTime.now());
        session.setTimestamp(System.currentTimeMillis());
        // 登录ip
        session.setLoginIp(ThreadLocalUtil.getTranVar("ClientIP"));
        String agent = ThreadLocalUtil.getTranVar("UserAgent");
        session.setLoginUserAgent(agent);

        //解析agent字符串
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        //获取浏览器对象
        Browser browser = userAgent.getBrowser();
        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 浏览器名
        session.setBrowser(OSUtil.simplifyBrowser(browser.getName()));
        // 操作系统名
        session.setOsName(OSUtil.simplifyOperatingSystem(operatingSystem.getName()));

        dao.save(session);
    }

    /**
     * 移除会话
     *
     * @param sid 会话id
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void removeSession(String sid) {
        OnlineUser session = dao.findFirstByProperty(OnlineUser.FIELD_SID, sid);
        if (Objects.nonNull(session)) {
            dao.delete(session);
        }
        // 更新退出时间
        loginHistoryService.setLogoutTime(sid);
    }

    /**
     * 定时注销
     */
//    @Async
    @Transactional(rollbackFor = Exception.class)
    @SeiLock(key = "'auth:timedLogout'")
    public void timedLogout() {
        int count = 0;
        Set<String> keys = stringRedisTemplate.keys(Constants.REDIS_KEY_PREFIX.concat("*"));
        List<OnlineUser> users = dao.getAllOnlineUsers();
        if (CollectionUtils.isNotEmpty(users)) {
            List<String> sidList = users.stream().map(OnlineUser::getSid).collect(Collectors.toList());
            users.clear();
            Set<String> delSids = sidList.stream()
                    .filter(o -> !keys.contains(Constants.REDIS_KEY_PREFIX.concat(o)))
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(delSids)) {
                count = delSids.size();
                // 移除会话
                dao.removeSids(delSids);
                // 更新退出时间
                loginHistoryService.batchSetLogoutTime(delSids);
            }
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("自动清除会话: {}个", count);
        }
    }
}
