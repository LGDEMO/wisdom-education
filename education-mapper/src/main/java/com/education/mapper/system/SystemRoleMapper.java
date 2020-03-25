package com.education.mapper.system;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 13:52
 */
public interface SystemRoleMapper extends BaseMapper {

    ModelBeanMap findByRoleName(String roleName);
}
