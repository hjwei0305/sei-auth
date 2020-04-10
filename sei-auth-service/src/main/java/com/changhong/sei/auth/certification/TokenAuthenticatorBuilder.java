package com.changhong.sei.auth.certification;

import com.changhong.sei.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现功能：TokenAuthenticatorBuilder
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:57
 */
@Component
public class TokenAuthenticatorBuilder {

    /**
     * TokenAuthenticator缓存
     */
    private Map<String, TokenAuthenticator> granterPool = new ConcurrentHashMap<>();

    public TokenAuthenticatorBuilder(Map<String, TokenAuthenticator> granterPool) {
        granterPool.forEach(this.granterPool::put);
    }

    /**
     * 获取TokenAuthenticator
     *
     * @param authType 认证类型
     * @return TokenAuthenticator
     */
    public TokenAuthenticator getAuthenticator(String authType) {
        TokenAuthenticator tokenGranter = granterPool.get(authType);
        if (Objects.isNull(tokenGranter)) {
            throw new ServiceException("authType 不支持，请传递正确的 authType 参数:[captcha,password]");
        } else {
            return tokenGranter;
        }
    }

}
