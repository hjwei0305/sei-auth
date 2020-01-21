package com.changhong.sei.auth.service;

import com.changhong.com.sei.core.test.BaseUnitTest;
import com.changhong.sei.auth.dto.AuthDto;
import com.changhong.sei.auth.dto.SessionUserDto;
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
        AuthDto dto = new AuthDto();
        dto.setTenant("10001");
        dto.setAccount("admin");
        dto.setPassword(encrypt.encrypt("123456"));
        ResultData<SessionUserDto> resultData = service.login(dto);
        System.out.println(JsonUtils.toJson(resultData));
        Assert.assertTrue(resultData.getSuccessful());
    }

}