package com.education.admin.api.config.shiro;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 由于LifecycleBeanPostProcessor用于在实现了Initializable接口的Shiro bean初始化时调用Initializable接口回调，
 * 在实现了Destroyable接口的Shiro bean销毁时调用 Destroyable接口回调。
 * 因此造成在ShiroConfiguration 无法@Autowired 注入bean实例
 * @descript:
 * @Auther: zengjintao
 * @Date: 2019/12/20 14:33
 * @Version:2.1.0
 */
@Configuration
public class ShiroLifecycleBeanPostProcessorConfig {

    /**
     * Shiro生命周期处理器
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
