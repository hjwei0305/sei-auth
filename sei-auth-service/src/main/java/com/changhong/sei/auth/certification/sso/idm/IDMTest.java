package com.changhong.sei.auth.certification.sso.idm;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-05-28 13:46
 */
public class IDMTest {
    public static void main(String[] args) {
        // https://loginuatin.newhopedairy.cn/siam/oauth2.0/accessTokenByJson?client_id=AUTH_CGFSSC&client_secret=f811ba58gewqtj9i&grant_type=authorization_code&redirect_uri=http://10.200.16.10/api-gateway/sei-auth/sso/login?authType=landray&code=ST-9124-bZgcWdNO1cWk0RPOdvyd-SIAM
        // https://loginuatin.newhopedairy.cn/siam/oauth2.0/accessTokenByJson?client_id=AUTH_CGFSSC&client_secret=f811ba58gewqtj9i&grant_type=authorization_code&redirect_uri=http://10.200.16.10/api-gateway/sei-auth/sso/login?authType=idmOAuth2&code=ST-9123-mC2MhfNbD1dHDKogsyk2-SIAM
        // https://loginuatin.newhopedairy.cn/siam/oauth2.0/accessTokenByJson?client_id=AUTH_CGFSSC&client_secret=f811ba58gewqtj9i&grant_type=authorization_code&redirect_uri=http://10.200.16.10/api-gateway/sei-auth/sso/login?authType=idmOAuth2&code=ST-9126-2Rb4yz5nlbcNUdmDZEqr-SIAM
        // 检查缓存中是否存在有效token
        String redirectUri = "http://10.200.16.10/api-gateway/sei-auth/sso/login?authType=idmOAuth2";
        StringBuilder url = new StringBuilder();
        url.delete(0, url.length());
        url.append("https://loginuatin.newhopedairy.cn/siam/oauth2.0/accessTokenByJson")
                .append("?client_id=").append("AUTH_CGFSSC")
                .append("&client_secret=").append("f811ba58gewqtj9i")
                .append("&grant_type=authorization_code&redirect_uri=").append(redirectUri)
                .append("&code=").append("ST-9142-bJcnBbqfB4OuprKm17tc-SIAM");
        Map<String, String> data = IdmOAuth2Authenticator.httpRequest(url.toString(), "POST", null);
        System.out.println(data);
        if (data != null && StringUtils.equalsIgnoreCase("true", data.get("status"))) {
            // access_token：访问令牌
            String accessToken = data.get("access_token");
            if (StringUtils.isNotBlank(accessToken) && accessToken.contains("access_token=")) {
                accessToken = accessToken.replaceAll("access_token=", "");
            }
            data = IdmOAuth2Authenticator.httpRequest("https://loginuatin.newhopedairy.cn/siam/oauth2.0/profileByJson?access_token=" + accessToken, "POST", null);
            System.out.println(data);
        }
    }


    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
