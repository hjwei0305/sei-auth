package com.changhong.sei.auth.config;

import com.changhong.sei.auth.config.properties.AuthProperties;
import com.changhong.sei.auth.config.properties.SsoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-30 10:37
 */
@Configuration
@EnableConfigurationProperties({AuthProperties.class, SsoProperties.class})
public class SingleSignOnConfig {


}
