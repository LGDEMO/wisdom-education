package com.education.service.system;

import com.education.common.base.BaseService;
import com.education.common.model.AdminUserSession;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.MapTreeUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.mapper.system.SystemMenuMapper;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 15:38
 */
@Service
public class SystemMenuService extends BaseService<SystemMenuMapper> {


    public List<ModelBeanMap> treeMenu() {
        List<ModelBeanMap> menuList = mapper.treeList();
        return MapTreeUtils.buildTreeData(menuList);
    }

    public Result findById(Integer id) {
        Map menuMap = mapper.findById(id); //
        int parentId = (Integer)menuMap.get("parent_id");
        List<Integer> parentIds = new ArrayList<>();
        parentIds = getParentIds(parentId, parentIds);
        Collections.reverse(parentIds); // 对集合倒序排序
        menuMap.put("parentArrayId", parentIds);
        return Result.success(menuMap);
    }

    private List<Integer> getParentIds(int parentId, List<Integer> parentIds) {
        if (parentId != ResultCode.FAIL) {
            Map parentMap = mapper.findById(parentId);
            if (ObjectUtils.isNotEmpty(parentMap)) {
                int newParentId = (Integer)parentMap.get("parent_id");
                parentIds.add((Integer)parentMap.get("id"));
                return getParentIds(newParentId, parentIds);
            }
        }
        return parentIds;
    }

    public List<ModelBeanMap> getMenuByUser() {
        AdminUserSession userSession = getAdminUserSession();
        List<ModelBeanMap> menuList = userSession.getMenuList();
        if (ObjectUtils.isEmpty(menuList)) {
            Integer userId = userSession.getUserId();
            Map params = new HashMap<>();
            if (userSession.isSuperAdmin()) {
                menuList = mapper.findMenuByUser(params);
            } else {
                params.put("adminId", userId);
                menuList = mapper.findMenuByUser(params);
            }
            for (Map menu : menuList) {
                Integer parentId = (Integer)menu.get("id");
                params.put("parentId", parentId);
                List<ModelBeanMap> children = mapper.findByParentIdAndRoleId(params);
                menu.put("children", children);
            }
            userSession.setMenuList(menuList);
        }
        return menuList;
    }
}
