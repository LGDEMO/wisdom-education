package com.education.admin.api.controller;

import com.education.common.model.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/4/7 11:27
 */
@RestController
@Api(tags = "验证码接口")
public class ImageController {


    /**
     * 生成验证码
     * @param request
     * @param response
     */
    @GetMapping("/image")
    @ApiOperation("生成验证码接口")
    public void image(HttpServletRequest request, HttpServletResponse response){
        Captcha captcha = new Captcha();
        captcha.render(request, response);
    }

    /**
     * 微信公众号授权配置文件接口
     * @return
     */
    @GetMapping("MP_verify_W2WeDnp77xFcTDlk.txt")
    public String weChatConfig() {
        return "W2WeDnp77xFcTDlk";
    }
}
