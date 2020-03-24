package com.education.admin.api.controller.course;

import com.education.mapper.common.annotation.SystemLog;
import com.education.mapper.common.base.BaseController;
import com.education.mapper.common.model.ModelBeanMap;
import com.education.mapper.common.utils.ObjectUtils;
import com.education.mapper.common.utils.Result;
import com.education.mapper.common.utils.ResultCode;
import com.education.service.course.SubjectInfoService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;



/**
 * 科目管理
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/3/24 16:36
 */
@RestController
@RequestMapping("/system/subject")
public class SubjectController extends BaseController {

    @Autowired
    private SubjectInfoService subjectInfoService;

    @GetMapping({"", "list"})
    @RequiresPermissions("system:subject:list")
    @SystemLog(describe = "获取科目列表")
    public Result<ModelBeanMap> list(@RequestParam Map params) {
        return subjectInfoService.pagination(params);
    }

    /**
     * 添加更新科目
     * @param subjectMap
     * @return
     */
    @PostMapping("saveOrUpdate")
    @RequiresPermissions(value = {"system:subject:save", "system:subject:update"}, logical = Logical.OR)
    public ResultCode saveOrUpdate(@RequestBody ModelBeanMap subjectMap) {
        Integer id = subjectMap.getInt("id");
        boolean updateFlag = false;
        if (ObjectUtils.isNotEmpty(id)) {
            updateFlag = true;
            subjectMap.put("update_date", new Date());
        }
        return subjectInfoService.saveOrUpdate(updateFlag, subjectMap);
    }

    /**
     * 单条删除
     * @param subjectMap
     * @return
     */
    @DeleteMapping
    @RequiresPermissions("system:subject:deleteById")
    public ResultCode deleteById(@RequestBody ModelBeanMap subjectMap) {
        return subjectInfoService.deleteById(subjectMap);
    }

    /**
     * 批量删除
     * @param subjectMap
     * @return
     */
    @DeleteMapping("batchDelete")
    @RequiresPermissions("system:subject:batchDelete")
    public ResultCode batchDeleteByIds(@RequestBody ModelBeanMap subjectMap) {
        return subjectInfoService.deleteById(subjectMap);
    }
}
