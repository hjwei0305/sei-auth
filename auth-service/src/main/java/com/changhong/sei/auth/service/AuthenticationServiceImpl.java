package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dto.SessionUserDto;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.auth.api.AuthenticationService;
import com.changhong.sei.auth.dto.AuthDto;
import io.swagger.annotations.Api;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
     *
     * @param authDto
     * @return
     */
    @Override
    public ResultData<SessionUserDto> login(AuthDto authDto) {
        BoundValueOperations<String, String> operations = stringRedisTemplate.boundValueOps("");
//        operations.set();
        return null;
    }

    /**
     * 登出
     * 清除会话id
     *
     * @param sid
     * @return
     */
    @Override
    public ResultData<String> logout(String sid) {
        return null;
    }

    /**
     * 登出
     * 1.通过id获取会话内容(未获取到直接返回false)
     * ​2.刷新id的有效期
     * 3.返回true​
     *
     * @param sid
     * @return
     */
    @Override
    public ResultData<SessionUserDto> check(String sid) {
        return null;
    }
}
