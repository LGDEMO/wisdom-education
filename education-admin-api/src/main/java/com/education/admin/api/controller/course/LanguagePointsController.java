package com.education.admin.api.controller.course;

import com.education.common.annotation.Param;
import com.education.common.annotation.ParamsType;
import com.education.common.annotation.ParamsValidate;
import com.education.common.base.BaseController;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.MapTreeUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.service.course.LanguagePointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 知识点管理接口
 */
@RestController
@RequestMapping("/system/languagePoints")
public class LanguagePointsController extends BaseController {

    @Autowired
    private LanguagePointsService languagePointsService;

    /**
     * 知识点列表
     * @param params
     * @return
     */
    @GetMapping
    public Result list(@RequestParam Map params) {
        Result result = languagePointsService.pagination(params);
        ModelBeanMap modelBeanMap = (ModelBeanMap) result.getData();
        List<ModelBeanMap> data = (List<ModelBeanMap>) modelBeanMap.get("dataList");
        if (ObjectUtils.isNotEmpty(params.get("lazy")) && "false".equals(params.get("lazy"))) {
            modelBeanMap.put("dataList", MapTreeUtils.buildTreeData(data));
        }
        return result;
    }

    /**
     * 添加或修改知识点
     * @param modelBeanMap
     * @return
     */
    @ParamsValidate(params = {
       @Param(name = "name", message = "请输入知识点名称"),
       @Param(name = "grade_type", message = "请选择所属年级"),
       @Param(name = "subject_id", message = "请选择所属科目")
    }, paramsType = ParamsType.JSON_DATA)
    @PostMapping
    public Result saveOrUpdate(@RequestBody ModelBeanMap modelBeanMap) {
        return languagePointsService.saveOrUpdate(modelBeanMap);
    }

    /**
     * 获取知识点详情
     * @param id
     * @return
     */
    @GetMapping("findById")
    public Result findById(Integer id) {
        return Result.success(languagePointsService.findById(id));
    }

    /**
     * 获取知识点父类id集合
     * @param languagePointsId
     * @return
     */
    @GetMapping("getParentId")
    public Result getParentId(Integer languagePointsId) {
        List<Integer> parentIds = languagePointsService.getParentId(languagePointsId);
        parentIds.add(languagePointsId);
        return null;
      //  return parentIds;
    }
}
