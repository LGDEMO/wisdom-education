package com.education.service.course;

import com.education.common.base.BaseService;
import com.education.common.constants.EnumConstants;
import com.education.common.exception.BusinessException;
import com.education.common.model.ModelBeanMap;
import com.education.common.template.BaseTemplate;
import com.education.common.template.FreeMarkerTemplate;
import com.education.common.utils.*;
import com.education.mapper.course.StudentQuestionAnswerMapper;
import com.education.mapper.course.TestPaperInfoMapper;
import com.education.mapper.course.TestPaperQuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 20:00
 */
@Service
public class TestPaperInfoService extends BaseService<TestPaperInfoMapper> {

    private static final String TEST_PAPER_TEMPLATE = "temp.ftl";

   // @Autowired
  //  private ModeService modeService;
    @Autowired
    private QuestionInfoService questionInfoService;
    @Autowired
    private TestPaperQuestionMapper testPaperQuestionMapper;

    @Autowired
    private StudentQuestionAnswerMapper studentQuestionAnswerMapper;
    /**
     * 关联试题
     * @param params
     * @return
     */
    @Transactional
    public ResultCode relevanceQuestion(Map params) {
        try {
            Integer testPaperInfoId = (Integer)params.get("testPaperInfoId");
            List<Integer> questionIds = (List<Integer>)params.get("questionIds");
            List<Map> paramList = new ArrayList<>();
            Date now = new Date();
            questionIds.forEach(questionId -> {
                Map data = new HashMap<>();
                data.put("question_info_id", questionId);
                data.put("test_paper_info_id", testPaperInfoId);
                data.put("create_date", now);
                data.put("mark", 0);
                paramList.add(data);
            });
          /*  List<Integer> marks = (List<Integer>)params.get("marks");
            int markCount = 0;
            for (Integer mark : marks) {
                markCount += mark;
            }
            params.clear();
            params.put("id", testPaperInfoId);
            params.put("mark", markCount);
            this.update("test.paper.info.update", params);*/
            params.clear();
            params.put("list", paramList);
            testPaperQuestionMapper.batchSave(params);
           // this.saveSystemLog("导入试题id" + paramList.toArray());
            return new ResultCode(ResultCode.SUCCESS, paramList.size() + "道试题导入成功");
        } catch (Exception e) {
            logger.error("关联试题失败", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "试题导入失败"));
        }
    }

    public ResultCode updateQuestionSort(Map params) {
        try {
            params.put("update_date", new Date());

            testPaperQuestionMapper.updatePaperQuestionSort(params);
           // sqlSessionTemplate.update("test.paper.question.info.updateSort", params);
            return new ResultCode(ResultCode.SUCCESS, "排序修改成功");
        } catch (Exception e) {
            logger.error("排序修改失败", e);
        }
        return new ResultCode(ResultCode.FAIL, "排序修改失败");
    }


    @Transactional
    public ResultCode updateQuestionMarkForPaper(Map params) {
        try {
            params.put("update_date", new Date());
            testPaperQuestionMapper.updatePaperQuestionMark(params);
          //  sqlSessionTemplate.update("test.paper.question.info.updateMark", params);
            //获取试卷总分
            int id = (int) params.get("testPaperInfoId");
            Integer paperMark = testPaperQuestionMapper.getTestPaperInfoSum(id);
          //  Integer paperMark = sqlSessionTemplate.selectOne("test.paper.question.info.getMarkSum", id);
            params.clear();
            params.put("id", id);
            params.put("mark", paperMark);
            testPaperQuestionMapper.update(params);
            return new ResultCode(ResultCode.SUCCESS, "分数修改成功");
        } catch (Exception e) {
            logger.error("修改试题分数异常", e);
            throw new BusinessException(new ResultCode(ResultCode.SUCCESS, "修改失败"));
        }
    }


    public ResultCode deleteQuestionForPaper(Map params) {
        try {
           // sqlSessionTemplate.delete("test.paper.question.info.deleteByPaperId", params);
            return new ResultCode(ResultCode.SUCCESS, "移除试题成功");
        } catch (Exception e) {
            logger.error("移除试题失败", e);
        }
        return new ResultCode(ResultCode.FAIL, "移除试题失败");
    }

    public ResultCode exportTestPaper(ModelBeanMap requestBody) {
        try {
            List<Integer> testPaperIds = (List<Integer>) requestBody.get("testPaperIds");
            List<String> subjectNames = (List<String>) requestBody.get("subjectNames");
            Map params = new HashMap<>();
            params.put("testPaperIds", testPaperIds);
            List<Map> testPaperList = mapper.queryList(params);;
            String id = ObjectUtils.generateUuId();
            String savePath = FileUtils.getUploadPath() + "html" + File.separator + id + File.separator;
            for (int i = 0; i < testPaperList.size(); i++) {
                Map testPaper = testPaperList.get(i);
                Integer testPaperInfoId = (Integer) testPaper.get("id");
                List<ModelBeanMap> questionList = mapper.findByTestPaperInfoId(testPaperInfoId); //sqlSessionTemplate.selectList("questions.info.findByIds", testPaperInfoId);
                Set<Integer> questionTypes = questionList.stream()
                        .map(item -> (Integer)item.get("question_type"))
                        .collect(Collectors.toSet());
                Map data = new HashMap<>();
                List<Map> newQuestionList = new ArrayList<>();
                int num = 1;
                for (Integer type : questionTypes) {
                    Map questionMap = new HashMap<>();
                    questionMap.put("questionTypeName", NumberUtils.numberToChinese(num) + "、" + EnumConstants.QuestionType.getName(type));
                    List<Map> questionMapList = new ArrayList<>();
                    // 遍历所有的试题类型
                    questionList.forEach(question -> {
                        Integer value = (Integer) question.get("question_type");
                        if (type == value) {
                            questionMapList.add(question);
                        }
                    });
                    questionMap.put("questionList", questionMapList);
                    newQuestionList.add(questionMap);
                    num++;
                }

               // modeService.exportDocForPaper(data, newQuestionList);
                BaseTemplate enjoyTemplate = new FreeMarkerTemplate(TEST_PAPER_TEMPLATE, savePath);
                data.put("title", testPaper.get("name"));
                data.put("subjectName", subjectNames.get(i));
                String testPaperName = (String) testPaper.get("name");
                String charName = SpellUtils.getSpellHeadChar(testPaperName);
                enjoyTemplate.generateTemplate(data, charName + ".doc");
            }
            String returnPath = "html" + File.separator + id + ".zip";
            String saveZipPath = FileUtils.getUploadPath() + returnPath;
            FileUtils.fileDirectoryToZip(savePath, saveZipPath);
            return new ResultCode(ResultCode.SUCCESS, returnPath);
        } catch (Exception e) {
            logger.error("试卷导出失败", e);
        }
        return new ResultCode(ResultCode.FAIL, "试卷导出失败");
    }


    public void parseQuestion(Integer paperInfoId, Map dataMap) {
        List<ModelBeanMap> questionList = (List<ModelBeanMap>) dataMap.get("data");
        if (ObjectUtils.isNotEmpty(questionList)) {
            // 获取试卷试题
            Map params = new HashMap<>();
            params.put("paperInfoId", paperInfoId);
            params.put("studentId", getFrontUserInfo().get("student_id"));
            List<ModelBeanMap> userAnswerList = studentQuestionAnswerMapper.getStudentCourseQuestionByPaperId(params); //sqlSessionTemplate.selectList("user.question.answer.list.findByPaperId", params);
            // 获取用户试卷试题答案信息
            Map userQuestionAnswerMap = questionInfoService.getQuestionUserAnswer(userAnswerList);
            dataMap.put("isCommit", userQuestionAnswerMap.isEmpty() ? false : true);
            questionInfoService.parserQuestion(questionList, userQuestionAnswerMap, 0, null);
            dataMap.put("data", questionList);
        }
    }


    public ResultCode commitPaperQuestion(Map params) {
        try {
           /* List<Map> userInfoAnswerQuestionList = (List<Map>) params.get("userInfoAnswerList");
            Integer modeId = (Integer) params.get("modeId");
            String result = modeService.batchSaveUserAnswer(userInfoAnswerQuestionList, modeId, 0, params); // 保存错题记录
            Integer mark = (Integer) params.get("mark");
            return new ResultCode(ResultCode.SUCCESS, ObjectUtils.isNotEmpty(result) ?
                    "提交成功,你本次考试非主观题成绩为" + mark + "分," + result
                    : "提交成功,你本次考试非主观题成绩为" + mark + "分");*/
        } catch (Exception e) {
            logger.error("提交考试信息异常,", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "提交失败, 请稍后再试"));
        }
        return null;
    }
}
