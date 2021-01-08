package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.AccountApi;
import com.changhong.sei.auth.dto.*;
import com.changhong.sei.auth.entity.Account;
import com.changhong.sei.auth.entity.AccountInfo;
import com.changhong.sei.auth.service.AccountService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:55
 */
@RestController
@Api(value = "AccountApi", tags = "账户接口服务")
@RequestMapping(path = AccountApi.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController implements AccountApi {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 通过租户和账号获取已有账户
     *
     * @param tenant  租户
     * @param account 账号
     */
    @Override
    public ResultData<SessionUserResponse> getByTenantAccount(String tenant, String account) {
        Account accountObj = accountService.getByAccountAndTenantCode(account, tenant);
        if (Objects.isNull(accountObj)) {
            return ResultData.fail("账户不存在！");
        }
        ResultData<SessionUser> resultData = accountService.getSessionUser(accountObj, StringUtils.EMPTY, StringUtils.EMPTY);
        if (resultData.successful()) {
            SessionUser sessionUser = resultData.getData();
            SessionUserResponse dto = SessionUserResponse.build().setLoginStatus(SessionUserResponse.LoginStatus.success);
            dto.setSessionId(sessionUser.getSessionId());
            dto.setTenantCode(sessionUser.getTenantCode());
            dto.setUserId(sessionUser.getUserId());
            dto.setAccount(sessionUser.getAccount());
            dto.setLoginAccount(sessionUser.getLoginAccount());
            dto.setUserName(sessionUser.getUserName());
            dto.setUserType(sessionUser.getUserType());
            dto.setAuthorityPolicy(sessionUser.getAuthorityPolicy());
            dto.setLocale(sessionUser.getLocale());
            return ResultData.success(dto);
        } else {
            return ResultData.fail(resultData.getMessage());
        }
    }

    /**
     * 通过账户id获取已有账户
     *
     * @param id 账户id
     */
    @Override
    public ResultData<AccountResponse> getById(String id) {
        Account account = accountService.findOne(id);
        if (account == null) {
            return ResultData.fail("账户不存在！");
        }
        AccountResponse dto = modelMapper.map(account, AccountResponse.class);
        return ResultData.success(dto);
    }

    /**
     * 更新账户
     */
    @Override
    public ResultData<Void> updateAccountInfo(AccountInfoDto infoDto) {
        return accountService.updateAccountInfo(modelMapper.map(infoDto, AccountInfo.class));
    }

    /**
     * 创建新账户
     *
     * @param request 账户dto
     */
    @Override
    public ResultData<String> register(RegisterAccountRequest request) {
        Account account = modelMapper.map(request, Account.class);
        if (Objects.isNull(account)) {
            return ResultData.fail("参数不能为空！");
        }
        AccountInfo accountInfo = modelMapper.map(request, AccountInfo.class);

        return accountService.createAccount(account, accountInfo, false);
    }

    /**
     * 创建新账户
     *
     * @param request 账户dto
     */
    @Override
    public ResultData<String> create(CreateAccountRequest request) {
        Account account = modelMapper.map(request, Account.class);
        if (Objects.isNull(account)) {
            return ResultData.fail("参数不能为空！");
        }
        account.setPassword(StringUtils.EMPTY);
        AccountInfo accountInfo = modelMapper.map(request, AccountInfo.class);

        return accountService.createAccount(account, accountInfo, true);
    }

    /**
     * 更新账户
     *
     * @param request 更新账户
     */
    @Override
    public ResultData<String> update(UpdateAccountRequest request) {
        if (ObjectUtils.isEmpty(request.getId())) {
            return ResultData.fail("参数id不能为空！");
        }
        Account account = accountService.findOne(request.getId());
        if (account == null) {
            return ResultData.fail("账户数据不存在！");
        }
        // 允许修改的账户信息
        account.setName(request.getName());
        account.setChannel(request.getChannel());
        if (Objects.nonNull(request.getFrozen())) {
            account.setFrozen(request.getFrozen());
        }
        if (Objects.nonNull(request.getLocked())) {
            account.setLocked(request.getLocked());
        }
        if (Objects.nonNull(request.getAccountExpired())) {
            account.setAccountExpired(request.getAccountExpired());
        }

        AccountInfo accountInfo = modelMapper.map(request, AccountInfo.class);

        return accountService.saveAccount(account, accountInfo);
    }

    /**
     * 更新账户
     *
     * @param request 更新账户
     */
    @Override
    public ResultData<String> updateByTenantAccount(UpdateAccountByAccountRequest request) {
        Account account = accountService.getByAccountAndTenantCode(request.getAccount(), request.getTenantCode());
        if (Objects.isNull(account)) {
            return ResultData.fail("租户[" + request.getTenantCode() + "]账户[" + request.getAccount() + "]数据不存在！");
        }
        // 允许修改的账户信息
        account.setName(request.getName());
        if (Objects.nonNull(request.getFrozen())) {
            account.setFrozen(request.getFrozen());
        }
        if (Objects.nonNull(request.getLocked())) {
            account.setLocked(request.getLocked());
        }
        if (Objects.nonNull(request.getAccountExpired())) {
            account.setAccountExpired(request.getAccountExpired());
        }
        AccountInfo accountInfo = modelMapper.map(request, AccountInfo.class);

        return accountService.saveAccount(account, accountInfo);
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
        return accountService.updatePassword(request);
    }

    /**
     * 重置密码
     *
     * @param tenant  租户
     * @param account 账号
     */
    @Override
    public ResultData<String> resetPassword(String tenant, String account, String password) {
        return accountService.resetPassword(tenant, account, password);
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
        PageResult<Account> pageResult = accountService.findByPage(search);
        pageResult.getRows().forEach(d -> newRows.add(modelMapper.map(d, AccountResponse.class)));
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
        return accountService.frozen(id, frozen);
    }

    /**
     * 账户锁定/解锁
     *
     * @param id 账户id
     */
    @Override
    public ResultData<String> locked(String id, boolean locked) {
        return accountService.locked(id, locked);
    }

    /**
     * 通过账户id获取已有账户
     *
     * @param userId 用户id
     */
    @Override
    public ResultData<List<AccountResponse>> getByUserId(String userId) {
        List<Account> accounts = accountService.findListByProperty(Account.FIELD_USER_ID, userId);
        if (CollectionUtils.isNotEmpty(accounts)) {
            List<AccountResponse> responseList = new ArrayList<>(accounts.size());
            accounts.forEach(a -> responseList.add(modelMapper.map(a, AccountResponse.class)));
            return ResultData.success(responseList);
        } else {
            return ResultData.fail("用户ID[" + userId + "]未找到对应的账户信息");
        }
    }

    /**
     * 绑定账号
     */
    @Override
    public ResultData<String> binding(BindingAccountRequest request) {
        SessionUser user = ContextUtil.getSessionUser();
        request.setTenantCode(user.getTenantCode());
        request.setUserId(user.getUserId());
        request.setAccount(user.getAccount());
        request.setName(user.getUserName());

        return accountService.bindingAccount(request);
    }

    /**
     * 解绑账号
     */
    @Override
    public ResultData<String> unbinding(BindingAccountRequest request) {
        return accountService.unbinding(request.getOpenId(), request.getChannel());
    }

    /**
     * 找回密码验证码
     *
     * @param accountId 账号id
     * @param channel   通道
     * @return 返回验证码
     */
    @Override
    public ResultData<String> sendVerifyCode(String accountId, String channel) {
        return accountService.sendVerifyCode(accountId, channel);
    }

    /**
     * 检查账号是否存在
     */
    @Override
    public ResultData<CheckAccountResponse> checkExisted(CheckAccountRequest request) {
        return accountService.findPassword4Check(request);
    }

    /**
     * 找回密码
     */
    @Override
    public ResultData<String> findPassword(FindPasswordRequest request) {
        return accountService.doFindPassword(request);
    }
}
