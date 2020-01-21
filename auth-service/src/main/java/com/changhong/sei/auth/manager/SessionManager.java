package com.changhong.sei.auth.manager;

/**
 * 实现功能：会话管理
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 11:48
 */
public interface SessionManager {

    /**
     * 添加会话
     *
     * @param sid   会话id
     * @param value 会话信息,通常为token
     */
    void addSession(String sid, String value);

    /**
     * 会话续期
     *
     * @param sid 会话id
     */
    void touchSession(String sid);

    /**
     * 获取会话并续期
     *
     * @param sid 会话id
     * @return 存在则返回会话信息, 不存在则返回null
     */
    String getAndTouchSession(String sid);

    /**
     * @param sid     会话id
     * @param timeOut 会话延迟过期时间(秒).为保证后续业务的处理,通常需要延迟会话一定的过期时间
     */
    void removeSession(String sid, long timeOut);
}
