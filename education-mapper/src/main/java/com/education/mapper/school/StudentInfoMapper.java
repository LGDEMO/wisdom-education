package com.education.mapper.school;

import com.education.mapper.common.base.BaseMapper;
import com.education.mapper.common.model.ModelBeanMap;

import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 15:37
 */
public interface StudentInfoMapper extends BaseMapper {


    int deleteByIdOrSchoolId(Map params);

    ModelBeanMap findByLoginName(String loginName);
}
