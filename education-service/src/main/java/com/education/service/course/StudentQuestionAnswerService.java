package com.education.service.course;

import com.education.mapper.common.constants.EnumConstants;
import com.education.mapper.common.model.ModelBeanMap;
import com.education.mapper.common.utils.ObjectUtils;
import com.education.mapper.common.utils.ResultCode;
import com.education.mapper.mapper.course.ExamInfoMapper;
import com.education.mapper.mapper.course.StudentQuestionAnswerMapper;
import com.education.mapper.mapper.course.TestPaperInfoMapper;
import com.education.mapper.service1.BaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 20:17
 */
@Service
public class StudentQuestionAnswerService extends BaseService<StudentQuestionAnswerMapper> {

    @Autowired
    private ExamInfoMapper examInfoMapper;
    @Autowired
    private StudentQuestionAnswerMapper studentQuestionAnswerMapper;
    @Autowired
    private TestPaperInfoMapper testPaperInfoMapper;

    @Transactional
    public String batchSaveUserAnswer(List<ModelBeanMap> questionList, Integer courseId, ModelBeanMap paperInfo) {
        int mark = 0;
        if (ObjectUtils.isNotEmpty(questionList)) {
            List<ModelBeanMap> userQuestionAnswerList = new ArrayList<>(questionList.size());
            Date now = new Date();
            Integer testPaperInfoId = ObjectUtils.isEmpty(paperInfo) ? null : (Integer) paperInfo.get("testPaperId");
            for (ModelBeanMap questionAnswer : questionList) {
                ModelBeanMap userQuestionAnswer = new ModelBeanMap();
                userQuestionAnswer.put("questionInfoId", questionAnswer.get("questionInfoId"));
                userQuestionAnswer.put("courseId", courseId);
                userQuestionAnswer.put("studentId", getFrontUserInfo().get("student_id"));
                userQuestionAnswer.put("create_date", now);
                userQuestionAnswer.put("test_paper_info_id", testPaperInfoId);
                String userAnswer = "";
                // 多选题情况
                if (questionAnswer.get("userInfoAnswer") instanceof List) {
                    List<String> userInfoAnswerArray = (List<String>)questionAnswer.get("userInfoAnswer");
                    if (ObjectUtils.isNotEmpty(userInfoAnswerArray)) {
                        for (String answer : userInfoAnswerArray) {
                            userAnswer += answer + ",";
                        }
                        userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
                    }
                } else {
                    userAnswer = (String) questionAnswer.get("userInfoAnswer");
                }
                userQuestionAnswer.put("answer", userAnswer);
                List<String> answerEnclosureList = (List<String>) questionAnswer.get("answerEnclosure");
                String answerEnclosureStr = "";
                if (ObjectUtils.isNotEmpty(answerEnclosureList)) {
                    for (String answerEnclosure : answerEnclosureList) {
                        answerEnclosureStr += answerEnclosure + ",";
                    }
                }
                userQuestionAnswer.put("enclosure", answerEnclosureStr);
                String rightAnswer = (String) questionAnswer.get("questionAnswer"); // 试题正确答案
                userQuestionAnswer.put("mark", ResultCode.FAIL);
                int questionType = (int) questionAnswer.get("questionType");
                if (questionType == EnumConstants.QuestionType.SINGLE_QUESTION.getValue()
                        || questionType == EnumConstants.QuestionType.MULTIPLE_QUESTION.getValue()
                        || questionType == EnumConstants.QuestionType.JUDGMENT_QUESTION.getValue()) {
                    if (StringUtils.isNotEmpty(userAnswer) && userAnswer.equals(rightAnswer)) {
                        userQuestionAnswer.put("correct_status", EnumConstants.CorrectStatus.RIGHT.getValue());
                        userQuestionAnswer.put("mark", questionAnswer.get("mark"));
                        mark += (Integer)questionAnswer.get("mark");
                    } else {
                        userQuestionAnswer.put("correct_status", EnumConstants.CorrectStatus.ERROR.getValue());
                    }
                }
                else {
                    userQuestionAnswer.put("correct_status", EnumConstants.CorrectStatus.CORRECT_RUNNING.getValue());
                }
                // 保存试题分数
                userQuestionAnswer.put("question_points", questionAnswer.get("mark"));
                userQuestionAnswerList.add(userQuestionAnswer);
            }
            // 保存考试记录
            String result = null;
            if (ObjectUtils.isNotEmpty(paperInfo)) {
                Map params = new HashMap<>();

                params.put("student_id", getFrontUserInfo().get("student_id"));
                params.put("create_date", now);
                params.put("test_paper_info_id", testPaperInfoId);
                params.put("subject_id", paperInfo.get("subjectId"));
                params.put("system_mark", mark); // 系统自动评分
                params.put("grade_type", getFrontUserInfo().get("grade_type"));
                paperInfo.put("mark", mark);
                examInfoMapper.save(params);


                paperInfo = testPaperInfoMapper.findById(paperInfo.getInt("testPaperId"));
               // paperInfo = sqlSessionTemplate.selectOne("test.paper.info.findById",  paperInfo.get("testPaperId"));
                int examNumber = (int) paperInfo.get("exam_number");
                paperInfo.put("exam_number", ++examNumber);
                testPaperInfoMapper.update(paperInfo);
               // super.update("test.paper.info.update", paperInfo);
                result = "本次考试系统判分" + mark;
            } else {
                result = "本次练习非选择题或判断题得分" + mark;
            }
            Map data = new HashMap();
            data.put("list", userQuestionAnswerList);
            studentQuestionAnswerMapper.batchSave(data);
            return result;
        }
        return null;
    }
}
