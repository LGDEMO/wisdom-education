package com.education.mapper.course;

import com.education.common.base.BaseMapper;

import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 15:59
 */
public interface TestPaperQuestionMapper extends BaseMapper {

    int updatePaperQuestionSort(Map params);
    int updatePaperQuestionMark(Map params);

    Integer getTestPaperInfoSum(Integer testPaperInfoId);
}
