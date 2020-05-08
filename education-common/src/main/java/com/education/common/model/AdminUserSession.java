package com.education.common.model;

import com.education.common.utils.ObjectUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 管理员用户信息实体类
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/3/22 22:09
 */
public class AdminUserSession implements Serializable {

    private String sessionId;
    private Map userMap; // 存储管理员信息
    private List<ModelBeanMap> roleList;
    private List<ModelBeanMap> menuList;
    private Set<String> permissionList = new HashSet<>();


    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public AdminUserSession(Map userMap) {
        this.userMap = userMap;
    }

    /**
     * 是否校长账号
     * @return
     */
    public boolean isPrincipalAccount() {
        return (boolean)this.userMap.get("principal_flag");
    }

    public void addPermission(String permission) {
        permissionList.add(permission);
    }

    public void setPermissionList(Set<String> permissionList) {
        this.permissionList = permissionList;
    }

    public Set<String> getPermissionList() {
        return permissionList;
    }

    public Integer getUserId() {
        if (ObjectUtils.isEmpty(userMap)) {
            return null;
        }
        Number n = (Number)userMap.get("id");
        return n != null ? n.intValue() : null;
    }

    /**
     * 是否超级管理员
     * @return
     */
    public boolean isSuperAdmin() {
        if (ObjectUtils.isEmpty(userMap)) {
            return false;
        }
        return (boolean)userMap.get("super_flag");
    }

    public List<ModelBeanMap> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<ModelBeanMap> roleList) {
        this.roleList = roleList;
    }

    public List<ModelBeanMap> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<ModelBeanMap> menuList) {
        this.menuList = menuList;
    }

    public Map getUserMap() {
        return userMap;
    }

    public void setUserMap(Map userMap) {
        this.userMap = userMap;
    }
}
