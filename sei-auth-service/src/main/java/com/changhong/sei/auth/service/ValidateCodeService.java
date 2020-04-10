package com.changhong.sei.auth.service;

import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.common.validatecode.IVerifyCodeGen;
import com.changhong.sei.auth.common.validatecode.VerifyCode;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 实现功能：验证码
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-10 15:04
 */
@Service
public class ValidateCodeService {

    @Autowired
    private IVerifyCodeGen iVerifyCodeGen;

    @Autowired
    private CacheBuilder cacheBuilder;

    /**
     * 生成验证码
     *
     * @param reqId 验证码key
     */
    public ResultData<String> generate(String reqId) {
        try {
            //设置长宽
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();
            LogUtil.info("验证码: {}", code);

            // 验证码5分钟有效期
            cacheBuilder.set(Constants.VERIFY_CODE_KEY + reqId, code, (long) 5 * 60 * 1000);

            // 返回Base64编码过的字节数组字符串
            String str = Base64.encodeBase64String(verifyCode.getImgBytes());
            return ResultData.success("data:image/jpeg;base64," + str);
        } catch (IOException e) {
            LogUtil.error("验证码错误", e);
            return ResultData.fail("验证码错误");
        }
    }

    /**
     * 校验验证码
     *
     * @param reqId 前端上送key
     * @param value 前端上送待校验值
     * @return 是否成功
     */
    public ResultData<String> check(String reqId, String value) {
        if (StringUtils.isBlank(value)) {
            return ResultData.fail("请输入验证码");
        }

        String key = Constants.VERIFY_CODE_KEY + reqId;
        String cacheValue = cacheBuilder.get(key);
        if (StringUtils.isBlank(cacheValue)) {
            return ResultData.fail("请输入验证码");
        }
        if (!StringUtils.equalsIgnoreCase(value, cacheValue)) {
            return ResultData.fail("验证码不正确");
        }
        // 移除已使用的验证码
        cacheBuilder.remove(key);
        return ResultData.success("ok");
    }
}
