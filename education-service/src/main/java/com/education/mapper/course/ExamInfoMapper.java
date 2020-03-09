package com.education.mapper.course;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 15:55
 */
public interface ExamInfoMapper extends BaseMapper {

    ModelBeanMap findByPaperId(Integer paperId);
}
