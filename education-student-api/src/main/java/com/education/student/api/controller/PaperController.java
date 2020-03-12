package com.education.student.api.controller;

import com.education.common.base.BaseController;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.mapper.course.TestPaperInfoMapper;
import com.education.mapper.course.TestPaperQuestionMapper;
import com.education.service.course.SubjectInfoService;
import com.education.service.course.TestPaperInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/11 13:54
 */
@RestController
@RequestMapping("/student/paper")
public class PaperController extends BaseController {

    @Autowired
    private TestPaperInfoService testPaperInfoService;
    @Autowired
    private SubjectInfoService subjectInfoService;

    /**
     * 获取题库模式试卷列表
     * @param params
     * @return
     */
    @GetMapping("getPaperList")
    public Result getPaperList(@RequestParam Map params) {
        Map userInfo = testPaperInfoService.getFrontUserInfo();
        Integer gradeType = (Integer) userInfo.get("grade_type");
        List<ModelBeanMap> list = subjectInfoService.findListByGradeType();
        List<Integer> subjectIds = list.stream()
                .map(item -> item.getInt("id"))
                .collect(Collectors.toList());
        params.put("gradeType", gradeType);
        params.put("subjectIds", subjectIds);
        params.put("sqlId", "mode.paper.info.list");
        params.put("studentId", userInfo.get("student_id"));
        return testPaperInfoService.pagination(params, TestPaperInfoMapper.class,
                TestPaperInfoMapper.FIND_LIST_BY_SUBJECT_IDS);
    }


    /**
     * 获取试卷试题
     * @param params
     * @return
     */
    @GetMapping("getQuestionByPaperId")
    public Result getQuestionByPaperId(@RequestParam Map params) {
        Result result = testPaperInfoService.pagination(params, TestPaperQuestionMapper.class, "getPaperQuestionList");
        Integer paperInfoId = Integer.parseInt((String)params.get("testPaperInfoId")) ;
        testPaperInfoService.parseQuestion(paperInfoId, (Map) result.getData());
        return result;
    }

    /**
     * 提交试卷接口
     * @param params
     * @return
     */
    @PostMapping("commitPaperQuestion")
    public ResultCode commitPaperQuestion (@RequestBody ModelBeanMap params) {
        return testPaperInfoService.commitPaperQuestion(params);
    }

}
