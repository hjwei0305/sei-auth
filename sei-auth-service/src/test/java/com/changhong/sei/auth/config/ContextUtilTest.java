package com.changhong.sei.auth.config;

import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <strong>实现功能:</strong>
 * <p></p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-31 20:32
 */
public class ContextUtilTest extends BaseUnitTest {

    @Autowired
    private CacheBuilder cacheBuilder;

    @Test
    public void getMessage(){
        String message = ContextUtil.getMessage("core_service_00003", "tes-001");
        Assert.assertNotNull(message);
        System.out.println("core_service_00003="+message);
        message = ContextUtil.getMessage("00001");
        Assert.assertNotNull(message);
        System.out.println("00001="+message);
    }

    @Test
    public void getSessionUser() {
        SessionUser sessionUser = ContextUtil.getSessionUser();
        Assert.assertNotNull(sessionUser);
        System.out.println(JsonUtils.toJson(sessionUser));
    }

    @Test
    public void cacheTest() {
        String key = "test:123456";
        String value = cacheBuilder.get(key);
        cacheBuilder.set(key, "123456");
        value = cacheBuilder.get(key);
        System.out.println(value);
    }
}