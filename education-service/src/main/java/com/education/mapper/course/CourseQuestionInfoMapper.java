package com.education.mapper.course;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 15:57
 */
public interface CourseQuestionInfoMapper extends BaseMapper {

    /**
     * 获取课程试题列表
     * @param params
     * @return
     */
    List<ModelBeanMap> getCourseQuestionList(Map params);

    int delete(Map params);

    // int deleteByCourseIdOrPaperId(Map params);
}
