package com.education.service.task;

import com.alibaba.fastjson.JSONObject;
import com.education.common.base.BaseMapper;
import com.education.common.model.ModelBeanMap;
import com.education.event.BaseTask;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/14 15:21
 */
public class PositionTask extends BaseTask {

    public static Set<String> codeSet = new HashSet<String>() {
        {
            codeSet.add("重庆市"); //重庆
            codeSet.add("北京市"); //北京
            codeSet.add("上海市"); //上海
            codeSet.add("天津市");//天津
            codeSet.add("香港"); //香港
            codeSet.add("澳门"); //澳门
        }
    };

    private BaseMapper baseMapper;

    public PositionTask(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    public static boolean hasName(String provinceName){
        return codeSet.contains(provinceName);
    }

    private static final String LBS_HTTP_URL = "https://apis.map.qq.com/ws/geocoder/v1/?location=";

    @Override
    public void run() {
        try {
            ModelBeanMap modelBeanMap = getModelBeanMap();
            String result = HttpUtils.get(LBS_HTTP_URL + modelBeanMap.getStr("lat") + ","
                    + modelBeanMap.get("lng") + "&key=" + modelBeanMap.getStr("key"));
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject != null && jsonObject.containsKey("result")) {
                JSONObject loaction = JSONObject.parseObject(jsonObject.getString("result"));
                if (loaction != null && loaction.containsKey("address")) {
                    String address = loaction.getString("address");
                    put("address", address);
                }
                if (loaction != null && loaction.containsKey("ad_info")) {
                    Map<String, String> resultMap = (Map<String, String>) JSONObject.parse(loaction.getString("ad_info"));
                    if (resultMap.containsKey("adcode")) {
                        String districtCode = resultMap.get("adcode"); //区编码
                        String provinceName = resultMap.get("province");
                        if (hasName(provinceName)) { //保存地区code
                            if (StringUtils.isNotBlank(districtCode) && districtCode.length() >= 2) {
                                put("province_code", districtCode.substring(0, 2) + "0000");
                                put("city_code", districtCode.substring(0, 2) + "0000");
                            }
                        } else {
                            if (StringUtils.isNotEmpty(districtCode) && districtCode.length() >= 2) {
                                String provinceCode = districtCode.substring(0, 2) + "0000";
                                put("province_code", provinceCode);
                            }
                            if (StringUtils.isNotEmpty(districtCode) && districtCode.length() >= 4) {
                                String cityCode = districtCode.substring(0, 4) + "00";
                                put("city_code", cityCode);
                            }
                        }
                        put("town_code", districtCode);
                    }
                }
                modelBeanMap.remove("key");
                baseMapper.update(getModelBeanMap());
            }
        } catch (Exception e) {
            logger.error("获取定位异常", e);
        }
    }
}
