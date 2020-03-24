package com.education.mapper.student.api.controller;

import com.education.mapper.common.base.BaseController;
import com.education.mapper.common.model.ModelBeanMap;
import com.education.mapper.common.utils.Result;
import com.education.mapper.common.utils.ResultCode;
import com.education.mapper.service1.course.CourseInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/11 13:53
 */
@RequestMapping("/student/course")
@RestController
public class CourseController extends BaseController {

    @Autowired
    private CourseInfoService courseInfoService;

    @GetMapping("getCourseQuestion")
    public Result getCourseQuestion(Integer courseId) {
        return Result.success(courseInfoService.getCourseQuestion(courseId));
    }


    /**
     * 提交试题
     * @param params
     * @return
     */
    @PostMapping("commitQuestion")
    @ApiOperation("提交试题接口")
    public ResultCode commitQuestion(@RequestBody ModelBeanMap params) {
        List<ModelBeanMap> userInfoAnswerList = params.getModelBeanMapList("userInfoAnswerList");
        Integer courseId = (Integer) params.get("courseId");
        return courseInfoService.commitQuestion(courseId, userInfoAnswerList);
    }

    @GetMapping("getCourseList")
    public Result getCourse(@RequestParam Map params) {
       return courseInfoService.getCourse(params);
    }
}
