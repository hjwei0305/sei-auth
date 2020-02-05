package com.changhong.sei.auth.dao;

import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 实现功能：账户实体数据访问接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:47
 */
@Repository
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
     * 更新账户冻结状态
     *
     * @param id     账户id
     * @param frozen 冻结状态
     * @return 更新结果
     */
    @Modifying
    @Query("update Account a set a.frozen = :frozen where a.id = :id")
    int updateFrozen(String id, boolean frozen);

    /**
     * 更新账户锁定状态
     *
     * @param id     账户id
     * @param locked 锁定状态
     * @return 更新结果
     */
    @Modifying
    @Query("update Account a set a.locked = :locked where a.id = :id")
    int updateLocked(String id, boolean locked);

    /**
     * 根据账号查询账户
     *
     * @param account 账号
     * @return 存在返回账号, 不存在返回null
     */
    List<Account> findByAccount(String account);

    /**
     * 根据账号,租户代码查询账户
     *
     * @param account 账号
     * @param tenant  租户代码
     * @return 存在返回账号, 不存在返回null
     */
    Account findByAccountAndTenantCode(String account, String tenant);
}
