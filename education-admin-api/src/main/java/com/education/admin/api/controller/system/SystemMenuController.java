package com.education.admin.api.controller.system;



import com.education.common.model.ModelBeanMap;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.core.ApiController;
import com.education.service.system.SystemMenuService;
import com.education.service.system.SystemRoleMenuService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


/**
 * 菜单管理
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/3/22 21:44
 */
@RestController
@RequestMapping("/system/menu")
public class SystemMenuController extends ApiController {

    @Autowired
    private SystemMenuService systemMenuService;
    @Autowired
    private SystemRoleMenuService systemRoleMenuService;

    /**
     * 获取角色拥有菜单id 集合
     * @param roleId
     * @return
     */
    @GetMapping("getMenuByRole")
    public Result<Integer> getMenuByRole(Integer roleId) {
        return Result.success(systemRoleMenuService.getMenuListByRoleId(roleId));
    }

    @GetMapping(value = "getMenuByParent")
    public Result<ModelBeanMap> getMenuByParent() {
        return Result.success(systemMenuService.treeMenu());
    }

    @GetMapping({"", "list"})
    @RequiresPermissions("system:menu:list")
    public Result<ModelBeanMap> list(@RequestParam Map params) {
        return systemMenuService.pagination(params);
    }

    @GetMapping("findById")
    public Result findById(Integer id) {
        return systemMenuService.findById(id);
    }

    @DeleteMapping
    @RequiresPermissions("system:menu:deleteById")
    public ResultCode deleteById(@RequestBody ModelBeanMap menuMap) {
        return systemMenuService.deleteById(menuMap);
    }

    @PostMapping("saveOrUpdate")
    @RequiresPermissions(value = {"system:menu:save", "system:menu:update"}, logical = Logical.OR)
    public ResultCode saveOrUpdate(@RequestBody ModelBeanMap menuMap) {
        Integer id = menuMap.getInt("id");
        boolean updateFlag = false;
        if (ObjectUtils.isNotEmpty(id)) {
            updateFlag = true;
        }
        return systemMenuService.saveOrUpdate(updateFlag, menuMap);
    }
}
