/**
 * 
 */
package com.education.admin.api.config.interceptor;

import com.education.common.interceptor.BaseInterceptor;
import com.education.common.model.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户认证拦截器
 * @author zengjintao
 * @version 1.0
 * @create_at 2017年5月18日 下午2:41:05
 */
@Component
public class AuthInterceptor extends BaseInterceptor {

	@Autowired
	private JwtToken adminJwtToken;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		/* String target = getRequestUrl(request);
		if ("/".equals(target) || "/system/unAuth".equals(target))
			return true; */
		return checkToken(adminJwtToken, request, response);
	}
}
