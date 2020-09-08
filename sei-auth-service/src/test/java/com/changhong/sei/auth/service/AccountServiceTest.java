package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dto.FindPasswordRequest;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-07 22:23
 */
public class AccountServiceTest extends BaseUnitTest {
    @Autowired
    private AccountService service;

    @Test
    public void doFindPassword() {
        String json = "{\"id\":\"62D5F24D-43F8-11EA-B9FC-CEA14F741438\",\"newPassword\":\"9be67e24a85ff2e1b2c3bfd78c6235b6\",\"verifyCode\":\"855393\"}";
        FindPasswordRequest request = JsonUtils.fromJson(json, FindPasswordRequest.class);
        ResultData<String> resultData = service.doFindPassword(request);
        System.out.println(resultData);
    }
}