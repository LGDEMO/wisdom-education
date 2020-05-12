package com.education.admin.api.controller.course;

import com.education.common.base.BaseController;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.course.TestPaperInfoService;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 试卷管理接口
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/4/23 22:47
 */
@RestController
@RequestMapping("/system/testPaper")
public class TestPaperController extends BaseController {

    @Autowired
    private TestPaperInfoService testPaperInfoService;

    /**
     * 试卷列表
     * @param params
     * @return
     */
    @GetMapping
    @RequiresPermissions("system:testPaper:list")
    public Result<ModelBeanMap> list(@RequestParam Map params) {
        return testPaperInfoService.pagination(params);
    }

    @PostMapping("saveOrUpdate")
    @RequiresPermissions(value = {"system:testPaper:save", "system:testPaper:update"}, logical = Logical.OR)
    public Result saveOrUpdate(@ApiParam(hidden = true) @RequestBody ModelBeanMap testPaper) {
        Integer id = testPaper.getInt("id");
        boolean updateFlag = false;
        if (ObjectUtils.isNotEmpty(id)) {
            updateFlag = true;
        }
        return testPaperInfoService.saveOrUpdate(updateFlag, testPaper);
    }

    /**
     * 关联试题
     * @param params
     * @return
     */
    @PostMapping("relevanceQuestion")
    @RequiresPermissions("system:testPaper:relevanceQuestion")
    public ResultCode relevanceQuestion(@RequestBody Map params) {
        return testPaperInfoService.relevanceQuestion(params);
    }

    /**
     * 删除试卷
     * @param testPaperInfoMap
     * @return
     */
    @DeleteMapping
    @RequiresPermissions("system:testPaper:deleteById")
    public ResultCode deleteById(@RequestBody ModelBeanMap testPaperInfoMap) {
        return testPaperInfoService.deleteById(testPaperInfoMap);
    }

    /**
     * 删除关联试题
     * @param testPaperQuestionInfoMap
     * @return
     */
    @DeleteMapping("deleteQuestionForPaper")
    public ResultCode deletePaperQuestion(@RequestBody ModelBeanMap testPaperQuestionInfoMap) {
        return testPaperInfoService.deletePaperQuestion(testPaperQuestionInfoMap);
    }

    /**
     * 提交试卷接口
     * @param testPaperInfoMap
     * @return
     */
    @PostMapping("commitPaperQuestion")
    public ResultCode commitPaperQuestion (@RequestBody ModelBeanMap testPaperInfoMap) {
        return testPaperInfoService.commitPaperQuestion(testPaperInfoMap);
    }

    /**
     * 修改试卷试题分数
     * @param testPaperQuestionInfoMap
     * @return
     */
    @PostMapping("updateQuestionMark")
    public ResultCode updateQuestionMark(@RequestBody ModelBeanMap testPaperQuestionInfoMap) {
        return testPaperInfoService.updateQuestionMarkForPaper(testPaperQuestionInfoMap);
    }

    /**
     * 修改试卷试题排序
     * @param testPaperQuestionInfoMap
     * @return
     */
    @PostMapping("updateQuestionSort")
    public ResultCode updateQuestionSort(@RequestBody ModelBeanMap testPaperQuestionInfoMap) {
        return testPaperInfoService.updateQuestionSort(testPaperQuestionInfoMap);
    }

    /**
     * 试卷导出
     */
    @PostMapping("export")
    @RequiresPermissions("system:testPaper:export")
    public ResultCode export(@RequestBody ModelBeanMap testPaperInfoMap) {
        return testPaperInfoService.exportTestPaper(testPaperInfoMap);
    }

    @GetMapping("getPaperQuestionList")
    public Result getPaperQuestionList(@RequestParam Map params) {
        return testPaperInfoService.getPaperQuestionList(params);
    }
}
