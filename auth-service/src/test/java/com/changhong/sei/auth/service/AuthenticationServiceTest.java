package com.changhong.sei.auth.service;

import com.changhong.com.sei.core.test.BaseUnitTest;
import com.changhong.sei.apitemplate.ApiTemplate;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sun.rmi.runtime.Log;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthenticationServiceTest extends BaseUnitTest {
    @Autowired
    private AuthenticationServiceImpl service;
    @Autowired
    private IEncrypt encrypt;

    @Autowired
    private ApiTemplate apiTemplate;

    @Test
    public void login() {
        LoginRequest request = new LoginRequest();
//        request.setTenant("10044");
//        request.setAccount("admin");
//        System.out.println("e10adc3949ba59abbe56e057f20f883e".equals(encrypt.encrypt("123456")));
//        request.setPassword("e10adc3949ba59abbe56e057f20f883e");
        request = JsonUtils.fromJson(" {\"id\":null,\"tenant\":null,\"account\":\"admin\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"locale\":\"zh_CN\"}", LoginRequest.class);

        ResultData<SessionUserResponse> resultData = service.login(request);
        System.out.println(JsonUtils.toJson(resultData));
        Assert.assertTrue(resultData.getSuccessful());
    }

    @Test
    public void loginApi(){
        String uri = "http://localhost:8080/auth/login";
        LoginRequest request = new LoginRequest();
        request.setTenant("10044");
        request.setAccount("admin");
//        System.out.println("e10adc3949ba59abbe56e057f20f883e".equals(encrypt.encrypt("123456")));
//        request.setPassword("e10adc3949ba59abbe56e057f20f883e");
//        request = JsonUtils.fromJson(" {\"id\":null,\"tenant\":null,\"account\":\"admin\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"locale\":\"zh_CN\"}", LoginRequest.class);
        ResultData result = apiTemplate.postByUrl(uri, ResultData.class, request);
        System.out.println(JsonUtils.toJson(result));
        Assert.assertTrue(result.isSuccessful());
    }

}