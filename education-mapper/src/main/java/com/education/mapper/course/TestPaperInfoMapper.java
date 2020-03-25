package com.education.mapper.course;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 15:59
 */
public interface TestPaperInfoMapper extends BaseMapper {

    String FIND_LIST_BY_SUBJECT_IDS = "findListBySubjectIds";


    List<ModelBeanMap> findListBySubjectIds(Map params);

}
