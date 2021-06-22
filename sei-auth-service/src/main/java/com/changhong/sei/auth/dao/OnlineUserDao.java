package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.OnlineUser;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:35
 */
@Repository
public interface OnlineUserDao extends BaseEntityDao<OnlineUser> {

    /**
     * 获取当前所有会话
     *
     * @return 当前所有会话
     */
    @Query("select t from OnlineUser t where t.timestamp > 0")
    List<OnlineUser> getAllOnlineUsers();

    /**
     * 删除会话
     *
     * @param sids 要删除的会话id清单
     */
    @Modifying
    @Query("delete from OnlineUser t where t.sid in :sids ")
    void removeSids(@Param("sids") Set<String> sids);
}
