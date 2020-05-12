package com.education.admin.api.controller.system;

import com.education.common.base.BaseController;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.Result;

import com.education.common.utils.ResultCode;
import com.education.service.system.SystemDictService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 21:15
 */
@RequestMapping("/system/dict")
@RestController
@Api(tags = "字典管理接口")
public class SystemDictController extends BaseController {

    @Autowired
    private SystemDictService systemDictService;

    @GetMapping
    public Result list(@RequestParam Map params) {
        return systemDictService.pagination(params);
    }

    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ModelBeanMap params) {
        return systemDictService.saveOrUpdate(params);
    }
}
