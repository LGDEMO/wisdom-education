package com.education.student.api.interceptor;

import com.education.common.interceptor.BaseInterceptor;
import com.education.common.model.FrontUserInfoSession;
import com.education.common.model.JwtToken;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.school.StudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前台网站api接口拦截器
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/5/12 15:02
 */
@Component
public class AuthInterceptor extends BaseInterceptor {

    @Autowired
    private JwtToken frontJwtToken;
    @Autowired
    private StudentInfoService studentInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        FrontUserInfoSession userInfoSession = studentInfoService.getFrontUserInfoSession();
        if (ObjectUtils.isEmpty(userInfoSession)) {
            renderJson(response, Result.fail(ResultCode.UN_AUTH_ERROR_CODE, "用户身份已过期，请重新登录"));
            return false;
        }
        return checkToken(frontJwtToken, request, response);
    }
}
