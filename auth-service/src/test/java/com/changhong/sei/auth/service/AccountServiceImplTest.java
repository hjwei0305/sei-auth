package com.changhong.sei.auth.service;

import com.changhong.com.sei.core.test.BaseUnitTest;
import com.changhong.sei.auth.dto.*;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

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
        ResultData<AccountResponse> resultData = service.getById(ID);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void register() {
        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setUserId(UUID.randomUUID().toString());
        request.setAccount("mac");
        request.setName("mac测试");
        request.setPassword(encrypt.encrypt("123456"));

        ResultData<String> resultData = service.register(request);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void create() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setUserId(UUID.randomUUID().toString());
        request.setAccount("admin");
        request.setName("账号测试");

        ResultData<String> resultData = service.create(request);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void update() throws IllegalAccessException {
        ResultData<AccountResponse> result = service.getById(ID);
//        UpdateAccountRequest request = (UpdateAccountRequest) result.getData();
//        ResultData<String> resultData = service.update(request);
//        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void updatePassword() {
        ResultData<AccountResponse> result = service.getById(ID);
        if (result.isSuccessful()) {
            AccountResponse response = result.getData();
            UpdatePasswordRequest request = new UpdatePasswordRequest();
            request.setTenant(response.getTenantCode());
            request.setAccount(response.getAccount());
            request.setNewPassword("123456");
            request.setOldPassword("654321");
            ResultData<String> resultData = service.updatePassword(request);
            System.out.println(JsonUtils.toJson(resultData));
        }
    }

    @Test
    public void resetPassword() {
        ResultData<AccountResponse> result = service.getById(ID);
        AccountResponse dto = result.getData();
        ResultData<String> resultData = service.resetPassword(dto.getTenantCode(), dto.getAccount());
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void findByPage() {
        Search search = new Search();
        ResultData<PageResult<AccountResponse>> resultData = service.findByPage(search);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void frozenById() {
        ResultData<String> resultData = service.frozen(ID, true);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void lockedById() {
        ResultData<String> resultData = service.locked(ID, true);
        System.out.println(JsonUtils.toJson(resultData));
    }
}
