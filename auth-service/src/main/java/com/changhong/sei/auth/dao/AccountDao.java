package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 实现功能：账户实体数据访问接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:47
 */
public interface AccountDao extends BaseEntityDao<Account> {

    /**
     * 更新密码
     *
     * @param id       账户id
     * @param password 账户新密码
     * @return 更新结果
     */
    @Modifying
    @Query("update Account a set a.password = :password where a.id = :id")
    int updatePassword(String id, String password);

    /**
     * 根据账号,租户代码查询账户
     *
     * @param account 账号
     * @param tenant  租户代码
     * @return 存在返回账号, 不存在返回null
     */
    Account findByAccountAndTenantCode(String account, String tenant);
}
