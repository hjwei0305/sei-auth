package com.changhong.sei.auth.common.validatecode;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 实现功能：验证码生成接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-31 15:29
 */
public interface IVerifyCodeGen {
    /**
     * 生成验证码并返回code，将图片写的os中
     *
     * @param width
     * @param height
     * @param os
     * @return
     * @throws IOException
     */
    String generate(int width, int height, OutputStream os) throws IOException;

    /**
     * 生成验证码对象
     *
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    VerifyCode generate(int width, int height) throws IOException;
}
