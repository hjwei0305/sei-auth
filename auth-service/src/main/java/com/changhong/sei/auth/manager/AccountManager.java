package com.changhong.sei.auth.manager;

import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.manager.BaseEntityManager;
import com.changhong.sei.auth.dao.AccountDao;
import com.changhong.sei.auth.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实现功能：平台账户业务逻辑实现
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:53
 */
@Service
public class AccountManager extends BaseEntityManager<Account> {

    @Autowired
    private AccountDao dao;

    @Override
    protected BaseEntityDao<Account> getDao() {
        return dao;
    }

}
