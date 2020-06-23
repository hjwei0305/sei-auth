package com.changhong.sei.auth.certification.sso.wechat;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.Signature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-28 16:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeChatAuthenticatorTest {

    @Autowired
    private WeChatAuthenticator authenticator;

    @Test
    public void bindingAccount() {
        String json = "{\"account\":\"admin\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"reqId\":\"vision.ma\"}";
        LoginRequest request = JsonUtils.fromJson(json, LoginRequest.class);
//        ResultData<SessionUserResponse> resultData = authenticator.bindingAccount(request);
//        System.out.println(resultData);
    }

    @Test
    public void auth() {
    }

    @Test
    public void sign() {
        String signature = "";
        String str = "jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value";
        signature = Signature.sign(str);
        System.out.println("0f9de62fce790f9a083d5c99e95740ceb90c27ed".equals(signature));
        System.out.println(signature);
    }

}