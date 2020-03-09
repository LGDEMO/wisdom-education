package com.education.admin.api.config.shiro;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

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
	@Autowired
	private ShiroCookieConfig shiroCookieConfig;

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setUnauthorizedUrl("/system/unAuth");
		shiroFilterFactoryBean.setLoginUrl("/system/unAuth");
		Map<String, String> filterChainDefinitionMap = new HashMap<>();
		// swagger-ui 配置
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		filterChainDefinitionMap.put("/MP_verify_W2WeDnp77xFcTDlk.txt", "anon");
		filterChainDefinitionMap.put("/swagger-resources", "anon");
		filterChainDefinitionMap.put("/v2/api-docs", "anon");
		filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");

		filterChainDefinitionMap.put("/doc.html", "anon"); // boostrap swagger ui
		filterChainDefinitionMap.put("/swagger-resources/**", "anon");
		filterChainDefinitionMap.put("/ueditor/exec", "anon");
		filterChainDefinitionMap.put("/uploads/**", "anon");
		filterChainDefinitionMap.put("/upload/**", "anon");
		filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/system/login", "anon");
		filterChainDefinitionMap.put("/*", "anon");

		filterChainDefinitionMap.put("/front/**", "anon");
        filterChainDefinitionMap.put("/index.html", "anon");
        filterChainDefinitionMap.put("/system/**", "authc");
        filterChainDefinitionMap.put("/api/**", "anon");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		//自定义过滤器
		Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
		shiroFilterFactoryBean.setFilters(filterMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public CacheManager ehCacheManager(net.sf.ehcache.CacheManager cacheManager) {
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManager(cacheManager);
		return ehCacheManager;
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
										   CacheManager ehCacheManager,
										   CookieRememberMeManager cookieRememberMeManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(systemRealm);
		securityManager.setSessionManager(sessionManager);
		securityManager.setCacheManager(ehCacheManager);
		securityManager.setRememberMeManager(cookieRememberMeManager);
		return securityManager;
	}


	@Bean
	public SessionDAO educationShiroSession() {
		return new EhcacheShiroSession();
	}

	@Bean
	public CookieRememberMeManager cookieRememberMeManager(SimpleCookie simpleCookie) {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(simpleCookie);
		cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
		return cookieRememberMeManager;
	}

	@Bean
	public SimpleCookie rememberMeCookie() {
		SimpleCookie simpleCookie = new SimpleCookie(shiroCookieConfig.getName());
		// 记住我cookie生效时间5天 ,单位秒;-->
		simpleCookie.setHttpOnly(true);
		simpleCookie.setMaxAge(shiroCookieConfig.getTimeOut());
		simpleCookie.setDomain(shiroCookieConfig.getDomain());
		simpleCookie.setPath(shiroCookieConfig.getPath());
		return simpleCookie;
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
