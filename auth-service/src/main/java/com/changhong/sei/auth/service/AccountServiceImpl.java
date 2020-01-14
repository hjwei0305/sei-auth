package com.changhong.sei.auth.service;

import com.changhong.sei.auth.api.AccountService;
import com.changhong.sei.auth.dto.AccountDto;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.manager.AccountManager;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.manager.BaseEntityManager;
import com.changhong.sei.core.service.DefaultBaseEntityService;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:55
 */
@Service
@Api(value = "AccountService", tags = "账户接口服务")
public class AccountServiceImpl implements DefaultBaseEntityService<Account, AccountDto>, AccountService {

    @Autowired
    private AccountManager accountManager;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BaseEntityManager<Account> getManager() {
        return accountManager;
    }

    @Override
    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    /**
     * 获取数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }

    /**
     * 获取传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<AccountDto> getDtoClass() {
        return AccountDto.class;
    }

    ///////////////////////

    /**
     * 通过账户id获取已有账户
     *
     * @param id
     */
    @Override
    public ResultData<AccountDto> getById(String id) {
        Account account = accountManager.findOne(id);
        return ResultData.success(new AccountDto());
    }

    /**
     * 创建新账户
     *
     * @param dto
     */
    @Override
    public ResultData<String> create(AccountDto dto) {
        return ResultData.success(dto.getAccount());
    }

    /**
     * 更新账户
     *
     * @param dto
     */
    @Override
    public ResultData<String> udapte(AccountDto dto) {
        return ResultData.success(dto.getAccount());
    }

    /**
     * 更新密码
     *
     * @param dto
     */
    @Override
    public ResultData<String> updatePassword(AccountDto dto) {
        return ResultData.success(dto.getAccount());
    }

    /**
     * 重置密码
     *
     * @param dto
     */
    @Override
    public ResultData<String> resetPassword(AccountDto dto) {
        return ResultData.success(dto.getAccount());
    }

    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @Override
    public ResultData<PageResult<AccountDto>> findByPage(Search search) {
        return null;
    }
}
