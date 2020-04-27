package com.education.service.core;

import com.baidu.ueditor.ActionEnter;
import com.education.common.base.BaseController;
import com.education.common.model.Captcha;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.*;
import com.education.service.system.SystemDictService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * api 公共接口
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 11:33
 */
@RestController
@Slf4j
public class ApiController extends BaseController {

    @Autowired
    private SystemDictService systemDictService;

    /**
     * 字典管理列表
     * @param params
     * @return
     */
    @GetMapping("/dict/list")
    public Result dictList(@RequestParam Map params) {
        return systemDictService.pagination(params);
    }

    /**
     * 添加或修改字典
     * @param params
     * @return
     */
    @PostMapping("/dict/saveOrUpdate")
    public ResultCode saveOrUpdate(@RequestBody ModelBeanMap params) {
        return systemDictService.saveOrUpdate(params);
    }

    @RequestMapping(value = "/ueditor/exec", method = { RequestMethod.GET, RequestMethod.POST })
    public String exec(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Content-Type" , "text/html");
        return new ActionEnter(request, PathKit.getWebRootPath()).exec();
    }

    protected static final Set<String> excelTypes = new HashSet<String>() {
        {
            add("application/x-xls");
            add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            add("application/vnd.ms-excel");
            add("text/xml");
        }
    };

    @Value("${file.uploadPath}")
    private String baseUploadPath;

    // 上传文件类型
    private static final int VIDEO_FILE = 1;
    private static final int IMAGE_FILE = 2;
    private static final int OTHER_FILE = 3;

    private static final Set<String> videoTypes = new HashSet<String>() {
        {
            add("video/mp4");
            add("video/x-ms-wmv");
            add("video/mpeg4");
            add("video/avi");
            add("video/mpeg");
            add("video/3gp");
        }
    };

    /**
     * 文件上传api 接口
     * @param file
     * @param uploadFileType
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "upload/{uploadFileType}", method = {RequestMethod.GET, RequestMethod.POST})
    public Map uploadFile(@RequestParam MultipartFile file, @PathVariable int uploadFileType) throws IOException {
        String result = null;
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        String suffix = "." + FilenameUtils.getExtension(fileName);
        String message = null;
        Map resultMap = new HashMap<>();
        switch (uploadFileType) {
            case VIDEO_FILE :
                if (file.getSize() > 500 * 1024 * 1024) {
                    resultMap.put("code", ResultCode.FAIL);
                    resultMap.put("message", "视频大小不能超过500MB");
                    return resultMap;
                }
                result = beforeUploadVideo(contentType, fileName);
                message = "视频";
                break;
            case IMAGE_FILE :
                result = beforeUploadImage(suffix);
                message = "图片";
                break;
            case OTHER_FILE :
                result = beforeUploadOtherFile(fileName);
                break;
        }

        if (ObjectUtils.isNotEmpty(result)) {
            try {
                String basePath = baseUploadPath + result;
                File filePath = new File(basePath);
                if (!filePath.getParentFile().exists()) {
                    filePath.getParentFile().mkdirs();
                }
                file.transferTo(filePath);
                resultMap.put("code", ResultCode.SUCCESS);
                resultMap.put("message", message + "上传成功");
                resultMap.put("url", result);
            } catch (Exception e) {
                resultMap.put("code", ResultCode.FAIL);
                resultMap.put("message", message + "文件上传失败");
                log.error(message + "上传失败", e);
            }
        } else {
            resultMap.put("code", ResultCode.FAIL);
            resultMap.put("message", message + "文件格式错误,请更换文件");
        }
        return resultMap;
    }


    private String beforeUploadVideo(String contentType, String fileName) {
        if (videoTypes.contains(contentType)) {
            return "/videos/" + ObjectUtils.generateFileByTime() + ObjectUtils.generateUuId() + "/" + fileName;
        }
        return null;
    }

    private String beforeUploadImage(String suffix) {
        return "/images/" + ObjectUtils.generateFileByTime() + ObjectUtils.generateUuId() + suffix;
    }

    private String beforeUploadOtherFile(String fileName) {
        return "/others/" + ObjectUtils.generateFileByTime() + ObjectUtils.generateUuId() + "/" + fileName;
    }

    /**
     * 获取汉字拼音
     * @param keyWord
     * @return
     */
    @GetMapping("getSpell")
    @ApiOperation(value = "公共api 获取汉字拼音接口")
    @ApiImplicitParam(name = "keyWord", value = "关键词", required = true, dataType = "string")
    public String getSpell(@RequestParam(defaultValue = "") String keyWord) {
        return SpellUtils.getSpellHeadChar(keyWord);
    }


    /**
     * 生成验证码
     * @param request
     * @param response
     */
    @GetMapping("/image")
    @ApiOperation("生成验证码接口")
    public void image(HttpServletRequest request, HttpServletResponse response) {
        String key = request.getParameter("key");
        Captcha captcha = new Captcha(redisTemplate, key);
        captcha.render(response);
    }
}
