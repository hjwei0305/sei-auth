package com.changhong.sei.auth.certification.sso.wechat;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.util.HttpUtils;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.core.util.JwtTokenUtil;
import com.changhong.sei.util.DateUtils;
import com.changhong.sei.util.Signature;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-28 16:01
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
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
    public void auth() throws Exception {
        String url;
        url = "http://182.140.244.93:81/esc-sso/oauth2.0/accessToken?client_id=f2b1766bf30b448d&client_secret=03c3e7eb794f40a2a2e41d1b0ac3dd23&grant_type=authorization_code&redirect_uri=http://182.140.244.93/api-gateway/sei-auth/sso/login?authType=oauth2&code=OC-28-FoPa2FZyhAjf5lQUabjvIBocFGDFVbBLACm";
        String postResult;
        postResult = HttpUtils.sendPost(url, "");
        System.out.println(postResult);
        url = "http://182.140.244.93:81/esc-sso/oauth2.0/profile";
        postResult = HttpUtils.sendGet(url + "?access_token=AT-19-h0mrQfixBRjuSVxycPhoYfrrKqnkKnoSVbK", "");
        System.out.println(postResult);
    }

    @Test
    public void date() {
        Date date1 = DateUtils.parseDate("2021-06-22 18:05:00", DateUtils.DEFAULT_TIME_FORMAT);
        Date date2 = DateUtils.parseDate("2021-06-22 18:00:00", DateUtils.DEFAULT_TIME_FORMAT);

        long s = date1.getTime() - date2.getTime();
        System.out.println(s);
        System.out.println(s/1000);

        long l = System.currentTimeMillis();
        Date date  = new Date(l);
        System.out.println(date);
    }

    @Test
    public void token() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        String sid = "075E44BF-D2F9-11EB-AB01-0242C0A84623";
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMDIwMTAwMCIsImxvZ2luQWNjb3VudCI6IjIwMjAxMDAwIiwiaXAiOiIxOTIuMTY4LjcwLjEiLCJ1c2VyTmFtZSI6IuaZk-S4vSIsImxvY2FsZSI6InpoX0NOIiwidXNlcklkIjoiRjc5NEE1MzgtRjdGQS0xMUVBLThGMDItMDI0MkMwQTg0NjBEIiwicmFuZG9tS2V5IjoiMDc1RTQ0QkYtRDJGOS0xMUVCLUFCMDEtMDI0MkMwQTg0NjIzIiwiYXV0aG9yaXR5UG9saWN5IjoiTm9ybWFsVXNlciIsInVzZXJUeXBlIjoiRW1wbG95ZWUiLCJleHAiOjE2MjQ0MTE2NTcsImlhdCI6MTYyNDMyNTI1NywidGVuYW50IjoiMTAwNDQiLCJhY2NvdW50IjoiMjAyMDEwMDAifQ.a1JblQOhOAdXzGjeZTW3nUjYCxjGTF2PZUPCxJBqAmY_QMblKv00y5xrn7fT-yaMiADhSFZ1V3LCm4XnSQgJow";
        Claims claims1 = jwtTokenUtil.getClaimFromToken(token);
        System.out.println(claims1);

        String newToken = jwtTokenUtil.refreshToken(token, sid);
        System.out.println(newToken);
        String s = jwtTokenUtil.getPrivateClaimFromToken(newToken, Claims.EXPIRATION);
        System.out.println(s);
        System.out.println(System.currentTimeMillis());


        Claims claims2 = jwtTokenUtil.getClaimFromToken(newToken);
        System.out.println(claims2);
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