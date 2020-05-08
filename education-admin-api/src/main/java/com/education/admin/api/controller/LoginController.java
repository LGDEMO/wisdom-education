package com.education.admin.api.controller;

import com.education.common.annotation.Param;
import com.education.common.annotation.ParamsType;
import com.education.common.annotation.ParamsValidate;
import com.education.common.annotation.SystemLog;
import com.education.common.base.BaseController;
import com.education.common.constants.EnumConstants;
import com.education.common.model.AdminUserSession;
import com.education.common.model.JwtToken;
import com.education.common.model.ModelBeanMap;
import com.education.common.model.online.OnlineUserManager;
import com.education.common.utils.RequestUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.WebSocketMessageService;
import com.education.service.system.SystemAdminService;
import com.education.service.task.TaskManager;
import com.education.service.task.TaskParam;
import com.education.service.task.UserLoginSuccessListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
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
public class LoginController extends BaseController {

    @Autowired
    private SystemAdminService systemAdminService;
    @Autowired
    private JwtToken adminJwtToken;
    @Autowired
    private WebSocketMessageService webSocketMessageService;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private OnlineUserManager onlineUserManager;

    /**
     * 管理员登录接口
     * @param requestBody
     * @return
     */
    @PostMapping("/login")
    @SystemLog(describe = "登录管理系统")
    @ParamsValidate(params = {
        @Param(name = "userName", message = "请输入用户名"),
        @Param(name = "password", message = "请输入密码"),
        @Param(name = "key", message = "请传递一个验证码时间戳"),
        @Param(name = "password", message = "请输入密码"),
    }, paramsType = ParamsType.JSON_DATA)
    public Result login(@RequestBody ModelBeanMap requestBody, HttpSession session) {
        String loginName = requestBody.getStr("userName");
        String password = requestBody.getStr("password");
        String codeKey = requestBody.getStr("key");
        String imageCode = requestBody.getStr("code");
        String cacheCode = cacheBean.get(codeKey);
        if (!imageCode.equalsIgnoreCase(cacheCode)) {
            return Result.fail(ResultCode.FAIL, "验证码输入错误");
        }
        Result result = systemAdminService.login(loginName, password);
        if (result.isSuccess()) {
            String token = adminJwtToken.createToken(systemAdminService.getUserId(), 24 * 60 * 60 * 1000 * 5); // 默认缓存5天
            AdminUserSession userSession = systemAdminService.getAdminUserSession();
            webSocketMessageService.checkOnlineUser(userSession.getUserId(), EnumConstants.PlatformType.WEB_ADMIN);
            userSession.setSessionId(session.getId());
            systemAdminService.loadUserMenuAndPermission(userSession);
            requestBody.clear();
            // 将用户信息返回前端
            requestBody.put("token", token);
            Map userInfo = new HashMap<>();
            Integer userId = userSession.getUserId();
            userInfo.put("id", userId);
            if (userSession.isPrincipalAccount()) {
                userInfo.put("school_id", userSession.getUserMap().get("school_id"));
            }
            userInfo.put("login_name", userSession.getUserMap().get("login_name"));
            userInfo.put("permissionList", userSession.getPermissionList()); // 用户权限标识
            userInfo.put("menuList", userSession.getMenuList()); // 用户菜单集合
            userInfo.put("name", userSession.getUserMap().get("name"));
            requestBody.put("userInfo", userInfo);

            // 异步更新用户相关信息
            TaskParam taskParam = new TaskParam(UserLoginSuccessListener.class);
            taskParam.put("userSession", userSession);
            taskParam.put("request", RequestUtils.getRequest());
            taskManager.pushTask(taskParam);

            systemAdminService.updateShiroCacheUserInfo(userSession);
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
