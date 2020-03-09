package com.education.service.course;

import com.education.common.base.BaseService;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.mapper.course.CourseInfoMapper;
import com.education.mapper.course.CourseQuestionInfoMapper;
import com.education.mapper.course.SubjectInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

/*
    public List<Map> courseTreeList(Map params) {
        String sqlId = (String) params.get("sqlId");
        List<Map> data = mapper.treeList(); //sqlSessionTemplate.selectList(sqlId, params);
        return MapTreeUtils.buildTreeData(data);
    }
*/

   /* public Map getCourse(Map params) {
        Map result = apiService.paginateGetByInterceptor(params);
        List<Map> dataList = (List<Map>) result.get("data");
        if (dataList != null) {
            dataList.forEach(item -> {
               Map count = sqlSessionTemplate.selectOne("course.info.getStudyStudent", item.get("id"));
               item.put("study_number", count.get("number"));
            });
        }
        return result;
    }*/


    public Map getCourseQuestion(Integer  courseId) {
        Map params = new HashMap<>();
        params.put("courseId", String.valueOf(courseId));
       // List<ModelBeanMap> modeQuestionList = studentQuestionAnswerMapper.getStudentCourseOrPaperQuestionInfoList(params);
        List<ModelBeanMap> courseQuestionList = courseQuestionInfoMapper.getCourseQuestionList(params); //sqlSessionTemplate.selectList("mode.question.info.list", params);
        Map userAnswerMap = new HashMap<>();
        userAnswerMap.put("courseId", courseId);
        userAnswerMap.put("studentId", getFrontUserInfo().get("student_id"));
        List<ModelBeanMap> userQuestionAnswerList = courseQuestionInfoMapper.getCourseQuestionList(userAnswerMap); //sqlSessionTemplate.selectList("user.question.answer.findByQuestionIdAndModeId", userAnswerMap);
        Map userQuestionAnswerMap = questionInfoService.getQuestionUserAnswer(userQuestionAnswerList);
        Map resultMap = new HashMap<>();
        // 设置试题已提交状态
        resultMap.put("isCommit", userQuestionAnswerMap.isEmpty() ? false : true);
        questionInfoService.parserQuestion(courseQuestionList, userQuestionAnswerMap, 0, resultMap);
        resultMap.put("questionList", courseQuestionList);
        return resultMap;
    }


   /* public List<Map> getCourseByGradeType(Integer gradeType, Integer studentId, Integer subjectId) {
        Map params = new HashMap<>();
        params.put("type", "grade_type");
        params.put("code", gradeType);
        Map gradeInfo = sqlSessionTemplate.selectOne("system.dict.get", params);

        params.put("studentId", studentId);
        params.put("subjectId", subjectId);
        List<Map> studentSubjectList = sqlSessionTemplate.selectList("student.subject.info.list.findByStudentId", params);
        params.clear();
        gradeInfo.put("label", gradeInfo.get("value"));
        gradeInfo.put("isGrade", true);
        gradeInfo.put("value", gradeInfo.get("id") + "grade");
        List<Map> children = new ArrayList<>();
        for (Map subject : studentSubjectList) {
            Map subjectMap = new HashMap<>();
            subjectMap.put("label", subject.get("name"));
            subjectMap.put("value", subject.get("subject_id") + "subject");
            subjectMap.put("isSubject", true);
            params.put("gradeType", gradeType);
            params.put("subjectId", subject.get("subject_id"));
            List<Map> courseChildren = null;
            // 是否体验账号
            if (isExperienceAccount()) {
                params.put("parentId", 0);
                List<Map> parentList = sqlSessionTemplate.selectList("course.info.list.findByParentId", params);
                if (ObjectUtils.isNotEmpty(parentList)) {
                    Map parentMap = parentList.get(0);
                    params.put("parentId", parentMap.get("value"));
                    List<Map> childrenList = sqlSessionTemplate.selectList("course.info.list.findByParentId", params);
                    if (childrenList.size() > 1) {
                        childrenList.remove(1); // 只留第一章
                    }
                    parentMap.put("children", childrenList);
                    courseChildren = new ArrayList<>();
                    courseChildren.add(parentMap);
                }
            } else {
                courseChildren = findParent(new ArrayList<>(), ResultCode.FAIL, "course.info.list.findByParentId", params);
            }
            subjectMap.put("children", courseChildren);
            children.add(subjectMap);
        }
        List<Map> result = new ArrayList<>();
        // 是否体验账号，体验账号只能看第一张第一节
       *//* if (isExperienceAccount()) {
            List<Map> experienceList = new ArrayList<>();
            if (children.size() > 0) {
                experienceList.add(children.get(0));
                gradeInfo.put("children", experienceList);
            }
        } else {*//*
            gradeInfo.put("children", children);
       // }
        result.add(gradeInfo);
        return result;
    }*/

    public Map findById(Integer id) {
        Map courseMap = mapper.findById(id);
        Map params = new HashMap<>();
        params.put("gradeType", courseMap.get("grade_type"));
        int parentId = (Integer)courseMap.get("parent_id");
        List<Integer> parentIds = new ArrayList<>();
        parentIds = getParentIds(parentId, parentIds);
        Collections.reverse(parentIds); // 对集合倒序排序
       // courseMap.put("parentIdArray", parentIds);
        List<Map> subjectList = subjectInfoMapper.queryList(params); //sqlSessionTemplate.selectList("system.subject.list", params);
        courseMap.put("subjectList", subjectList);
        return courseMap;
    }

    public List<Integer> getParentIds(int parentId, List<Integer> parentIds) {
        if (parentId != ResultCode.FAIL) {
            Map parentMap = courseInfoMapper.findById(parentId); //sqlSessionTemplate.selectOne("course.info.findById", parentId);
            if (ObjectUtils.isNotEmpty(parentMap)) {
                int newParentId = (Integer)parentMap.get("parent_id");
                parentIds.add((Integer)parentMap.get("id"));
                return getParentIds(newParentId, parentIds);
            }
        }
        return parentIds;
    }

    public List<Integer> getParentId(Integer courseId) {
        Map courseMap = courseInfoMapper.findById(courseId);
        Integer parentId = (Integer) courseMap.get("parent_id");
        List<Integer> parentIds = new ArrayList<>();
        if (parentId > 0) {
            return getParentIds(parentId, parentIds);
        }
        parentIds.add(parentId);
        return parentIds;
    }

    public Result getParentCourseList() {
        try {
        } catch (Exception e) {

        }
        return null;
    }
}
