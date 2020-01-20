package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 实现功能：账户实体数据访问接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:47
 */
public interface AccountDao extends BaseEntityDao<Account> {

    /**
     * 更新密码
     * @param id 账户id
     * @param passwordHash 账户新密码
     * @return 更新结果
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update com.changhong.sei.auth.entity.Account a set a.passwordHash = :passwordHash where a.id = :id")
    int updatePassword(@Param("id") String id, @Param("passwordHash") String passwordHash);
}
