package com.education.admin.api.config.shiro;

import com.education.common.cache.CacheBean;
import com.education.common.cache.RedisCacheBean;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * shiro配置
 * @author taoge
 * @version 1.0
 * @create_at 2018年11月3日下午9:07:49
 */
@Configuration
@AutoConfigureAfter(ShiroLifecycleBeanPostProcessorConfig.class)
public class ShiroConfiguration {

	private static final long INVALID_TIME = 3600 * 6 * 1000;

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setUnauthorizedUrl("/system/unAuth");
		shiroFilterFactoryBean.setLoginUrl("/system/unAuth");
		Map<String, String> filterChainDefinitionMap = new HashMap<>();
		// swagger-ui 配置
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		filterChainDefinitionMap.put("/swagger-resources", "anon");
		filterChainDefinitionMap.put("/v2/api-docs", "anon");
		filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
		filterChainDefinitionMap.put("/doc.html", "anon"); // boostrap swagger ui
		filterChainDefinitionMap.put("/swagger-resources/**", "anon");
		filterChainDefinitionMap.put("/uploads/**", "anon");
		filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/system/login", "anon");
		filterChainDefinitionMap.put("/*", "anon");
		
		// 需要认证才能访问的url
		filterChainDefinitionMap.put("/ueditor/exec", "authc");
		filterChainDefinitionMap.put("/upload/**", "authc");
		filterChainDefinitionMap.put("/dict/**", "anon");
        filterChainDefinitionMap.put("/system/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		//自定义过滤器
		Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
		shiroFilterFactoryBean.setFilters(filterMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public CacheManager redisCacheManager(CacheBean cacheBean) {
		return new RedisCacheManager(cacheBean);
	}

	@Bean
	public DefaultWebSessionManager sessionManager(SessionDAO educationShiroSession) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置session过期时间6个小时
		sessionManager.setGlobalSessionTimeout(INVALID_TIME);
        sessionManager.setSessionDAO(educationShiroSession);
		return sessionManager;
	}


	@Bean
	public SecurityManager securityManager(SessionManager sessionManager,
										   Realm systemRealm,
										   CacheManager redisCacheManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(systemRealm);
		securityManager.setSessionManager(sessionManager);
		securityManager.setCacheManager(redisCacheManager);
		return securityManager;
	}

	@Bean
	public CacheBean cacheBean(RedisTemplate redisTemplate) {
		return new RedisCacheBean(redisTemplate);
	}

	@Bean
	public SessionDAO distributeShiroSession(CacheBean cacheBean) {
		return new DistributeShiroSession(cacheBean);
	}


	/**
	 * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
	 * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证 *
	 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能 * @return
	 */
	@Bean
	@DependsOn({"lifecycleBeanPostProcessor"})
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
}
