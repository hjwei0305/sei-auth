package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.test.BaseUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-08 09:01
 */
public class ValidateCodeServiceTest extends BaseUnitTest {

    @Autowired
    private ValidateCodeService service;

    @Test
    public void generate() {
        ResultData<String> resultData = service.generate("1111");
        System.out.println(resultData);
    }

    @Test
    public void check() {
        LogUtil.debug("debug");
        LogUtil.info("info");
        LogUtil.warn("warn");
        LogUtil.error("error");
        LogUtil.bizLog("bizLog");
        ResultData<String> resultData = service.check("1111", "123456");
        System.out.println(resultData);
    }

    @Test
    public void sendVerifyCode() {
        ResultData<String> resultData = service.sendVerifyCode("1111", "chao2.ma@changhong.com", "chao2.ma", ChannelEnum.EMAIL, "测试验证码");
        System.out.println(resultData);
    }
}