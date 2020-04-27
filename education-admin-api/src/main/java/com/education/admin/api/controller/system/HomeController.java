package com.education.admin.api.controller.system;

import com.education.common.base.BaseController;
import com.education.common.utils.Result;
import com.education.service.system.HomeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页数据接口
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/2/16 16:07
 */
@RequestMapping("/system/home")
@RestController
@Api(tags = "首页数据接口")
public class HomeController extends BaseController {

    @Autowired
    private HomeService homeService;

    @GetMapping
    public Result test() {
        return homeService.homeData();
    }
}
