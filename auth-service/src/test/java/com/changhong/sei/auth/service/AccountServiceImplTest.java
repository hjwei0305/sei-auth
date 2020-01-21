package com.changhong.sei.auth.service;

import com.changhong.com.sei.core.test.BaseUnitTest;
import com.changhong.sei.auth.dto.AccountDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @Author: 杨浩
 * @Description:
 * @Date: 2020/1/16 14:57
 */
public class AccountServiceImplTest extends BaseUnitTest {

    @Autowired
    private AccountServiceImpl service;
    @Autowired
    private IEncrypt encrypt;

    private final static String ID = "58092E60-3B5F-11EA-B974-1063C8D2143D";

    @Test
    public void md5() {
        String str = encrypt.encrypt("123456");
        System.out.println(str);
        System.out.println("e10adc3949ba59abbe56e057f20f883e".equals(str));
    }
    @Test
    public void getById() {
        ResultData<AccountDto> resultData = service.getById(ID);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void create() throws IllegalAccessException {
        AccountDto dto = new AccountDto();
        dto.setUserId(UUID.randomUUID().toString());
        dto.setAccount("admin");
        dto.setName("账号测试");
        dto.setPassword(encrypt.encrypt("123456"));

        ResultData<String> resultData = service.create(dto);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void update() throws IllegalAccessException {
        ResultData<AccountDto> result = service.getById(ID);
        AccountDto dto = result.getData();
        dto.setName("账户名修改测试");
        ResultData<String> resultData = service.update(dto);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void updatePassword() {
        ResultData<AccountDto> result = service.getById(ID);
        AccountDto dto = result.getData();
        dto.setPassword("99999");
        ResultData<String> resultData = service.updatePassword(dto);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void resetPassword() {
        ResultData<AccountDto> result = service.getById(ID);
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

    @Test
    public void frozenById(){
        ResultData<String> resultData = service.frozenById(ID);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void lockedById(){
        ResultData<String> resultData = service.lockedById(ID);
        System.out.println(JsonUtils.toJson(resultData));
    }
}
