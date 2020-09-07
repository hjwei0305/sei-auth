package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.*;
import com.changhong.sei.core.api.FindByPageApi;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 实现功能：账户访问接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:09
 */
@FeignClient(name = "sei-auth", path = AccountApi.PATH)
public interface AccountApi extends FindByPageApi<AccountResponse> {
    /**
     * 服务访问目录
     */
    String PATH = "account";

    /**
     * 通过租户和账号获取已有账户
     */
    @GetMapping(path = "getByTenantAccount")
    @ApiOperation("通过租户和账号获取已有账户")
    ResultData<SessionUserResponse> getByTenantAccount(@RequestParam("tenant") @NotBlank String tenant,
                                                       @RequestParam("account") @NotBlank String account);

    /**
     * 通过账户id获取已有账户
     */
    @GetMapping(path = "getById")
    @ApiOperation("通过账户id获取已有账户")
    ResultData<AccountResponse> getById(@RequestParam("id") @NotBlank String id);

    /**
     * 注册新账户
     */
    @PostMapping(path = "register")
    @ApiOperation("创建新账户.有初始密码")
    ResultData<String> register(@RequestBody @Valid RegisterAccountRequest request) throws IllegalAccessException;

    /**
     * 创建新账户
     */
    @PostMapping(path = "create")
    @ApiOperation("创建新账户.无初始密码,使用平台提供的默认密码策略")
    ResultData<String> create(@RequestBody @Valid CreateAccountRequest request) throws IllegalAccessException;

    /**
     * 更新账户
     */
    @PostMapping(path = "update")
    @ApiOperation("更新账户")
    ResultData<String> update(@RequestBody @Valid UpdateAccountRequest request) throws IllegalAccessException;

    /**
     * 更新账户
     */
    @PostMapping(path = "updateByTenantAccount")
    @ApiOperation("按租户账号修改账户")
    ResultData<String> updateByTenantAccount(@RequestBody @Valid UpdateAccountByAccountRequest request) throws IllegalAccessException;

    /**
     * 更新密码
     */
    @PostMapping(path = "updatePassword")
    @ApiOperation("更新密码")
    ResultData<String> updatePassword(@RequestBody @Valid UpdatePasswordRequest request);

    /**
     * 重置密码
     *
     * @param tenant   租户代码
     * @param account  账号
     * @param password 密码(MD5散列值). 为空时,为配置的默认密码
     */
    @PostMapping(path = "resetPass")
    @ApiOperation("重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenant", value = "租户代码", required = true),
            @ApiImplicitParam(name = "account", value = "账号", required = true),
            @ApiImplicitParam(name = "password", value = "密码(MD5散列值). 为空时,为配置的默认密码")
    })
    ResultData<String> resetPassword(@RequestParam("tenant") @NotBlank String tenant,
                                     @RequestParam("account") @NotBlank String account,
                                     @RequestParam(name = "password", required = false) String password);

    /**
     * 账户冻结/解冻
     */
    @PostMapping(path = "frozen")
    @ApiOperation("账户冻结/解冻")
    ResultData<String> frozen(@RequestParam("id") @NotBlank String id, @RequestParam("frozen") boolean frozen);

    /**
     * 账户锁定/解锁
     */
    @PostMapping(path = "locked")
    @ApiOperation("账户锁定/解锁")
    ResultData<String> locked(@RequestParam("id") @NotBlank String id, @RequestParam("locked") boolean locked);

    /**
     * 通过账户id获取已有账户
     */
    @GetMapping(path = "getByUserId")
    @ApiOperation("通过用户ID获取已有账户清单")
    ResultData<List<AccountResponse>> getByUserId(@RequestParam("userId") @NotBlank String userId);

    /**
     * 绑定账号
     */
    @PostMapping(path = "binding")
    @ApiOperation("绑定账号")
    ResultData<String> binding(@RequestBody @Valid BindingAccountRequest request);

    /**
     * 解绑账号
     */
    @PostMapping(path = "unbinding")
    @ApiOperation("解绑账号")
    ResultData<String> unbinding(@RequestBody @Valid BindingAccountRequest request);



}
