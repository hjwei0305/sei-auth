package com.changhong.sei.auth;

import com.changhong.sei.auth.service.client.UserClient;
import com.changhong.sei.auth.service.client.UserInformation;
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
 * @version 1.0.00  2020-04-28 11:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationApiTest  {

    @Autowired
    private UserClient userClient;

    @Test
    public void testBasic() {
        String userId = "7d345ed4-4443-4b71-aa00-29e23c191a9e";
        ResultData<UserInformation> userInfo = userClient.getUserInformation(userId);
        System.out.println(userInfo);

        // 172.31.0.222:18004/sei-basic/user/getUserInformation?userId=7d345ed4-4443-4b71-aa00-29e23c191a9e
    }
}
