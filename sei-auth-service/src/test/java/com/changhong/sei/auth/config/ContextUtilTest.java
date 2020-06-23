package com.changhong.sei.auth.config;

import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.context.mock.MockUser;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.thread.ThreadLocalHolder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <strong>实现功能:</strong>
 * <p></p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-31 20:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContextUtilTest /*extends BaseUnitTest*/ {

    @Autowired
    private CacheBuilder cacheBuilder;
    @Autowired
    private MockUser mockUser;

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
        // 初始化当前线程容器
        ThreadLocalHolder.begin();
        SessionUser sessionUser = mockUser.mockUser("10044", "admin");
        // 编写业务逻辑
        // SessionUser sessionUser = ContextUtil.getSessionUser();
        Assert.assertNotNull(sessionUser);
        System.out.println(JsonUtils.toJson(sessionUser));
        // 释放当前线程资源占用
        ThreadLocalHolder.end();
    }

    @Test
    public void cacheTest() {
        String key = "test:654321";
        String value = cacheBuilder.get(key);
        System.out.println(value);
        cacheBuilder.set(key, "123456");
        value = cacheBuilder.get(key);
        System.out.println(value);
    }
}