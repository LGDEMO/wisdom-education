package com.education.student.api.controller;

import com.education.common.annotation.Param;
import com.education.common.annotation.ParamsType;
import com.education.common.annotation.ParamsValidate;
import com.education.common.base.BaseController;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.course.QuestionInfoService;
import com.education.service.course.SubjectInfoService;
import com.education.service.school.StudentInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.Map;

/**
 * 学员信息接口
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/11 13:52
 */
@RestController
@RequestMapping("/student")
public class StudentController extends BaseController {

    @Autowired
    private StudentInfoService studentInfoService;
    @Autowired
    private SubjectInfoService subjectInfoService;

    @GetMapping("getSubjectByGradeType")
    public Result<ModelBeanMap> getSubjectByGradeType(@RequestParam Map params) {
        params.put("grade_type", studentInfoService.getFrontUserInfo().get("grade_type"));
        return subjectInfoService.pagination(params);
    }

    /**
     * 学员登录接口
     * @param params
     * @return
     */
    @PostMapping("login")
    @ParamsValidate(params = {
        @Param(name = "userName", message = "用户名不能为空"),
        @Param(name = "password", message = "密码不能为空"),
    }, paramsType = ParamsType.JSON_DATA)
    @ApiOperation("学员登录接口")
    public Result login(@RequestBody Map params) {
        return studentInfoService.doLogin(params);
    }

    @PostMapping("logout")
    @ApiOperation("账号退出接口")
    public ResultCode logout() {
        studentInfoService.logout();
        return new ResultCode(ResultCode.SUCCESS, "退出成功");
    }


    @PostMapping("/updatePassword")
    @ParamsValidate(params = {
            @Param(name = "password", message = "请输入原始密码"),
            @Param(name = "newPassword", message = "请输入新密码"),
            @Param(name = "confirmPassword", message = "请输入确认密码"),
    }, paramsType = ParamsType.JSON_DATA)
    @ApiOperation("学员修改密码接口")
    public  ResultCode updatePassword(@RequestBody Map params) {
        return studentInfoService.resettingFrontUserPassword(params);
    }

    @GetMapping("getPaperHistory")
    public Result<ModelBeanMap> getPaperHistory(@RequestParam Map params) {
        return studentInfoService.getPaperHistory(params);
    }

    @GetMapping("getStudentErrorQuestionList")
    public Result getStudentErrorQuestionList(@RequestParam Map params) {
        return studentInfoService.getStudentErrorQuestionList(params);
    }
}
