package com.education.service.system;

import com.education.mapper.common.model.ModelBeanMap;
import com.education.mapper.common.utils.ObjectUtils;
import com.education.mapper.common.utils.ResultCode;
import com.education.mapper.system.SystemMenuMapper;
import com.education.mapper.system.SystemRoleMenuMapper;
import com.education.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 15:57
 */
@Service
public class SystemRoleMenuService extends BaseService<SystemRoleMenuMapper> {

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    /**
     * @param roleId
     * @return
     */
    public List<Integer> getMenuListByRoleId(Integer roleId) {
        List<ModelBeanMap> menuList = mapper.getByRoleId(roleId);
        List<Integer> ids = new ArrayList<>();
        for (ModelBeanMap menu : menuList) {
            Integer menuId = (Integer)menu.get("id");
            if (!((Integer)menu.get("parent_id") == ResultCode.FAIL)) {//是否父级菜单
                List<ModelBeanMap> list = systemMenuMapper.findByParentId(menuId);
                if (ObjectUtils.isEmpty(list)) {
                    ids.add(menuId);
                }
            }
        }
        return ids;
    }
}
