package com.education.admin.api.controller.course;

import com.education.common.annotation.Param;
import com.education.common.annotation.ParamsType;
import com.education.common.annotation.ParamsValidate;
import com.education.common.annotation.SystemLog;
import com.education.common.base.BaseController;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
;
import com.education.mapper.course.CourseQuestionInfoMapper;
import com.education.service.course.CourseInfoService;
import com.education.service.course.CourseQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;

/**
 * 课程管理
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/3/31 9:59
 */
@RestController
@RequestMapping("/system/course")
@Api(tags = "课程管理接口")
public class CourseInfoController extends BaseController {

    @Autowired
    private CourseInfoService courseService;
    @Autowired
    private CourseQuestionService courseQuestionService;

    /**
     * 课程管理接口
     * @param params
     * @return
     */
    @GetMapping("list")
    @RequiresPermissions("system:course:list")
    @ApiOperation(value = "课程管理列表接口")
    @SystemLog(describe = "获取课程列表")
    public Result courseTreeList(@RequestParam Map params) {
        return courseService.pagination(params);
    }


    /**
     * 添加或修改课程
     * @param courseInfoMap
     * @return
     */
    @PostMapping
    @RequiresPermissions(value = {"system:course:save", "system:course:update"}, logical = Logical.OR)
    @ParamsValidate(params = {
        @Param(name = "name", message = "请输入课程名称"),
        @Param(name = "school_type", message = "请选择阶段"),
        @Param(name = "grade_type", message = "请选择年级"),
        @Param(name = "subject_id", message = "请选择科目")
    }, paramsType= ParamsType.JSON_DATA)
    @ApiOperation("课程添加修改接口")
    @SystemLog(describe = "添加或修改课程")
    public Result saveOrUpdate(@RequestBody ModelBeanMap courseInfoMap) {
        Integer id = courseInfoMap.getInt("id");
        boolean updateFlag = false;
        if (ObjectUtils.isNotEmpty(id)) {
            updateFlag = true;
            courseInfoMap.put("update_date", new Date());
            Integer parentId = courseInfoMap.getInt("parent_id");
            if (id == parentId) {
                return new Result(ResultCode.FAIL, "您选择的父级课程不能和填写的课程一样");
            }
        }
        return courseService.saveOrUpdate(updateFlag, courseInfoMap);
    }


    /**
     * 删除课程
     * @param modelBeanMap
     * @return
     */
    @DeleteMapping
    @RequiresPermissions("system:course:deleteById")
    @ApiOperation("删除课程接口")
    @SystemLog(describe = "删除课程")
    public ResultCode deleteById(@RequestBody ModelBeanMap modelBeanMap) {
        return courseService.deleteById(modelBeanMap);
    }

    /**
     * 修改模式试题分数或排序
     * @param courseQuestionMap
     * @return
     */
    @PostMapping("updateCourseQuestionSortOrMark")
    @ApiOperation("修改模式试题分数或排序")
    public ResultCode updateCourseQuestionSortOrMark(@RequestBody ModelBeanMap courseQuestionMap) {
        return courseService.updateCourseQuestionSortOrMark(courseQuestionMap);
    }

    @GetMapping("getCourseQuestionList")
    public Result<ModelBeanMap> getCourseQuestionList(@RequestParam Map params) {
        return courseQuestionService.pagination(params, CourseQuestionInfoMapper.class, CourseQuestionInfoMapper.GET_COURSE_QUESTION_LIST);
    }

    /**
     * 关联试题
     * @param modelBeanMap
     * @return
     */
    @PostMapping("relevanceQuestion")
    @RequiresPermissions(value = "system:mode:relevanceQuestion")
    @ApiOperation("关联试题")
    @SystemLog(describe = "关联试题")
    public ResultCode relevanceQuestion(@RequestBody ModelBeanMap modelBeanMap) {
        return courseQuestionService.relevanceQuestion(modelBeanMap);
    }
}
