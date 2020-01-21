package com.changhong.sei.auth.service;

import com.changhong.com.sei.core.test.BaseUnitTest;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationServiceTest extends BaseUnitTest {
    @Autowired
    private AuthenticationServiceImpl service;
    @Autowired
    private IEncrypt encrypt;

    @Test
    public void login() {
        LoginRequest request = new LoginRequest();
        request.setTenant("10001");
        request.setAccount("admin");
        request.setPassword(encrypt.encrypt("123456"));
        ResultData<SessionUserResponse> resultData = service.login(request);
        System.out.println(JsonUtils.toJson(resultData));
        Assert.assertTrue(resultData.getSuccessful());
    }

}