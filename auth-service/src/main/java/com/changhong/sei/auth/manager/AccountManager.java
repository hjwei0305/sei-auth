package com.changhong.sei.auth.manager;

import com.changhong.sei.auth.dao.AccountDao;
import com.changhong.sei.auth.dto.AccountDto;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.manager.BaseEntityManager;
import com.changhong.sei.core.manager.bo.OperateResultWithData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
     * 参数为空检查并保存
     * @param dto 账户dto
     * @return 检查结果
     */
    public ResultData<String> checkNullAndSave(AccountDto dto){
        if(dto == null){
            return ResultData.fail("参数不能为空！");
        }
        if(dto.getAccount()==null){
            return ResultData.fail("账号不能为空！");
        }
        if(dto.getName()==null){
            return ResultData.fail("名称不能为空！");
        }
        if(dto.getPassword()==null){
            return ResultData.fail("密码不能为空！");
        }
        Account account = new Account();
        BeanUtils.copyProperties(dto, account);
        OperateResultWithData<Account> saveResult = this.save(account);
        if(saveResult.notSuccessful()){
            return ResultData.fail(saveResult.getMessage());
        }
        return ResultData.success(dto.getAccount());
    }

    /**
     * 更新密码
     * @param dto 账户dto
     * @return 更新结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultData<String> updatePassword(AccountDto dto){
        if(dto == null){
            return ResultData.fail("参数不能为空！");
        }
        if(dto.getId() == null){
            return ResultData.fail("ID不能为空！");
        }
        if(dto.getPassword()==null){
            return ResultData.fail("密码不能为空！");
        }
        String id = dto.getId();
        Account account = dao.findOne(id);
        if(ObjectUtils.isEmpty(account)){
            return ResultData.fail("账户不存在！");
        }
        if(!ObjectUtils.isEmpty(account) && account.getPassword().equals(dto.getPassword())){
            return ResultData.fail("新密码与原密码相同，请重新输入！");
        }
        int i = dao.updatePassword(id, dto.getPassword());
        if(i!=1){
            return ResultData.fail("密码更新失败！");
        }
        return ResultData.success("密码更新成功！", dto.getPassword());
    }
}
