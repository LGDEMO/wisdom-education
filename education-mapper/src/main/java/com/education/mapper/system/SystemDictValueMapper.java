package com.education.mapper.system;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

import java.util.List;
import java.util.Map;

public interface SystemDictValueMapper extends BaseMapper {

    List<ModelBeanMap> getDictValueByType(Map params);

    List<ModelBeanMap> getDictValueByParentId(Map params);

}
