package com.education.student.api.controller;

import com.education.common.base.BaseController;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.course.TestPaperInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

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


    /**
     * 获取试卷试题
     * @param params
     * @return
     */
    @GetMapping("getQuestionByPaperId")
    public Result getQuestionByPaperId(@RequestParam Map params) {
        Result result = testPaperInfoService.pagination(params);
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
