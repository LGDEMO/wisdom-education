package com.education.admin.api.controller.course;

import com.education.common.base.BaseController;
import com.education.common.model.AdminUserSession;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.Result;
import com.education.service.course.ExamInfoService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 考试管理接口
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/6/22 11:25
 */
@RestController
@RequestMapping("/system/exam")
@Api(tags = "考试管理接口")
public class ExamInfoController extends BaseController {

    @Autowired
    private ExamInfoService examInfoService;

    @GetMapping
    @RequiresPermissions("system:exam:list")
    public Result<ModelBeanMap> list(@RequestParam Map params) {
        AdminUserSession adminUserSession = examInfoService.getAdminUserSession();
        if (adminUserSession.isPrincipalAccount()) {
            params.put("schoolId", adminUserSession.getUserMap().get("school_id"));
        }
        return examInfoService.pagination(params);
    }

    @GetMapping("getStudentExamQuestionList")
    public Result<ModelBeanMap> getStudentExamQuestionList(@RequestParam Map params) {
        return null;
    }
}
