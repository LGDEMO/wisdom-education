package com.education.mapper.system;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 15:58
 */
public interface SystemRoleMenuMapper extends BaseMapper {

    List<ModelBeanMap> getByRoleId(Integer roleId);

    List<ModelBeanMap> getMenuByRoleIds(List<Integer> roleIds);

    int deleteByRoleId(Integer roleId);
}
