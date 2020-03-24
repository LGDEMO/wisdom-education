package com.education.mapper.system;

import com.education.mapper.common.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 21:16
 */
public interface SystemDictMapper extends BaseMapper {

    String LIST_GROUP = "listGroup";

    List<Map> listGroup(Map params);
}
