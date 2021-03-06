package com.education.mapper.course;

import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 18:46
 */
public interface StudentQuestionAnswerMapper extends BaseMapper {

    String GET_STUDENT_ANSWER_PAPER_LIST = "getStudentAnswerPaperList";
    String GET_STUDENT_ERROR_QUESTION_LIST = "getStudentErrorQuestionList";

    /**
     * 获取学员课程或试卷试题答案信息列表
     * @param params
     * @return
     */
    List<ModelBeanMap> getStudentCourseOrPaperQuestionInfoList(Map params);

    List<ModelBeanMap> getStudentAnswerPaperList(Map params);

    /**
     * 修改学员答题得分
     * @param params
     * @return
     */
    int updateStudentQuestionMark(Map params);

   // ModelBeanMap findByCourseId(Integer courseId);

    ModelBeanMap getStudentQuestionAnswerInfo(Map params);

    /**
     * 根据课程id 获取试题答案信息列表
     * @param params
     * @return
     */
    List<ModelBeanMap> getStudentCourseQuestionByCourseId(Map params);

    List<ModelBeanMap> getStudentCourseQuestionByPaperId(Map params);

    /**
     * 获取学员错题本列表
     * @param params
     * @return
     */
    List<ModelBeanMap> getStudentErrorQuestionList(Map params);
}
