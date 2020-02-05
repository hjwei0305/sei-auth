package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dao.LoginSessionDao;
import com.changhong.sei.auth.entity.LoginSession;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.service.BaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:01
 */
//@Component
public class LoginSessionService extends BaseEntityService<LoginSession> implements SessionService {

    /**
     * 超时时间(秒)
     */
    @Value("${server.servlet.session.timeout:36000}")
    private Integer sessionTimeout;

    @Autowired
    private LoginSessionDao dao;

    @Override
    protected BaseEntityDao<LoginSession> getDao() {
        return dao;
    }

    /**
     * 添加会话
     *
     * @param sid   会话id
     * @param value 会话信息,通常为token
     */
    @Override
    public void addSession(String sid, String value) {
        LoginSession session = new LoginSession();
        // 会话id
        session.setSid(sid);
        // 会话信息
        session.setToken(value);
        // 登录时间
        session.setLoginDate(System.currentTimeMillis());
        // 会话过期时间
        session.setExpireDate(System.currentTimeMillis() + sessionTimeout * 1000);

        dao.save(session);
    }

    /**
     * 会话续期
     *
     * @param sid 会话id
     */
    @Override
    public void touchSession(String sid) {
        // 续期
        dao.updateExpireDate(sid, sessionTimeout + 1000);
    }

    /**
     * 获取会话并续期
     *
     * @param sid 会话id
     * @return 存在则返回会话信息, 不存在则返回null
     */
    @Override
    public String getAndTouchSession(String sid) {
        String token = null;
        LoginSession session = dao.findFirstByProperty(LoginSession.FIELD_SID, sid);
        if (Objects.nonNull(session)) {
            // 检查是否超期
            if (session.getExpireDate() >= System.currentTimeMillis()) {
                token = session.getToken();

                // 会话过期时间
                session.setExpireDate(System.currentTimeMillis() + sessionTimeout * 1000);
                dao.save(session);
            } else {
                dao.delete(session);
            }
        }
        return token;
    }

    /**
     * @param sid     会话id
     * @param timeOut 会话延迟过期时间(秒).为保证后续业务的处理,通常需要延迟会话一定的过期时间
     */
    @Override
    public void removeSession(String sid, long timeOut) {
        LoginSession session = dao.findFirstByProperty(LoginSession.FIELD_SID, sid);
        if (Objects.nonNull(session)) {
            if (session.getExpireDate() >= System.currentTimeMillis()) {
                // 会话过期时间
                session.setExpireDate(System.currentTimeMillis() + timeOut * 1000);
                dao.save(session);
            } else {
                dao.delete(session);
            }
        }
    }
}
