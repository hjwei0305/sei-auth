package com.changhong.sei.auth.manager;

import com.changhong.sei.auth.dao.LoginHistoryDao;
import com.changhong.sei.auth.entity.LoginHistory;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.manager.BaseEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:38
 */
@Service
public class LoginHistoryManager extends BaseEntityManager<LoginHistory> {

    @Autowired
    private LoginHistoryDao dao;

    @Override
    protected BaseEntityDao<LoginHistory> getDao() {
        return dao;
    }


}
