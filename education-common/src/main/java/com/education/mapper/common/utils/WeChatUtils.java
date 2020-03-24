package com.education.mapper.common.utils;

import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.TemplateData;
import com.jfinal.weixin.sdk.api.TemplateMsgApi;

import java.util.Map;

public class WeChatUtils {

    public static void sendTempletMsg(Map<String, String> params) {
        ApiResult result = TemplateMsgApi.send(getTemplateContent(params));;
        if (result.isSucceed()) {
           // logger.info("模板消息发送成功");
        } else {
          //  logger.error("ErrorCode:" + result.getErrorCode() + "\n" + "errorMsg:" + result.getErrorMsg());
        }
    }

    public static String getTemplateContent(Map<String, String> params) {
        String color = "#999";
        String openId = (String) params.get("openId");
        String templateId = (String) params.get("templateId");
        return TemplateData.New()
                // 消息接收者
                .setTouser(openId)
                .setTemplate_id(templateId)
                // 模板参数
                .add("first", params.get("first"), color)
                .add("keyword1", params.get("name"), color)
                .add("keyword2", params.get("phone"), color)
                .add("keyword3", params.get("phone"), color)
                .add("keyword4", params.get("transmitTime"), color)
                .add("remark", params.get("remark"), color)
                .build();
    }
}
