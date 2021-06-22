package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:35
 */
@Repository
public interface LoginHistoryDao extends BaseEntityDao<LoginHistory> {

    /**
     * 更新会话登出时间
     *
     * @param sid      会话id
     * @param dateTime 登出时间
     */
    @Modifying
    @Query("update LoginHistory l set l.logoutDate = :dateTime where l.sessionId = :sid ")
    void setLogoutTime(@Param("sid") String sid, @Param("dateTime") LocalDateTime dateTime);

    /**
     * 更新会话登出时间
     *
     * @param sids     会话id
     * @param dateTime 登出时间
     */
    @Modifying
    @Query("update LoginHistory l set l.logoutDate = :dateTime where l.sessionId in :sids ")
    void batchSetLogoutTime(@Param("sids") Set<String> sids, @Param("dateTime") LocalDateTime dateTime);
}
