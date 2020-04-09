package com.education.mapper.system;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 21:16
 */
public interface SystemDictMapper extends BaseMapper {

   String GET_DICT_VALUE_LIST = "getDictValueList";

    String LIST_GROUP = "listGroup";

    List<ModelBeanMap> listGroup(Map params);

    List<ModelBeanMap> getDictValueList(Map params);
}
