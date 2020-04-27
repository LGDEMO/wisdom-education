package com.education.admin.api.controller.system;

import com.education.common.base.BaseController;
import com.education.common.utils.Result;
import com.education.service.system.SystemMessageInfoMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 系统消息通知接口
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/15 13:59
 */
@RestController
@RequestMapping("/system/message")
public class SystemMessageController extends BaseController {

    @Autowired
    private SystemMessageInfoMessageService systemMessageInfoMessageService;

    @GetMapping
    public Result list(@RequestParam Map params) {
        return systemMessageInfoMessageService.pagination(params);
    }
}
