package com.changhong.sei.auth.controller;

import com.changhong.sei.apitemplate.ApiTemplate;
import com.changhong.sei.core.dto.serach.SearchFilter;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.auth.dto.*;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.exception.ServiceException;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.UUID;

public class AccountControllerTest extends BaseUnitTest {

    @Autowired
    private AccountController service;
    @Autowired
    private IEncrypt encrypt;

    private final static String ID = "62D5F24D-43F8-11EA-B9FC-CEA14F741438";

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
        resultData = service.getById(ID);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void getByTenantAccount() {
        ResultData<SessionUserResponse> resultData = service.getByTenantAccount("10044", "mac321");
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void register() {
        RegisterAccountRequest request = new RegisterAccountRequest();
        request.setUserId(UUID.randomUUID().toString());
        request.setAccount("mac321");
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
        UpdateAccountRequest request = new ModelMapper().map(result.getData(), UpdateAccountRequest.class);
        request.setName("系统管理员"); // 系统管理员
        ResultData<String> resultData = service.update(request);
        System.out.println(JsonUtils.toJson(resultData));
        result = service.getById(ID);
        System.out.println(JsonUtils.toJson(result));
    }

    @Test
    public void updatePassword() {
//        ResultData<AccountResponse> result = service.getById(ID);
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setTenant("global");
        request.setAccount("sei");
        request.setNewPassword(encrypt.encrypt("123456"));
        request.setOldPassword(encrypt.encrypt("654321"));
        ResultData<String> resultData = service.updatePassword(request);
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void resetPassword() {
        ResultData<AccountResponse> result = service.getById(ID);
        AccountResponse dto = result.getData();
        ResultData<String> resultData = service.resetPassword(dto.getTenantCode(), dto.getAccount(), "");
        System.out.println(JsonUtils.toJson(resultData));
    }

    @Test
    public void findByPage() {
        Search search = new Search();
        search.addFilter(new SearchFilter("sinceDate", "2020-06-30 12:12:34", SearchFilter.Operator.LE, "date"));

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

    @Test
    public void unbinding() {
        BindingAccountRequest request = new BindingAccountRequest();
        request.setOpenId("pan1.zhang@changhong.com");
        request.setChannel(ChannelEnum.EMAIL);
        ResultData<String> resultData = service.unbinding(request);
        System.out.println(resultData);
    }


    @Autowired
    private ApiTemplate apiTemplate;

    @Test
    public void srm() {
        HashMap result;
        try {
            result = apiTemplate.postByUrl( "https://ecmp.changhong.com/srm-sm-web/supplier/listSupplierVos?pageNo=1&pageSize=5", HashMap.class);
        } catch (Exception e) {
            throw new ServiceException("调用srm获取供应商信息接口【/supplier/listSupplierVos】报错,请联系管理员!", e);
        }
        System.out.println(result);
    }
}
