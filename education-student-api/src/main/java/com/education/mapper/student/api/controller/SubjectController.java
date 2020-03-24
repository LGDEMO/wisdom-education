package com.education.mapper.student.api.controller;

import com.education.mapper.common.base.BaseController;
import com.education.mapper.common.utils.Result;
import com.education.mapper.service1.course.SubjectInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/11 13:54
 */
@RequestMapping("/student/subject")
@RestController
public class SubjectController extends BaseController {

    @Autowired
    private SubjectInfoService subjectInfoService;

    @GetMapping("getSubjectByGradeType")
    @ApiOperation(value = "根据学员年级会员科目列表")
    public Result findListByGradeType() {
        return Result.success(subjectInfoService.findListByGradeType());
    }
}
