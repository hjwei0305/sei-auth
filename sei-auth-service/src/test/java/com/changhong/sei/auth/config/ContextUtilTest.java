package com.changhong.sei.auth.config;

import com.changhong.com.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * <strong>实现功能:</strong>
 * <p></p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-31 20:32
 */
public class ContextUtilTest extends BaseUnitTest {

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
    public void getSessionUser(){
        SessionUser sessionUser = ContextUtil.getSessionUser();
        Assert.assertNotNull(sessionUser);
        System.out.println(JsonUtils.toJson(sessionUser));
    }
}