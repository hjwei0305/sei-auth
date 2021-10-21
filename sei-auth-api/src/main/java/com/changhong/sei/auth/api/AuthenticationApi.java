package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.LoginRequest;
import com.changhong.sei.auth.dto.OAuth2Response;
import com.changhong.sei.auth.dto.SessionUserResponse;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * 实现功能：账户认证接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:13
 */
@FeignClient(name = "sei-auth")
public interface AuthenticationApi {

    /**
     * 登录
     */
    @PostMapping(path = "auth/login")
    @ApiOperation("登录")
    ResultData<SessionUserResponse> login(@RequestBody @Valid LoginRequest loginRequest);

    /**
     * 登出
     */
    @PostMapping(path = "auth/logout")
    @ApiOperation("登出")
    ResultData<String> logout(@RequestBody String sid);

    /**
     * 认证会话id
     */
    @PostMapping(path = "auth/check")
    @ApiOperation("认证会话id")
    ResultData<String> check(@RequestBody String sid);

    /**
     * 获取匿名token
     */
    @GetMapping(path = "auth/getAnonymousToken")
    @ApiOperation("获取匿名token")
    ResultData<String> getAnonymousToken();

    /**
     * 获取指定会话用户信息
     */
    @GetMapping(path = "auth/getSessionUser")
    @ApiOperation("获取指定会话用户信息")
    ResultData<SessionUserResponse> getSessionUser(@RequestParam("sid") @NotBlank String sid);

    /**
     * 通过账号签名方式认证
     *
     * @param clientId 应用标示
     * @param stamp    时间戳
     * @param account  账号
     * @param sign     签名.
     */
    @PostMapping(path = "auth/signToken/{tenantCode}")
    @ApiOperation("密码式")
    ResultData<OAuth2Response> signToken(@PathVariable(name = "tenantCode") String tenantCode,
                                         @RequestParam("clientId") String clientId,
                                         @RequestParam("account") String account,
                                         @RequestParam("timestamp") Long stamp,
                                         @RequestParam("sign") String sign,
                                         HttpServletRequest request);

    /**
     * OAuth2协议认证
     *
     * @param clientId     应用标示
     * @param redirectUri  授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * @param responseType 返回类型[授权码:code 隐藏式:token 密码式:password 客户端凭证:client_credentials],固定为code
     * @param scope        应用授权作用域
     * @param state        重定向后会带上state参数，可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
     */
    @GetMapping(path = "{tenantCode}/oauth2/authorize")
    @ApiOperation("OAuth2协议认证")
    ResultData<String> authorize(@PathVariable(name = "tenantCode") String tenantCode,
                                 @RequestParam(name = "client_id") String clientId,
                                 @RequestParam(name = "redirect_uri") String redirectUri,
                                 @RequestParam(name = "response_type") String responseType,
                                 @RequestParam(name = "scope") String scope,
                                 @RequestParam(name = "state", required = false) String state,
                                 HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 通过授权码请求token
     *
     * @param clientId     应用标示
     * @param clientSecret 应用秘钥
     * @param grantType    授权方式
     * @param code         授权码
     * @param redirectUri  授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     */
    @PostMapping(path = "{tenantCode}/oauth2/token")
    @ApiOperation("通过OAuth2授权码请求Token")
    ResultData<OAuth2Response> token(@PathVariable(name = "tenantCode") String tenantCode,
                                     @RequestParam("client_id") String clientId,
                                     @RequestParam("client_secret") String clientSecret,
                                     @RequestParam("grant_type") String grantType,
                                     @RequestParam("code") String code,
                                     @RequestParam("redirect_uri") String redirectUri,
                                     HttpServletRequest request);

    // /**
    //  * 凭证式
    //  *
    //  * @param clientId 应用标示
    //  * @param secret   应用秘钥
    //  * @param scope    应用授权作用域
    //  */
    // @GetMapping(path = "{tenantCode}/oauth2/clientToken")
    // @ApiOperation("凭证式")
    // ResultData<OAuth2Response> clientToken(@PathVariable(name = "tenantCode") String tenantCode,
    //                                        @RequestParam("clientId") String clientId,
    //                                        @RequestParam("secret") String secret,
    //                                        @RequestParam("scope") String scope);
    //
    // /**
    //  * 密码式
    //  *
    //  * @param clientId 应用标示
    //  * @param username 账号
    //  * @param secret   密码
    //  */
    // @PostMapping(path = "{tenantCode}/oauth2/userToken")
    // @ApiOperation("密码式")
    // ResultData<OAuth2Response> userToken(@PathVariable(name = "tenantCode") String tenantCode,
    //                                      @RequestParam("clientId") String clientId,
    //                                      @RequestParam("secret") String secret,
    //                                      @RequestParam("username") String username);

    // /**
    //  * 刷新token
    //  */
    // @GetMapping(path = "oauth2/refresh")
    // @ApiOperation("刷新令牌")
    // ResultData<Long> refresh();

    // /**
    //  * 确认授权接口
    //  */
    // @GetMapping(path = "oauth2/confirm")
    // @ApiOperation("确认授权接口")
    // ResultData<Void> confirm();
}
