package com.education.admin.api.config.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @descript:
 * @Auther: zengjintao
 * @Date: 2019/12/20 14:13
 * @Version:2.1.0
 */
@ConfigurationProperties(prefix = "shiro.cookie")
@Data
@Component
public class ShiroCookieConfig {
    private static final String DEFAULT_PATH = "/";
    private String name;
    private int timeOut;
    private String domain;
    private String path = DEFAULT_PATH;
    private boolean rememberMe = false;
}
