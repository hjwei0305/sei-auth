package com.changhong.sei.auth.certification.sso.ch;

import com.auth0.jwt.JWTVerifier;
import com.changhong.sei.auth.certification.sso.ch.vo.TodoTaskRequest;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Map;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-15 08:40
 */
public class TestSSO {
    private static final Logger LOG = LoggerFactory.getLogger(TestSSO.class);

    private static final String EXP = "exp";

    private static final String PAYLOAD = "payload";


    private static String SECURITY = "123";

    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODcwMDMxMDQ4NjAsInBheWxvYWQiOiJ7XCJuYW1lXCI6XCIyMDA0NTIwM1wifSJ9.OXHBldS8Q83wo9MyHC-BWgVH7rS68-Gl6TRzXeZ3CPM";
        String account = unsign(token);
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1ODcwMDEwODI5NTMsInBheWxvYWQiOiJ7XCJ1c2VyX2lkXCI6XCIyMDA0NTIwM1wiLFwicGFnZVwiOlwiMVwiLFwicm93c1wiOlwiN1wifSJ9.CjM8gDke2C57_ipF9DqyYKKq3171PiuUXrRX-nAsLaY";
        TodoTaskRequest request = unsignTodoTaskVo(token);
        System.out.println(account);
        System.out.println(request);
    }

    /**
     * JWT解析 -- 通过密钥解析token，获取是否过期以及用户信息
     *
     * @param jwt    需解析的token参数
     */
    private static String unsign(String jwt) {
        if (StringUtils.isBlank(SECURITY)) {
            LOG.error("密钥为空！");
        }
        final JWTVerifier verifier = new JWTVerifier(SECURITY);
        try {
            //解析jwt 并转换token信息
            final Map<String, Object> claims = verifier.verify(jwt);
            //判断是否存在相应信息
            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
                //token时间
                long exp = (Long) claims.get(EXP);
                //系统当前时间
                long currentTimeMillis = System.currentTimeMillis();
                //验证token是否过期
//                if (exp > currentTimeMillis) {
                    String json = (String) claims.get(PAYLOAD);
                    JSONObject jsonObject = new JSONObject(json);
                    return jsonObject.getString("name");
//                } else {
//                    LOG.error("token过期！");
//                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 待办任务token解析 -- 通过密钥解析token，获取是否过期以及用户信息
     *
     * @param jwt 需解析的token参数
     */
    public static TodoTaskRequest unsignTodoTaskVo(String jwt) {
        if (StringUtils.isBlank(SECURITY)) {
            LOG.error("密钥为空！");
        }
        final JWTVerifier verifier = new JWTVerifier(SECURITY);
        try {
            //解析jwt 并转换token信息
            final Map<String, Object> claims = verifier.verify(jwt);
            //判断是否存在相应信息
            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
                //token时间
                long exp = (Long) claims.get(EXP);
                //系统当前时间
                long currentTimeMillis = System.currentTimeMillis();
                //验证token是否过期
//                if (exp > currentTimeMillis) {
                    String json = (String) claims.get(PAYLOAD);
                    return JsonUtils.fromJson(json, TodoTaskRequest.class);
//                } else {
//                    LOG.error("token过期！");
//                }
            }
        } catch (Exception e) {
            LOG.error("待办任务token解析异常", e);
        }
        return null;
    }
}
