package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.LoginSession;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:35
 */
@Repository
public interface LoginSessionDao extends BaseEntityDao<LoginSession> {

    /**
     * 更新过期时间
     *
     * @param sid        会话id
     * @param expireDate 过期时间
     */
    @Modifying
    @Query("update LoginSession a set a.expireDate = :expireDate where a.sid = :sid")
    void updateExpireDate(@Param("sid") String sid, @Param("expireDate") long expireDate);

}
