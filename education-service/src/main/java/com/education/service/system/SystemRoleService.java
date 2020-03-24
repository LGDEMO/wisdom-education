package com.education.service.system;

import com.education.mapper.common.exception.BusinessException;
import com.education.mapper.common.model.ModelBeanMap;
import com.education.mapper.common.utils.ObjectUtils;
import com.education.mapper.common.utils.ResultCode;
import com.education.mapper.system.SystemRoleMapper;
import com.education.mapper.system.SystemRoleMenuMapper;
import com.education.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色service
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 11:25
 */
@Service
@Slf4j
public class SystemRoleService extends BaseService<SystemRoleMapper> {

    @Autowired
    private SystemRoleMenuMapper systemRoleMenuMapper;

    /**
     * 保存角色权限
     * @param roleMenuMap
     * @return
     */
    @Transactional
    public ResultCode savePermission(ModelBeanMap roleMenuMap) {
        try {
            systemRoleMenuMapper.deleteByRoleId(roleMenuMap.getInt("roleId")); //先删除角色原有的权限
            String permission = roleMenuMap.getStr("permission");
            if (ObjectUtils.isNotEmpty(permission)) {
                String permissions[] = permission.split(",");
                List<Map> list = new ArrayList<>();
                Integer roleId = roleMenuMap.getInt("roleId");
                for (String item : permissions) {
                    Map map = new HashMap<>();
                    map.put("menuId", item);
                    map.put("roleId", roleId);
                    list.add(map);
                }
                Map dataMap = new HashMap<>();
                dataMap.put("list", list);
                int result = systemRoleMenuMapper.batchSave(dataMap);
                if (result > 0) {
                    saveSystemLog("更改角色" + roleMenuMap.getStr("name") + "权限");
                    return new ResultCode(ResultCode.SUCCESS, "权限设置成功");
                }
                return new ResultCode(ResultCode.FAIL, "权限设置失败");
            }
            return new ResultCode(ResultCode.SUCCESS, "权限设置成功");
        } catch (Exception e) {
            log.error("权限设置异常", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "权限设置异常"));
        }
    }



}
