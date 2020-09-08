package com.changhong.sei.auth.service;

import com.changhong.sei.auth.common.Constants;
import com.changhong.sei.auth.common.RandomUtils;
import com.changhong.sei.auth.common.validatecode.IVerifyCodeGen;
import com.changhong.sei.auth.common.validatecode.VerifyCode;
import com.changhong.sei.auth.dto.ChannelEnum;
import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.notify.dto.EmailAccount;
import com.changhong.sei.notify.dto.EmailMessage;
import com.changhong.sei.notify.dto.SmsMessage;
import com.changhong.sei.notify.sdk.manager.NotifyManager;
import com.changhong.sei.util.Signature;
import com.google.common.collect.Lists;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Pattern;

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
    @Autowired
    private NotifyManager notifyManager;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

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

            // 保存验证码
            saveVerifyCode(reqId, code);

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

        String cacheValue = getVerifyCode(reqId);
        if (StringUtils.isBlank(cacheValue)) {
            return ResultData.fail("验证码已过期");
        }
        if (!StringUtils.equalsIgnoreCase(value, cacheValue)) {
            return ResultData.fail("验证码不正确");
        }
        return ResultData.success("ok");
    }

    public ResultData<String> sendVerifyCode(String reqId, String target, ChannelEnum channel, String operation) {
        String code = RandomUtils.randomNumberString(6);
        LogUtil.info("验证码: {}", code);

        String subject = operation + "-验证码";
        StringBuilder content = new StringBuilder(128);
        switch (channel) {
            case EMAIL:
                if (EMAIL_PATTERN.matcher(target).matches()) {
                    EmailMessage message = new EmailMessage();
                    message.setSubject(subject);
                    content.append("尊敬的").append(ContextUtil.getUserName()).append("：<br/><br/>")
                            .append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！您申请了")
                            .append(operation).append("，验证码为：")
                            .append(code)
                            .append(", 5分钟内有效。如果您没有执行该操作，请忽略此邮件。");
                    message.setContent(content.toString());
                    message.setReceivers(Lists.newArrayList(new EmailAccount(ContextUtil.getUserName(), target)));
                    notifyManager.sendEmail(message);
                } else {
                    return ResultData.fail("邮箱格式不正确[" + target + "]");
                }
                break;
            case Mobile:
                if (target.matches("[0-9]+") && target.length() > 8 && target.length() < 14) {
                    content.append("您好！您申请了").append(operation).append(",验证码为:").append(code).append(", 5分钟内有效");
                    SmsMessage smsMessage = new SmsMessage();
                    smsMessage.setContent(content.toString());
                    smsMessage.addPhoneNum(target);
                    notifyManager.sendSms(smsMessage);
                } else {
                    return ResultData.fail("手机号不正确[" + target + "]");
                }
                break;
            default:
                return ResultData.fail("不支持的发送类型[" + channel + "]");
        }

        // 保存验证码
        saveVerifyCode(reqId, code);

        return ResultData.success();
    }

    /**
     * 将验证码写入缓存
     */
    private void saveVerifyCode(String key, String value) {
        // 验证码5分钟有效期
        cacheBuilder.set(Constants.VERIFY_CODE_KEY + Signature.sign(key), value, (long) 5 * 60 * 1000);
    }

    /**
     * 获取缓存的验证码
     */
    private String getVerifyCode(String key) {
        String cacheKey = Constants.VERIFY_CODE_KEY + Signature.sign(key);
        String value = cacheBuilder.get(cacheKey);
        // 移除已使用的验证码
        cacheBuilder.remove(cacheKey);
        return value;
    }
}
