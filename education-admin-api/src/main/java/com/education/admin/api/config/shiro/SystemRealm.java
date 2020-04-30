package com.education.admin.api.config.shiro;


import com.education.common.component.SpringBeanManager;
import com.education.common.exception.BusinessException;
import com.education.common.model.AdminUserSession;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.Md5Utils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.mapper.system.SystemAdminMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

/**
 * 管理员登录认证器
 */
@Component
public class SystemRealm extends AuthorizingRealm {


	/**
	 * 用户授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		AdminUserSession adminSession = (AdminUserSession)principals.getPrimaryPrincipal();
		if (ObjectUtils.isNotEmpty(adminSession)) {
			return this.loadPermission(adminSession);
		}
		return null;
	}

	/**
	 * 获取用户权限
	 * @param adminSession
	 * @return
	 */
	private AuthorizationInfo loadPermission(AdminUserSession adminSession) {
		if (ObjectUtils.isNotEmpty(adminSession)) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			info.addStringPermissions(adminSession.getPermissionList());
			return info;
		}
		return null;
	}

	/**
	 * 用户登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
		String userName = usernamePasswordToken.getUsername();
		ModelBeanMap systemAdmin = getSystemAdminMapper().findByLoginName(userName);
		if (ObjectUtils.isEmpty(systemAdmin)) {
			throw new UnknownAccountException("用户不存在");
		} else if ((boolean)systemAdmin.get("disabled_flag")) {
			throw new BusinessException(new ResultCode(ResultCode.FAIL, "此账号已被禁用"));
		}
		String password = Md5Utils.getMd5(new String(usernamePasswordToken.getPassword()), systemAdmin.getStr("encrypt"));
		usernamePasswordToken.setPassword(password.toCharArray());
		//以下数据属于数据库中的用户名密
		return new SimpleAuthenticationInfo(new AdminUserSession(systemAdmin), systemAdmin.getStr("password"), getName());
	}

	private SystemAdminMapper getSystemAdminMapper() {
		return SpringBeanManager.getBean(SystemAdminMapper.class);
	}
}
