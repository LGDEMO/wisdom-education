package com.education.mapper.course;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 15:55
 */
public interface ExamInfoMapper extends BaseMapper {

    String GET_STUDENT_LIST_BY_EXAM_ID = "getStudentListByExamId";

    ModelBeanMap findByPaperIdAndStudentId(Map params);

    List<ModelBeanMap> getStudentListByExamId(Map params);
}
