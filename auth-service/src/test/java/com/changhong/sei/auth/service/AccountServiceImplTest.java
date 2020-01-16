package com.changhong.sei.auth.service;

import com.changhong.com.sei.core.test.BaseUnitTest;
import com.changhong.sei.auth.dto.AccountDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: 杨浩
 * @Description:
 * @Date: 2020/1/16 14:57
 */
public class AccountServiceImplTest extends BaseUnitTest {

    @Autowired
    private AccountServiceImpl service;

    @Test
    public void getById() {
        ResultData<AccountDto> resultData = service.getById("76046094-3832-11EA-92DA-1063C8D2143D");
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void create() {
        AccountDto dto = new AccountDto();
        dto.setAccount("88001031");
        dto.setName("账号测试");
        dto.setPassword("123qweAS");
        ResultData<String> resultData = service.create(dto);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void update() {
        ResultData<AccountDto> result = service.getById("76046094-3832-11EA-92DA-1063C8D2143D");
        AccountDto dto = result.getData();
        dto.setName("账户名修改测试");
        ResultData<String> resultData = service.update(dto);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void updatePassword() {
        ResultData<AccountDto> result = service.getById("76046094-3832-11EA-92DA-1063C8D2143D");
        AccountDto dto = result.getData();
        dto.setPassword("99999");
        ResultData<String> resultData = service.updatePassword(dto);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void resetPassword() {
        ResultData<AccountDto> result = service.getById("76046094-3832-11EA-92DA-1063C8D2143D");
        AccountDto dto = result.getData();
        ResultData<String> resultData = service.resetPassword(dto);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void findByPage() {
        Search search = new Search();
        ResultData<PageResult<AccountDto>> resultData = service.findByPage(search);
        System.out.println(JsonUtils.toJson(resultData));
    }
}
