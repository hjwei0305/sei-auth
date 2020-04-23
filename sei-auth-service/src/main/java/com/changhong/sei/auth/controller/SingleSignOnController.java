package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.common.weixin.WeiXinUtil;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 实现功能： 单点登录
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-16 14:52
 */
@Controller
@Api(value = "SingleSignOnApi", tags = "账户认证服务")
public class SingleSignOnController {

    @Autowired
    private CacheBuilder cacheBuilder;

    @ApiOperation(value = "单点登录", notes = "PC应用单点登录")
    @RequestMapping(value = {"/sso/login"})
    public Object ssoLogin(HttpServletRequest request, HttpServletResponse response) {

        //单点错误页面
        ////跳转 重定向到获取ceode请求
        //         response.sendRedirect(url)
        return "redirect:/index";
    }

    @ResponseBody
    @ApiOperation(value = "单点登录", notes = "PC应用单点登录")
    @RequestMapping(value = {"/sso/getUser"})
    public Object getUser(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        String cropId = "wwdc99e9511ccac381";
        String agentId = "1000003";
        String cropSecret = "xIKMGprmIKWrK1VJ5oALdgeUAFng3DzxIpmPgT56XAA";

        String accessToken = cacheBuilder.get("QY_WX_ACCESS_TOKEN");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = WeiXinUtil.getAccessToken(cropId, cropSecret);
            if (StringUtils.isNotBlank(accessToken)) {
                cacheBuilder.set("QY_WX_ACCESS_TOKEN", accessToken, 7000000);
            }
        }

        Map<String, Object> userMap = WeiXinUtil.getUserInfo(accessToken, code);
        System.out.println(JsonUtils.toJson(userMap));
        return userMap;
    }
}
