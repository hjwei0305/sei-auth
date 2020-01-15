package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dto.SessionUserDto;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.auth.api.AuthenticationService;
import com.changhong.sei.auth.dto.AuthDto;
import com.changhong.sei.core.util.JsonUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:21
 */
@Service
@Api(value = "AuthenticationService", tags = "账户认证服务")
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
//    private CacheManager cacheManager;
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 登录
     * 1.账户认证
     * ​2.产生会话
     * 3.会话写入缓存并设置有效期
     * 4.返回会话id​​
     */
    @Override
    public ResultData<SessionUserDto> login(AuthDto authDto) {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setTenantCode(authDto.getTenant());
        sessionUser.setUserId(authDto.getAccount());
        sessionUser.setAccount(authDto.getAccount());
        sessionUser.setUserName("用户名-" + authDto.getAccount());
        sessionUser.setEmail("test@sei.com");
        sessionUser.setIp("127.0.0.1");
        ContextUtil.generateToken(sessionUser);

        SessionUserDto dto = new SessionUserDto();
        dto.setAccount(sessionUser.getAccount());
        dto.setAuthorityPolicy(sessionUser.getAuthorityPolicy());
        dto.setEmail(sessionUser.getEmail());
        dto.setIp(sessionUser.getIp());
        dto.setLocale(sessionUser.getLocale());
        dto.setSessionId(sessionUser.getSessionId());
        dto.setTenantCode(sessionUser.getTenantCode());
        dto.setUserId(sessionUser.getUserId());
        dto.setUserName(sessionUser.getUserName());
        dto.setUserType(sessionUser.getUserType());

        try {
            BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps("auth:login:" + sessionUser.getSessionId());
            operations.set(sessionUser.getToken(), 36000, TimeUnit.SECONDS);
            return ResultData.success(dto);
        } catch (Exception e) {
            return ResultData.fail("登录认证异常:" + e.getMessage());
        }
    }

    /**
     * 登出
     * 清除会话id
     */
    @Override
    public ResultData<String> logout(String sid) {
        try {
            stringRedisTemplate.delete("auth:login:" + sid);
            return ResultData.success("OK");
        } catch (Exception e) {
            return ResultData.fail("登出异常:" + e.getMessage());
        }
    }

    /**
     * 认证会话id
     * 1.通过id获取会话内容(未获取到直接返回false)
     * ​2.刷新id的有效期
     * 3.返回true​
     */
    @Override
    public ResultData<String> check(String sid) {
        try {
            BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps("auth:login:" + sid);
            String token = operations.get();
            if (StringUtils.isNotBlank(token)) {
                // 续期
                operations.set(token, 36000, TimeUnit.SECONDS);
                return ResultData.success(token);
            } else {
                return ResultData.fail("认证失败");
            }
        } catch (Exception e) {
            return ResultData.fail("认证会话id异常:" + e.getMessage());
        }
    }

    /**
     * 获取匿名token
     */
    @Override
    public ResultData<String> getAnonymousToken() {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setTenantCode("anonymous");
        sessionUser.setUserId("anonymous");
        sessionUser.setAccount("anonymous");
        sessionUser.setUserName("anonymous");
        sessionUser.setEmail("anonymous");
        ContextUtil.generateToken(sessionUser);
        return ResultData.success(sessionUser.getToken());
    }
}
