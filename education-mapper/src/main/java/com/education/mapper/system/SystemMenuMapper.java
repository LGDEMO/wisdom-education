package com.education.mapper.system;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 15:38
 */
public interface SystemMenuMapper extends BaseMapper {

    List<ModelBeanMap> findMenuByUser(Map params);

    List<ModelBeanMap> findByParentId(Integer parentId);

    List<ModelBeanMap> findByParentIdAndRoleId(Map params);
}
