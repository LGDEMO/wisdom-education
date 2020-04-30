package com.education.admin.api.controller.system;

import com.education.common.model.ModelBeanMap;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.core.ApiController;
import com.education.service.system.SystemRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


/**
 * 角色管理
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/3/22 21:39
 */
@RestController
@RequestMapping("/system/role")
public class SystemRoleController extends ApiController {

    @Autowired
    private SystemRoleService systemRoleService;

    /**
     * 角色列表
     * @param params
     * @return
     */
    @GetMapping({"", "list"})
    @RequiresPermissions("system:role:list")
    public Result<ModelBeanMap> list(@RequestParam Map params) {
        return systemRoleService.pagination(params);
    }

    /**
     * 添加或修改角色
     * @param roleModelMap
     * @return
     */
    @PostMapping({"", "saveOrUpdate"})
    @RequiresPermissions({"system:role:save", "system:role:update"})
    public ResultCode saveOrUpdate(@RequestBody ModelBeanMap roleModelMap) {
        Integer id = roleModelMap.getInt("id");
        boolean updateFlag = false;
        if (ObjectUtils.isNotEmpty(id)) {
            Integer createType = roleModelMap.getInt("create_type");
            if (createType == ResultCode.SUCCESS) {
                return new ResultCode(ResultCode.FAIL, "您不能编辑系统内置角色");
            }
            updateFlag = true;
        }
        return systemRoleService.saveOrUpdate(updateFlag, roleModelMap);
    }

    /**
     * 删除角色
     * @param roleMap
     * @return
     */
    @DeleteMapping
    @RequiresPermissions("system:role:deleteById")
    public ResultCode deleteRole(@RequestBody ModelBeanMap roleMap) {
        Integer createType = roleMap.getInt("create_type");
        if (createType != null && createType == ResultCode.SUCCESS) {
            return new ResultCode(ResultCode.FAIL, "您不能删除系统内置角色");
        }
        roleMap.put("operation", "角色" + roleMap.get("name"));
        return systemRoleService.deleteById(roleMap);
    }

    /**
     * 保存角色权限标识
     * @param roleMenu
     * @return
     */
    @PostMapping("savePermission")
    @RequiresPermissions("system:role:savePermission")
    public ResultCode savePermission(@RequestBody ModelBeanMap roleMenu) {
        return systemRoleService.savePermission(roleMenu);
    }

    /**
     * 批量删除
     * @param params
     * @return
     */
    @DeleteMapping("batchDelete")
    @RequiresPermissions("system:role:batchDelete")
    public ResultCode batchDelete(@RequestBody Map params) {
        params.put("operation", "角色");
        return null;
      //  return apiService.deleteById(params);
    }
}
