package com.changhong.sei.auth.manager;

import com.changhong.sei.auth.dao.AccountDao;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.manager.BaseEntityManager;
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

    /**
     * 更新密码
     * @param id 账户id
     * @param newPassword 新密码
     * @return 更新结果
     */
    public ResultData<String> updatePassword(String id, String newPassword){
        int i = dao.updatePassword(id, newPassword);
        if(i!=1){
            return ResultData.fail("密码更新失败！");
        }
        return ResultData.success(newPassword);
    }
}
