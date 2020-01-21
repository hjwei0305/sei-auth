package com.changhong.sei.auth.service;

import com.changhong.sei.auth.api.AccountService;
import com.changhong.sei.auth.dto.AccountRequest;
import com.changhong.sei.auth.dto.AccountResponse;
import com.changhong.sei.auth.dto.RegisterAccountRequest;
import com.changhong.sei.auth.dto.UpdatePasswordRequest;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.manager.AccountManager;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.manager.BaseEntityManager;
import com.changhong.sei.core.manager.bo.OperateResultWithData;
import com.changhong.sei.core.service.DefaultBaseEntityService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:55
 */
@Service
@Api(value = "AccountService", tags = "账户接口服务")
public class AccountServiceImpl implements DefaultBaseEntityService<Account, AccountResponse>, AccountService {

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
    public Class<AccountResponse> getDtoClass() {
        return AccountResponse.class;
    }

    ///////////////////////

    /**
     * 通过账户id获取已有账户
     *
     * @param id 账户id
     */
    @Override
    public ResultData<AccountResponse> getById(String id) {
        Account account = accountManager.findOne(id);
        if (account == null) {
            return ResultData.fail("账户不存在！");
        }
        AccountResponse dto = convertToDto(account);
        return ResultData.success(dto);
    }

    /**
     * 创建新账户
     *
     * @param request 账户dto
     */
    @Override
    public ResultData<String> register(RegisterAccountRequest request) {
        Account account = convertToEntity(request);
        if (Objects.isNull(account)) {
            return ResultData.fail("参数不能为空！");
        }

        return accountManager.createAccount(account);
    }

    /**
     * 创建新账户
     *
     * @param request 账户dto
     */
    @Override
    public ResultData<String> create(AccountRequest request) {
        Account account = convertToEntity(request);
        if (Objects.isNull(account)) {
            return ResultData.fail("参数不能为空！");
        }
        account.setPassword(StringUtils.EMPTY);

        return accountManager.createAccount(account);
    }

    /**
     * 更新账户
     *
     * @param dto 账户dto
     */
    @Override
    public ResultData<String> update(AccountRequest dto) throws IllegalAccessException {
        Account account = convertToEntity(dto);
        if (ObjectUtils.isEmpty(account.getId())) {
            return ResultData.fail("参数id不能为空！");
        }
        Account oldAccount = accountManager.findOne(account.getId());
        if (oldAccount == null) {
            return ResultData.fail("账户数据不存在！");
        }
        // 密码不能被修改
        account.setPassword(oldAccount.getPassword());

        OperateResultWithData<Account> resultWithData = accountManager.save(account);
        if (resultWithData.notSuccessful()) {
            return ResultData.fail(resultWithData.getMessage());
        }
        return ResultData.success(dto.getAccount());
    }

    /**
     * 更新密码
     *
     * @param request 账户
     */
    @Override
    public ResultData<String> updatePassword(UpdatePasswordRequest request) {
        if (request == null) {
            return ResultData.fail("请求参数不能为空！");
        }
        return accountManager.updatePassword(request);
    }

    /**
     * 重置密码
     *
     * @param tenant  租户
     * @param account 账号
     */
    @Override
    public ResultData<String> resetPassword(String tenant, String account) {
        return accountManager.resetPassword(tenant, account);
    }

    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @Override
    public ResultData<PageResult<AccountResponse>> findByPage(Search search) {
        PageResult<AccountResponse> newPageResult = new PageResult<>();
        List<AccountResponse> newRows = new ArrayList<>();
        PageResult<Account> pageResult = accountManager.findByPage(search);
        pageResult.getRows().forEach(d -> newRows.add(convertToDto(d)));
        newPageResult.setPage(pageResult.getPage());
        newPageResult.setRecords(pageResult.getRecords());
        newPageResult.setTotal(pageResult.getTotal());
        newPageResult.setRows(newRows);
        return ResultData.success(newPageResult);
    }

    /**
     * 账户冻结
     *
     * @param id 账户id
     */
    @Override
    public ResultData<String> frozen(String id, boolean frozen) {
        return accountManager.frozen(id, frozen);
    }

    /**
     * 账户锁定/解锁
     *
     * @param id 账户id
     */
    @Override
    public ResultData<String> locked(String id, boolean locked) {
        return accountManager.locked(id, locked);
    }
}
