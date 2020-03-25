package com.education.mapper.course;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 15:44
 */
public interface QuestionInfoMapper extends BaseMapper {

    ModelBeanMap findBySubjectId(Integer subjectId);
}
