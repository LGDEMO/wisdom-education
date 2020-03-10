package com.education.admin.api.controller.system;

import com.education.common.base.BaseController;
import com.education.common.utils.Result;
import com.education.service.system.SystemDictService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
