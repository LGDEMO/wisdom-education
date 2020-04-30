package com.education.service.system;

import com.education.common.constants.EnumConstants;
import com.education.common.model.AdminUserSession;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.Md5Utils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;

import com.education.mapper.system.SystemAdminMapper;
import com.education.mapper.system.SystemAdminRoleMapper;
import com.education.mapper.system.SystemMenuMapper;
import com.education.mapper.system.SystemRoleMenuMapper;
import com.education.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 11:25
 */
@Service
@Slf4j
public class SystemAdminService extends BaseService<SystemAdminMapper> {

    @Autowired
    private SystemAdminRoleService systemAdminRoleService;
    @Autowired
    private SystemMenuMapper systemMenuMapper;
    @Autowired
    private SystemAdminRoleMapper systemAdminRoleMapper;
    @Autowired
    private SystemRoleMenuMapper systemRoleMenuMapper;


    @Override
    public Result pagination(Map params) {
        Result result = super.pagination(params);
        List<ModelBeanMap> dataList = ((ModelBeanMap)result.getData()).getModelBeanMapList("dataList");
        for (ModelBeanMap admin : dataList) {
            List<ModelBeanMap> roleList = systemAdminRoleService.findRoleListByAdminId(admin.getInt("id"));
            List<Integer> roleIds = new ArrayList<>();
            for (Map role : roleList) {
                roleIds.add((Integer)role.get("role_id"));
            }
            admin.put("roleIds", roleIds);
        }
        return result;
    }

    @Autowired
    private HttpSession session;

    public void loadPermission(AdminUserSession userSession) {
        List<ModelBeanMap> menuList = null;
        if (userSession.isSuperAdmin()) {
            menuList = systemMenuMapper.queryList(null); //sqlSessionTemplate.selectList("system.menu.list");
        } else {
            int adminId = userSession.getUserId();
            List<ModelBeanMap> roleList = getRolesByAUserId(adminId);
            userSession.setRoleList(roleList);
            List<Integer> roleIds = new ArrayList<>();//用户角色id集合
            if (ObjectUtils.isNotEmpty(roleList)) {
                for (Map roleMap : roleList) {
                    roleIds.add((Integer)roleMap.get("role_id"));
                }
            }
            menuList = systemRoleMenuMapper.getMenuByRoleIds(roleIds);  //用户菜单集合
        }
        if (ObjectUtils.isNotEmpty(menuList)) {
            for (Map menuMap : menuList) {
                String permissions = (String)menuMap.get("permissions");
                if (ObjectUtils.isNotEmpty(permissions)) {
                    userSession.addPermission(permissions);
                }
            }
        }

     /*   Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principals = subject.getPrincipals();
        //realName认证信息的key，对应的value就是认证的user对象
        String realName = principals.getRealmNames().iterator().next();
        //创建一个PrincipalCollection对象，userDO是更新后的user对象
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(userSession, realName);
        // 调用subject的runAs方法，把新的PrincipalCollection放到session里面
        subject.runAs(newPrincipalCollection); */

    }

    public int updateByUserId(Map params) {
        return mapper.updateByUserId(params);
    }

    /**
     * 获取用户角色
     * @param adminId
     * @return
     */
    public List<ModelBeanMap> getRolesByAUserId(int adminId) {
        return systemAdminRoleMapper.findRoleListByAdminId(adminId);
    }

    public Result login(String loginName, String password) {
        Result result = new Result();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
        try {
            token.setRememberMe(true);
            subject.login(token);
            result.setCode(ResultCode.SUCCESS);
            result.setMessage("登录成功");
        } catch (Exception e) {
            log.error("登录失败", e);
            result.setCode(ResultCode.FAIL);
            if (e instanceof UnknownAccountException) {
                result.setMessage("用户不存在");
            } else {
                result.setMessage("用户名或密码错误");
            }
        }
        return result;
    }

    public ResultCode updatePassword(ModelBeanMap systemAdmin) {
        try {
            String password = systemAdmin.getStr("password");
            Map userMap = mapper.findById(systemAdmin.getInt("id"));
            String encrypt = (String)userMap.get("encrypt");
            password = Md5Utils.getMd5(password,  encrypt);
            systemAdmin.put("password", password);
            int result = super.update(systemAdmin);
            if (result > 0) {
                return new ResultCode(ResultCode.SUCCESS, "密码重置成功");
            }
        } catch (Exception e) {
            log.error("密码修改失败", e);
        }
        return new ResultCode(ResultCode.FAIL, "密码重置失败");
    }

    public ResultCode resettingPassword(ModelBeanMap systemAdmin) {
        try {
            String password = systemAdmin.getStr("password");
            String newPassword = systemAdmin.getStr("newPassword");
            Map userMap = getAdminUser();
            String encrypt = (String)userMap.get("encrypt");
            password = Md5Utils.getMd5(password, encrypt);
            String userPassword = (String)userMap.get("password");
            if (!password.equals(userPassword)) {
                return new ResultCode(ResultCode.FAIL, "密码输入错误");
            }
            password = Md5Utils.getMd5(newPassword, (String)userMap.get("encrypt"));
            systemAdmin.put("password", password);
            systemAdmin.remove("newPassword");
            systemAdmin.put("id", userMap.get("id"));
            int result = this.update(systemAdmin);
            if (result > 0) {
                return new ResultCode(ResultCode.SUCCESS, "密码修改成功, 退出后请使用新密码登录");
            }
        } catch (Exception e) {
            log.error("密码修改失败", e);
        }
        return new ResultCode(ResultCode.FAIL, "密码修改失败");
    }

    /**
     * 删除单条数据
     * @param systemAdmin
     * @return
     */
    public ResultCode deleteById(ModelBeanMap systemAdmin) {
        //批量删除
        String message = "删除";
        int result = mapper.deleteById(systemAdmin.getInt("id"));
        if (result > 0) {
            return new ResultCode(ResultCode.SUCCESS, "删除成功");
        }
        return new ResultCode(ResultCode.FAIL, "删除失败");
    }

    /**
     * 添加管理员
     * @param params
     * @return
     */
    @Transactional
    public ResultCode saveOrUpdate(Map params) {
        String message = "";
        try {
            checkParams(params);
            Integer id = (Integer)params.get("id");
            List<Integer> roleIds = (List<Integer>)params.get("roleIds");
            params.remove("roleIds");
            if (ObjectUtils.isEmpty(params.get("school_id"))) {
                params.put("school_id", null);
            }
            if (ObjectUtils.isEmpty(id)) {
                String password = (String)params.get("password");
                String confirmPassword = (String)params.get("confirmPassword");
                params.remove("confirmPassword");
                if (!password.equals(confirmPassword)) {
                    return new ResultCode(ResultCode.FAIL, "密码与确认密码不一致");
                }
                String encrypt = Md5Utils.encodeSalt(Md5Utils.generatorKey());
                params.put("encrypt", encrypt);
                password = Md5Utils.getMd5(password,  encrypt);
                params.put("password", password);
                params.put("create_date", new Date());
                params.put("create_type", EnumConstants.CreateType.ADMIN_CREATE.getValue()); //管理员创建
                message = "添加";
                id = this.save(params);
            } else {
                message = "修改";
                params.put("update_date", new Date());
                this.update(params);
            }
            systemAdminRoleMapper.deleteByAdminId(id);
            if (ObjectUtils.isNotEmpty(roleIds)) {
                List<Map> adminRoleList = new ArrayList<>();
                for (Integer roleId : roleIds) {
                    Map adminRoleMap = new HashMap<>();
                    adminRoleMap.put("roleId", roleId);
                    adminRoleMap.put("adminId", id);
                    adminRoleList.add(adminRoleMap);
                }
                Map dataMap = new HashMap<>();
                dataMap.put("list", adminRoleList);
                systemAdminRoleMapper.batchSave(dataMap);
            }
            return new ResultCode(ResultCode.SUCCESS, message + "管理员成功");
        } catch (Exception e) {
            log.error(message + "管理员异常", e);
        }
        return new ResultCode(ResultCode.FAIL, "添加管理员失败");
    }

    public Result findById(Integer id) {
        try {
            Map result = new HashMap<>();
            List<ModelBeanMap> list = systemAdminRoleService.findRoleListByAdminId(id);
            List<Integer> roleIds = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(list)) {
                for (Map map : list) {
                    roleIds.add((Integer)map.get("role_id"));
                }
            }
            result.put("roleIds", roleIds);
            return Result.success(result);
        } catch (Exception e) {
            log.error("操作异常", e);
        }
        return Result.fail();
    }
}
