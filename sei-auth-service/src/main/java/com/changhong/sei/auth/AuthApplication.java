package com.changhong.sei.auth;

import com.changhong.sei.auth.common.validatecode.IVerifyCodeGen;
import com.changhong.sei.auth.common.validatecode.SimpleCharVerifyCodeGenImpl;
import com.changhong.sei.auth.event.listener.LoginListener;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.encryption.provider.Md5EncryptProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:55
 */
@SpringBootApplication
//@EnableFeignClients(basePackages = {"com.changhong.sei.auth.service.client"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    public IEncrypt encrypt() {
        return new Md5EncryptProvider("");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IVerifyCodeGen verifyCodeGen() {
        return new SimpleCharVerifyCodeGenImpl();
    }

    @Bean
    public LoginListener loginListener() {
        return new LoginListener();
    }
}
