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
public interface TestPaperQuestionMapper extends BaseMapper {

    String GET_PAPER_QUESTION_LIST = "getPaperQuestionList";

    int updatePaperQuestionSort(Map params);
    int updatePaperQuestionMark(Map params);

    int delete(Map params);

    int deletePaperQuestion(Map params);

    List<ModelBeanMap> getPaperQuestionList(Map params);

    List<ModelBeanMap> findByTestPaperInfoId(Integer testPaperInfoId);

    Integer getTestPaperInfoSum(Integer testPaperInfoId);
}
