package com.changhong.sei.auth.common;

import com.changhong.sei.auth.common.weixin.WeChatUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-19 14:12
 */
public class WeiXinUtilTest {
    public static void main(String[] args) throws Exception {
        String cropId = "wwdc99e9511ccac381";
        String agentId = "1000003";
        String cropSecret = "xIKMGprmIKWrK1VJ5oALdgeUAFng3DzxIpmPgT56XAA";
//        String accessToken = WeChatUtil.getAccessToken(cropId, cropSecret);
//        String accessToken = "98ClYHYM-zNyxdGw5wQm3w-gH8kBzRuFBvw1SanoGBvc1TG8MyTKqkzMAy13R6WA_hmGLnbdsQ6MlJ61Am7AQzPiREu_e-sitz3jaJuo5TKApmygn7iwzeN91WVjpBs-lYCl9tYdbq3jeENFA3W10onS8ygPYNEsVgwYHFGhE0PXSTpDNMeFZqbg7pTCEDQmuk7Jmj0eu2QP9t3BxoK9Kw";
//
//        String oauth2 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&agentid=%s&state=STATE#wechat_redirect";
//        String url = String.format(oauth2, cropId, "http://localhost:8080", agentId);
//        WeiXinUtil.httpRequest(url, "GET", null);
//        System.out.println(accessToken);

//        String code = "khS7cCw0yE6W12fJhSVMFMl5uIzRTSCoqDd8PvLFajg";
//        Map<String, Object> userMap = WeChatUtil.getUserInfo(accessToken, code);
//        System.out.println(userMap);
        // errcode -> {Integer@1922} 40029
        // errmsg -> invalid code, hint: [1587613862_52_26c4d29a45264aa4876ee1eca97c58e6], from ip: 221.10.66.183, more info at https://open.work.weixin.qq.com/devtool/query?e=40029

        String url = "http://tsei.changhong.com:8090/api-gateway/sei-auth/sso/login?authType=weChat";
        System.out.println(URLEncoder.encode(url, "UTF-8"));
    }
}
