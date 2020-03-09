package com.education.common.base;

import com.education.common.constants.Constants;
import com.education.common.utils.ResultCode;
import com.education.common.utils.SpellUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 11:33
 */
public class ApiController extends BaseController {

    protected static final Set<String> excelTypes = new HashSet<String>() {
        {
            add("application/x-xls");
            add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            add("application/vnd.ms-excel");
            add("text/xml");
        }
    };

    protected Object checkCode(HttpSession session, Map params) {
        String code = (String)params.get("code");
        String sessionImageCode = (String)session.getAttribute(Constants.IMAGE_CODE);
        if (!code.equalsIgnoreCase(sessionImageCode)) {
            params.put("code", ResultCode.FAIL);
            params.put("message", "验证码错误");
            return params;
        }
        return null;
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
}
