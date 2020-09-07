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
        String json = "{\"id\":\"8f9f3a92-3f82-11e7-ac6f-005056930c6b\",\"newPassword\":\"18574234d459f052f3404ea8c7b73ea3\",\"verifyCode\":\"820486\"}";
        FindPasswordRequest request = JsonUtils.fromJson(json, FindPasswordRequest.class);
        ResultData<String> resultData = service.doFindPassword(request);
        System.out.println(resultData);
    }
}