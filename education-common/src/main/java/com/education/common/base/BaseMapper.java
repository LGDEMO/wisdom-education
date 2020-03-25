package com.education.common.base;

import com.education.common.model.ModelBeanMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 10:40
 */
public interface BaseMapper<T> {

    ModelBeanMap queryOne(Map params);

    ModelBeanMap findById(Integer id);

    List<ModelBeanMap> queryList(Map params);

    int save(@Param("params") Map params);

    int batchSave(Map params);

    int batchUpdate(Map params);

    int update(@Param("params") Map params);

    int batchDelete(Map params);

    int deleteById(Integer id);
}
