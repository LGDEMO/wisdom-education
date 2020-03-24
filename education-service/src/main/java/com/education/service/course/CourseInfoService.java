package com.education.service.course;

import com.education.mapper.common.exception.BusinessException;
import com.education.mapper.common.model.ModelBeanMap;
import com.education.mapper.common.utils.ObjectUtils;
import com.education.mapper.common.utils.Result;
import com.education.mapper.common.utils.ResultCode;
import com.education.mapper.mapper.course.CourseInfoMapper;
import com.education.mapper.mapper.course.CourseQuestionInfoMapper;
import com.education.mapper.mapper.course.StudentQuestionAnswerMapper;
import com.education.mapper.mapper.course.SubjectInfoMapper;
import com.education.mapper.service1.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/3/31 10:06
 */
@Service
public class CourseInfoService extends BaseService<CourseInfoMapper> {

    @Autowired
    private QuestionInfoService questionInfoService;
    @Autowired
    private CourseQuestionInfoMapper courseQuestionInfoMapper;
    @Autowired
    private SubjectInfoMapper subjectInfoMapper;
    @Autowired
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private StudentQuestionAnswerMapper studentQuestionAnswerMapper;
    @Autowired
    private StudentQuestionAnswerService studentQuestionAnswerService;

    @Override
    @Transactional
    public ResultCode deleteById(Integer id) {
        try {
            Map params = new HashMap<>();
            params.put("courseId", id);
            Map result = studentQuestionAnswerMapper.getStudentQuestionAnswerInfo(params);
            if (ObjectUtils.isNotEmpty(result)) {
                return new ResultCode(ResultCode.FAIL, "课程已被使用,无法删除");
            }
            super.deleteById(id);
            courseQuestionInfoMapper.delete(params);
            return new ResultCode(ResultCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除课程异常", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "删除课程异常"));
        }
    }

    public ResultCode updateCourseQuestionSortOrMark(ModelBeanMap courseQuestionMap) {
        try {
            Integer updateType = courseQuestionMap.getInt("updateType");
            if (updateType == ResultCode.SUCCESS) { // 修改分数
                courseQuestionInfoMapper.updateCourseQuestionMark(courseQuestionMap);
                return new ResultCode(ResultCode.SUCCESS, "分数修改成功");
            } else { // 修改排序
                courseQuestionInfoMapper.updateCourseQuestionSort(courseQuestionMap);
                return new ResultCode(ResultCode.SUCCESS, "排序修改成功");
            }
        } catch (Exception e) {
            logger.error("修改课程试题分数或排序失败", e);
        }
        return new ResultCode(ResultCode.FAIL, "操作失败");
    }

    public Map getCourseQuestion(Integer  courseId) {
        Map params = new HashMap<>();
        params.put("courseId", courseId);
        List<ModelBeanMap> courseQuestionList = courseQuestionInfoMapper.getCourseQuestionList(params); //sqlSessionTemplate.selectList("mode.question.info.list", params);
        Map userAnswerMap = new HashMap<>();
        userAnswerMap.put("courseId", courseId);
        userAnswerMap.put("studentId", getFrontUserInfo().get("student_id"));
        List<ModelBeanMap> userQuestionAnswerList = courseQuestionInfoMapper.getCourseQuestionAnswerList(userAnswerMap); //sqlSessionTemplate.selectList("user.question.answer.findByQuestionIdAndModeId", userAnswerMap);
        Map userQuestionAnswerMap = questionInfoService.getQuestionUserAnswer(userQuestionAnswerList);
        Map resultMap = new HashMap<>();
        // 设置试题已提交状态
        resultMap.put("isCommit", userQuestionAnswerMap.isEmpty() ? false : true);
        questionInfoService.parserQuestion(courseQuestionList, userQuestionAnswerMap, 0, resultMap);
        resultMap.put("questionList", courseQuestionList);
        return resultMap;
    }

    public ResultCode commitQuestion(Integer courseId, List<ModelBeanMap> questionList) {
        try {
            String result = studentQuestionAnswerService.batchSaveUserAnswer(questionList, courseId, null);
            return new ResultCode(ResultCode.SUCCESS, "提交成功" + result);
        } catch (Exception e) {
            logger.error("试题提交失败", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "试题提交失败"));
        }
    }

    public Result getCourse(Map params) {
        Result<ModelBeanMap> result = super.pagination(params);
        ModelBeanMap dataMap = result.getData();
        List<ModelBeanMap> dataList = dataMap.getModelBeanMapList("dataList");
        if (ObjectUtils.isNotEmpty(dataList)) {
            dataList.forEach(item -> {
                long studyNumber = courseQuestionInfoMapper.getStudyNumberByCourse(item.getInt("id"));
                item.put("study_number", studyNumber);
            });
        }
        dataMap.put("dataList", dataList);
        return result;
    }
}
