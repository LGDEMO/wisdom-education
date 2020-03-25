package com.education.service;

import com.education.common.model.ModelBeanMap;
import com.education.common.template.BaseTemplate;
import com.education.common.template.FreeMarkerTemplate;
import com.education.common.utils.FileUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.common.utils.SpellUtils;
import com.education.mapper.course.CourseInfoMapper;
import com.education.mapper.course.CourseQuestionInfoMapper;
import com.education.service.course.QuestionInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 20:43
 */
@Service
@Slf4j
public class DocumentService {

    @Autowired
    private QuestionInfoService questionInfoService;
    @Autowired
    private CourseQuestionInfoMapper courseQuestionInfoMapper;
    @Autowired
    private CourseInfoMapper courseInfoMapper;
    private static final String COURSE_QUESTION_TEMPLATE = "modeQuestion.ftl";

    /**
     * 模式试题导出
     * @param courseIds
     * @return
     */
    public ResultCode exportCourseQuestion(List<Integer> courseIds) {
        if (ObjectUtils.isEmpty(courseIds)) {
            return new ResultCode(ResultCode.FAIL, "请选择课程");
        }
        try {
            Map params = new HashMap<>();
            Map data = new HashMap<>();
            String id = ObjectUtils.generateUuId();
            String savePath = FileUtils.getUploadPath() + "html/mode/" + id + "/";
            courseIds.forEach(courseId -> {
                params.put("courseId", courseId);
                Map modeMap = courseInfoMapper.findById(courseId); //sqlSessionTemplate.selectOne("system.mode.findById", modeId);
                List<ModelBeanMap> courseQuestionList = courseQuestionInfoMapper.getCourseQuestionList(params); //sqlSessionTemplate.selectList("mode.question.info.list", params);
                questionInfoService.parserQuestion(courseQuestionList);
                String modeName = (String) modeMap.get("name");
                String subjectName = (String) modeMap.get("subject_name");
                data.put("modeName", modeName);
                data.put("subjectName", subjectName);
                this.exportDoc(data, courseQuestionList);
                BaseTemplate template = new FreeMarkerTemplate(COURSE_QUESTION_TEMPLATE, savePath);
                String charName = SpellUtils.getSpellHeadChar(modeName);
                template.generateTemplate(data, charName + ".doc");
            });
            String returnPath = "html/mode/" + id + ".zip";
            String saveZipPath = FileUtils.getUploadPath() + returnPath;
            FileUtils.fileDirectoryToZip(savePath, saveZipPath);
            return new ResultCode(ResultCode.SUCCESS, returnPath);
        } catch (Exception e) {
            log.error("模式试题导出失败", e);
        }
        return new ResultCode(ResultCode.FAIL, "模式试题导出失败");
    }

    public void exportDocForPaper(Map params, List<ModelBeanMap> questionList) {
        int number = 1;
        for (ModelBeanMap question : questionList) {
            List<ModelBeanMap> dataList = (List<ModelBeanMap>) question.get("questionList");
            List<ModelBeanMap> list = new ArrayList<>();
            for (ModelBeanMap item : dataList) {
                String options = (String) item.get("options");
                if (ObjectUtils.isNotEmpty(options)) {
                    questionInfoService.parseQuestionOptions(item);
                }
                questionInfoService.parserQuestionContentForDoc(item, list, number, 0);
                number++;
            }
            question.put("questionList", list);
        }
        params.put("dataList", questionList);
    }

    public void exportDoc(Map params, List<ModelBeanMap> questionList) {
        List<ModelBeanMap> dataList = new ArrayList<>();
        // 处理试题内容中的富文本标签
        int number = 1;
        Integer exportType = 0;
        if (params.containsKey("exportType")) {
            exportType = (Integer) params.get("exportType");
        }
        for (ModelBeanMap question : questionList) {
            questionInfoService.parserQuestionContentForDoc(question, dataList, number, exportType);
            number++;
        }
        params.put("dataList", dataList);
    }

}
