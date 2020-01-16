package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.AccountDto;
import com.changhong.sei.core.api.FindByPageService;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 实现功能：账户访问接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:09
 */
@RestController
@RequestMapping(path = "account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface AccountService extends FindByPageService<AccountDto> {

    /**
     * 通过账户id获取已有账户
     */
    @GetMapping(path = "getById")
    @ApiOperation("通过账户id获取已有账户")
    ResultData<AccountDto> getById(@RequestParam("id") String id);

    /**
     * 创建新账户
     */
    @PostMapping(path = "create")
    @ApiOperation("创建新账户")
    ResultData<String> create(@RequestBody AccountDto dto);

    /**
     * 更新账户
     */
    @PostMapping(path = "udapte")
    @ApiOperation("更新账户")
    ResultData<String> udapte(@RequestBody AccountDto dto);

    /**
     * 更新密码
     */
    @PostMapping(path = "updatePassword")
    @ApiOperation("更新密码")
    ResultData<String> updatePassword(@RequestBody AccountDto dto);

    /**
     * 重置密码
     */
    @PostMapping(path = "resetPass")
    @ApiOperation("重置密码")
    ResultData<String> resetPassword(@RequestBody AccountDto dto);
}
