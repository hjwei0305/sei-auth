package com.changhong.sei.auth;

import com.changhong.sei.auth.api.AccountApi;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.test.BaseUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-05-06 16:09
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class AccountApiTest extends BaseUnitTest {
    @Autowired
    private AccountApi api;

    @Test
    public void testAccount() {
        ResultData<SessionUserResponse> resultData = api.getByTenantAccount("global", "sei");

        System.out.println(resultData);
    }
}
