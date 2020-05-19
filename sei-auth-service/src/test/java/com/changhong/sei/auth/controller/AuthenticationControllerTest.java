package com.changhong.sei.auth.controller;

import com.changhong.sei.apitemplate.ApiTemplate;
import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationControllerTest extends BaseUnitTest {
    @Autowired
    private AuthenticationController controller;
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

        ResultData<SessionUserResponse> resultData = controller.login(request);
        System.out.println(JsonUtils.toJson(resultData));
        Assert.assertTrue(resultData.successful());

        try {
            Thread.sleep(100000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginApi(){
        String uri = "http://localhost:8080/auth/login";
//        String uri = "http://10.4.208.86:20002/sei-auth/auth/login";
        LoginRequest request = new LoginRequest();
        request.setTenant("10044");
        request.setAccount("admin");
//        System.out.println("e10adc3949ba59abbe56e057f20f883e".equals(encrypt.encrypt("123456")));
        request.setPassword("e10adc3949ba59abbe56e057f20f883e");
        request.setReqId("e10adc3949ba59abbe56e057f20f883e");
//        request = JsonUtils.fromJson(" {\"id\":null,\"tenant\":null,\"account\":\"admin\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"locale\":\"zh_CN\"}", LoginRequest.class);
        ResultData result = apiTemplate.postByUrl(uri, ResultData.class, request);
        System.out.println(JsonUtils.toJson(result));
        Assert.assertTrue(result.successful());
    }

    @Test
    public void getAuthorizedFeaturesApi() {
        String uri = "http://localhost:8080/auth/getAuthorizedFeatures";
//        String uri = "http://10.4.208.86:20002/sei-auth/auth/getAuthorizedFeatures";
        Map<String, String> params = new HashMap<>();
        params.put("userId", ContextUtil.getUserId());
        ResultData result = apiTemplate.getByUrl(uri, ResultData.class, params);
        System.out.println(JsonUtils.toJson(result));
        Assert.assertTrue(result.successful());
    }
}