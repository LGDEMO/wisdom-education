package com.education.admin.api.controller;

import com.education.mapper.common.annotation.SystemLog;
import com.education.mapper.common.base.ApiController;
import com.education.mapper.common.constants.EnumConstants;
import com.education.mapper.common.model.AdminUserSession;
import com.education.mapper.common.model.JwtToken;
import com.education.mapper.common.model.ModelBeanMap;
import com.education.mapper.common.model.online.OnlineUser;
import com.education.mapper.common.model.online.OnlineUserManager;
import com.education.mapper.common.utils.IpUtils;
import com.education.mapper.common.utils.RequestUtils;
import com.education.mapper.common.utils.Result;
import com.education.mapper.common.utils.ResultCode;

import com.education.service.WebSocketMessageService;
import com.education.service.system.SystemAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 系统登录
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/3/22 22:12
 */
@RestController
@RequestMapping("/system")
@Api(value = "管理员登录登出接口", tags = "系统登录登出接口")
public class LoginController extends ApiController {

    @Autowired
    private SystemAdminService systemAdminService;
    @Autowired
    private JwtToken adminJwtToken;
    @Autowired
    private WebSocketMessageService webSocketMessageService;
    @Autowired
    private OnlineUserManager onlineUserManager;

    /**
     * 管理员登录接口
     * @param requestBody
     * @return
     */
    @PostMapping("/login")
    @SystemLog(describe = "登录管理系统")
    public Result login(@RequestBody ModelBeanMap requestBody, HttpSession session) {
        String loginName = requestBody.getStr("userName");
        String password = requestBody.getStr("password");
        Result result = systemAdminService.login(loginName, password);
        if (result.isSuccess()) {
            String token = adminJwtToken.createToken(systemAdminService.getUserId(), 24 * 60 * 60 * 1000 * 5); // 默认缓存5天
            AdminUserSession userSession = systemAdminService.getAdminUserSession();
            webSocketMessageService.checkOnlineUser(userSession.getUserId(), EnumConstants.PlatformType.WEB_ADMIN);
            systemAdminService.loadPermission(userSession);
            requestBody.remove("password");
            requestBody.put("token", token);
            Map userInfo = new HashMap<>();
            userInfo.put("id", userSession.getUserId());
            if (userSession.isPrincipalAccount()) {
                userInfo.put("school_id", systemAdminService.getAdminUser().get("school_id"));
            }
            userInfo.put("id", userSession.getUserId());
            userInfo.put("login_name", userSession.getUserMap().get("login_name"));
            requestBody.put("name", userSession.getUserMap().get("name"));
            requestBody.put("userInfo", userInfo);
            requestBody.put("userPermission", userSession.getPermissionList());

            // 更新用户登录信息
            Integer userId = userSession.getUserId();
            Map data = new HashMap<>();
            Integer count = (Integer)userSession.getUserMap().get("login_count");
            data.put("loginCount", count + 1);
            data.put("loginIp", IpUtils.getAddressIp(RequestUtils.getRequest()));
            data.put("lastLoginTime", new Date());
            data.put("userId", userId);
            systemAdminService.updateByUserId(data);
            // 添加在线用户
            String sessionId = session.getId();
            OnlineUser nowOnlineUser = new OnlineUser(userSession.getUserId(), sessionId, EnumConstants.PlatformType.WEB_ADMIN);
            nowOnlineUser.setAdminUserSession(userSession);
            onlineUserManager.addOnlineUser(userId, nowOnlineUser);
            systemAdminService.saveSystemLog(loginName + "登录系统");
       }
        result.setData(requestBody);
        return result;
    }

    /**
     * 系统退出
     * @return
     */
    @PostMapping("logout")
    @ApiOperation(value="系统退出接口", notes="用户退出接口")
    @SystemLog(describe = "退出管理系统")
    public ResultCode logout() {
        onlineUserManager.removeOnlineUser(systemAdminService.getUserId());
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ResultCode(ResultCode.SUCCESS, "退出成功");
    }

    @GetMapping("unAuth")
    public ResultCode unAuth() {
        return new ResultCode(ResultCode.UN_AUTH_ERROR_CODE, "用户未认证");
    }
}
