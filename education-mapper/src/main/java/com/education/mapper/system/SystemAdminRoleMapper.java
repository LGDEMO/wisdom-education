package com.education.mapper.system;

import com.education.mapper.common.base.BaseMapper;
import com.education.mapper.common.model.ModelBeanMap;

import java.util.List;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 14:44
 */
public interface SystemAdminRoleMapper extends BaseMapper {

    List<ModelBeanMap> findRoleListByAdminId(Integer adminId);

    int deleteByAdminId(Integer adminId);
}
