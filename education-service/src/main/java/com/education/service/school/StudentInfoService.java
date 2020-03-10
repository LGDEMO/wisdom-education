package com.education.service.school;

import com.education.common.base.BaseService;
import com.education.common.constants.Constants;
import com.education.common.constants.EnumConstants;
import com.education.common.exception.BusinessException;
import com.education.common.model.AdminUserSession;
import com.education.common.model.ModelBeanMap;
import com.education.common.model.StudentInfo;
import com.education.common.utils.ExcelKit;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.RequestUtils;
import com.education.common.utils.ResultCode;
import com.education.mapper.course.ExamInfoMapper;
import com.education.mapper.course.StudentQuestionAnswerMapper;
import com.education.mapper.course.TestPaperInfoMapper;
import com.education.mapper.school.StudentInfoMapper;
import com.education.service.course.QuestionInfoService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 15:36
 */
@Service
public class StudentInfoService extends BaseService<StudentInfoMapper> {

    @Autowired
    private QuestionInfoService questionService;
    @Autowired
    private StudentQuestionAnswerMapper studentQuestionAnswerMapper;
    @Autowired
    private ExamInfoMapper examInfoMapper;
    @Autowired
    private TestPaperInfoMapper testPaperInfoMapper;

    private static final String STUDENT_EXCEL_TITLE[] = new String[]{
            "学生姓名", "头像", "就读学校", "性别", "年龄", "年级", "家庭住址", "联系电话", "父亲姓名", "母亲姓名"
    };

    @Override
    public ResultCode deleteById(ModelBeanMap studentInfoMap) {
        try {

        } catch (Exception e) {

        }
        return super.deleteById(studentInfoMap.getInt("id"));
    }

    public ResultCode exportExcel(Map params)  {
        List<String> column = new ArrayList<>();
        column.add("name");
        column.add("head_img");
        column.add("school_name");
        column.add("sex");
        column.add("age");
        column.add("grade_type");
        column.add("address");
        column.add("mobile");
        column.add("father_name");
        column.add("mother_name");
        int width[] = {10000, 10000, 9000, 5000, 7000, 10000, 5000, 15000, 5000, 5000};
        String title = "学员信息表";
        AdminUserSession adminUserSession = getAdminUserSession();
        if (adminUserSession.isPrincipalAccount()) {
            params.put("schoolId", adminUserSession.getUserMap().get("school_id"));
        }
        List<Map> dataList = mapper.queryList(params); //sqlSessionTemplate.selectList("student.info.list", params);
        dataList.forEach(student -> {
            Integer gradeType = (Integer) student.get("grade_type");
            String gradeName = null; // BaseTask.getGradeName(gradeType);
            student.put("grade_type", gradeName);
            Integer sex = (Integer) student.get("sex");
            student.put("sex", sex == EnumConstants.Sex.MAN.getValue() ? "男" : "女");
        });
        HSSFWorkbook hssfWorkbook = ExcelKit.export(dataList, column, width, STUDENT_EXCEL_TITLE, title);
        return ExcelKit.exportByAjax("学员信息表.xls", RequestUtils.getResponse(), hssfWorkbook);
    }

    public ResultCode importStudentFromExcel(List<StudentInfo> dataList) throws Exception {

        return new ResultCode(ResultCode.SUCCESS, "数据导入失败");
    }




/*    public ResultCode deleteById(ModelBeanMap studentInfo) {
        return mapper.deleteById(studentInfo.getInt("id"));
    }*/



    @Transactional
    public ResultCode saveOrUpdate(boolean updateFlag, ModelBeanMap studentInfoMap) {
        try {
            int result = 0;
            String message = "";
            if (updateFlag) {
                message = "修改学员成功";
                result = super.update(studentInfoMap);
            } else {
                message = "添加学员成功";
                result = super.save(studentInfoMap);
            }
            if (result > 0) {
                return new ResultCode(ResultCode.FAIL, message);
            }
        } catch (Exception e) {
            logger.error("操作异常", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "操作异常"));
        }
        return new ResultCode(ResultCode.FAIL, "操作异常");
    }




    public List<ModelBeanMap> getStudentCourseOrPaperQuestionInfoList(Map params) {
        try {
            List<ModelBeanMap> userAnswerQuestionList = studentQuestionAnswerMapper.getStudentCourseOrPaperQuestionInfoList(params); //sqlSessionTemplate.selectList("user.question.answer.modeQuestion.list", params);
            int number = 1;
            for (ModelBeanMap userAnswer : userAnswerQuestionList) {
                questionService.parseQuestionOptions(userAnswer);
                int questionType = (Integer) userAnswer.get("question_type");
                if (questionType == EnumConstants.QuestionType.MULTIPLE_QUESTION.getValue()) {
                    String userAnswerStr = (String) userAnswer.get("user_answer");
                    if (ObjectUtils.isNotEmpty(userAnswerStr)) {
                        userAnswer.put("user_answer", ObjectUtils.spilt(userAnswerStr));
                    }
                }
                String questionTypeName = EnumConstants.QuestionType.getName(questionType);
                userAnswer.put("questionTypeName", questionTypeName);

                String enclosure = (String) userAnswer.get("enclosure");
                List<String> enclosureList = new ArrayList<>();
                if (ObjectUtils.isNotEmpty(enclosure)) {
                    enclosureList = Arrays.asList(ObjectUtils.spilt(enclosure));
                }
                userAnswer.put("enclosureList", enclosureList);
                userAnswer.put("content", String.valueOf(number) + "、 " + userAnswer.get("content"));
                number++;
            }
            return userAnswerQuestionList;
        } catch (Exception e) {
            logger.error("获取模式试题异常", e);
        }
        return null;
    }

    @Transactional
    public Map correctStudentQuestionAnswer(Map params) {
        try {
            Integer studentId = (Integer) params.get("studentId");
            Integer modeId = (Integer) params.get("modeId");
            Integer testPaperId = (Integer) params.get("testPaperId");
            List<Map> studentQuestionList = (List<Map>) params.get("studentQuestionList");
            Map data = new HashMap<>();
            int mark = 0;
            for (Map question : studentQuestionList) {
                Integer questionType = (Integer) question.get("question_type");
                if (questionType == EnumConstants.QuestionType.FILL_QUESTION .getValue()
                        || questionType == EnumConstants.QuestionType.CALCULATION_QUESTION.getValue()
                        || questionType == EnumConstants.QuestionType.SYNTHESIS_QUESTION.getValue()
                        || questionType == EnumConstants.QuestionType.INDEFINITE_ITEM_QUESTION.getValue()) {
                    data.put("studentId", studentId);
                    data.put("modeId", modeId);
                    data.put("questionInfoId", question.get("id"));
                    data.put("mark", question.get("answer_mark"));
                    data.put("question_points", question.get("mark"));
                    data.put("is_right", question.get("is_right"));
                    Object answerMark = question.get("answer_mark");
                    // 不知到什么情况，answer_mark 参数值有时候是 int,有时候是String, 没办法，先这样解决下
                    if (answerMark instanceof String) {
                        mark += Integer.valueOf((String)question.get("answer_mark"));
                    } else if (answerMark instanceof Integer) {
                        mark += (Integer) question.get("answer_mark");
                    }
                    data.put("comment", question.get("comment"));
                    data.put("test_paper_info_id", question.get("testPaperId"));
                    studentQuestionAnswerMapper.updateStudentQuestionMark(data);
                } else {
                    mark += (Integer) question.get("answer_mark");
                }
            }

            // 更新考试记录分数
            if (testPaperId > 0) {
                data.clear();
                data.put("testPaperId", testPaperId);
                data.put("studentId", studentId);
                Map examInfo = examInfoMapper.findByPaperId(testPaperId); //sqlSessionTemplate.selectOne("exam.info.findByPaperId", data);
                if (ObjectUtils.isNotEmpty(examInfo) && (boolean)examInfo.get("correct_flag")) {
                    params.put("code", ResultCode.FAIL);
                    params.put("message", "该试卷已批改,请勿重复提交");
                    return params;
                }

                Integer examMark = (Integer) examInfo.get("mark");
                mark += examMark;
                data.clear();
                data.put("id", examInfo.get("id"));
                data.put("mark", mark);
                data.put("correct_flag", true);
                examInfoMapper.update(data);
                // 清除错题本记录
                Map paperInfo = testPaperInfoMapper.findById(testPaperId); //sqlSessionTemplate.selectOne("test.paper.info.findById", testPaperId);
                Integer type = (Integer) paperInfo.get("type");
                if (type == EnumConstants.ExamType.ERROR_EXAM.getValue()) {
                    // Map question = studentQuestionList.get(0);
                    // Integer languagePointsId = (Integer) question.get("language_points_id");
                    if (mark >= Constants.MIN_PASS_MARK) {
                        // 清除错题
                        Integer questionInfoId = (Integer) paperInfo.get("error_question_info_id");
                       // this.clearErrorQuestionByQuestionInfoId(questionInfoId, studentId);
                        //  this.clearErrorQuestionByLanguagePointsId(languagePointsId);
                    }
                }
            }
            // 发送答题成绩分数模板消息
           /* BaseTask studyTemplateMsg = new StudyTemplateMsg(sqlSessionTemplate);
            studyTemplateMsg.put("studentId", studentId);
            studyTemplateMsg.put("modeId", modeId);
            studyTemplateMsg.put("mark", mark);
            studyTemplateMsg.put("testPaperId", testPaperId);
            studyTemplateMsg.put("templateId", Constants.STUDY_SOURCE_TEMPLATE_MESSAGE_ID);
            studyTemplateMsg.put("mark", mark);
            taskManager.execute(studyTemplateMsg);*/
            params.put("code", ResultCode.SUCCESS);
            params.put("message", "操作成功, 该学员本次共得到" + mark + "分");
            return params;
        } catch (Exception e) {
            params.clear();
            params.put("code", ResultCode.FAIL);
            params.put("message", "批改试题异常");
            logger.error("批改试题异常", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "批改试题异常"));
        }
    }
}
