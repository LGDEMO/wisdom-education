/**
 * 
 */
package com.education.student.api.interceptor;

import com.education.common.interceptor.LogInterceptor;
import com.education.common.interceptor.ParamsValidateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;


/**
 * 系统配置
 * @author zengjintao
 * @version 1.0
 * @create_at 2017年9月27日下午8:03:45
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

	@Autowired
	private LogInterceptor logInterceptor;
	@Autowired
	private ParamsValidateInterceptor paramsValidateInterceptor;
	@Autowired
	private AuthInterceptor authInterceptor;

	@Value("${file.uploadPath}")
	private String uploadPath;

	//不需要拦截的url
	private static final List<String> noInterceptorUrl = new ArrayList<String>() {
		{
			add("/system/unAuth");
			add("/system/login");
			add("/api/**");
		}
	};

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor).addPathPatterns("/**");
		registry.addInterceptor(paramsValidateInterceptor).addPathPatterns("/**");
		registry.addInterceptor(authInterceptor).addPathPatterns("/student/**").excludePathPatterns("/student/login");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		//ModelBeanConfig modelBeanConfig = new ModelBeanConfig(jdbcTemplate);
		//resolvers.add(new RequestModelMapMethodArgumentResolver(modelBeanConfig, new JdkClassFactory()));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 //配置静态资源路径
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		//配置文件上传虚拟路径
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + uploadPath);
	}
}
