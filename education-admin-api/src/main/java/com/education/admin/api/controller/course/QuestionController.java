package com.education.admin.api.controller.course;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.education.common.annotation.Param;
import com.education.common.annotation.ParamsType;
import com.education.common.annotation.ParamsValidate;
import com.education.common.base.BaseController;
import com.education.common.model.ModelBeanMap;
import com.education.common.model.QuestionInfo;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.course.QuestionInfoService;
import com.jfinal.i18n.Res;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 试题管理
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/3/24 20:03
 */
@RestController
@Slf4j
@RequestMapping("/system/question")
public class QuestionController extends BaseController {

    @Autowired
    private QuestionInfoService questionInfoService;

    /**
     * 实体列表
     * @param params
     * @return
     */
    @GetMapping({"", "list"})
    @RequiresPermissions("system:question:list")
    public Result list(@RequestParam Map params) {
        return questionInfoService.pagination(params);
    }


    /**
     * 试题详情
     * @param id
     * @return
     */
    @GetMapping("findById")
    public Result findById(Integer id) {
        return Result.success(questionInfoService.findById(id));
    }

    /**
     * 添加修改试题
     * @param questionInfoMap
     * @return
     */
    @PostMapping
    @ParamsValidate(params = {
        @Param(name = "school_type", message = "请选择阶段"),
        @Param(name = "grade_type", message = "请选择年级"),
        @Param(name = "subject_id", message = "请选择科目"),
        @Param(name = "question_type", message = "请选择试题类型")
    }, paramsType = ParamsType.JSON_DATA)
    @RequiresPermissions(value = {"system:question:save", "system:question:update"}, logical = Logical.OR)
    public Result saveOrUpdate(@RequestBody ModelBeanMap questionInfoMap) {
        return questionInfoService.saveOrUpdate(questionInfoMap);
    }

    /**
     * 删除试题
     * @param questionInfoMap
     * @return
     */
    @DeleteMapping
    @RequiresPermissions("system:question:deleteById")
    public ResultCode deleteById(@RequestBody ModelBeanMap questionInfoMap) {
        return questionInfoService.deleteById(questionInfoMap);
    }

    /**
     * excel导入
     * @param file
     */
    @RequestMapping(value = "uploadExcel", method = {RequestMethod.GET, RequestMethod.POST})
    public Result uploadExcel(@RequestParam MultipartFile file) {
        try {
            if (!excelTypes.contains(file.getContentType())) {
                return Result.fail(ResultCode.FAIL, "只能导入excel文件");
            }
            InputStream inputStream = file.getInputStream();
            ImportParams importParams = new ImportParams();
            importParams.setNeedVerfiy(true); // 设置需要校验
            ExcelImportResult<QuestionInfo> result = ExcelImportUtil.importExcelMore(inputStream, QuestionInfo.class, importParams);
            // 存在校验失败数据
            if (result.isVerfiyFail() && result.getFailList().size() > 1) {
                List<QuestionInfo> failQuestionInfoList = result.getFailList();
                StringBuilder errorMsg = new StringBuilder("表格第");
                for (int i = 0; i < failQuestionInfoList.size() - 1; i++ ) {
                    int rowNumber = failQuestionInfoList.get(i).getRowNum() + 1;
                    if (i == failQuestionInfoList.size() -1) {
                        errorMsg.append(rowNumber);
                    } else {
                        errorMsg.append(rowNumber + ",");
                    }
                }
                return Result.fail(ResultCode.FAIL, errorMsg.toString() + "行数据错误，请根据表格错误提示进行修改后再导入");
            }
            List<QuestionInfo> dataList = result.getList();
            questionInfoService.importQuestionFromExcel(dataList);
            return Result.success(ResultCode.SUCCESS, "数据导入成功");
        } catch (Exception e) {
            log.error("数据导入失败", e);
        }
        return Result.fail(ResultCode.FAIL, "导入失败, 请检查科目是否已存在或文件格式是否正确");
    }
}
