package com.education.mapper.system;

import com.education.mapper.common.base.BaseMapper;
import com.education.mapper.common.model.ModelBeanMap;

import java.util.Map;


/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 12:09
 */
public interface SystemAdminMapper extends BaseMapper {

    ModelBeanMap findByLoginName(String loginName);

    int updateByUserId(Map params);

    int deleteBySchoolId(Integer schoolId);
}
