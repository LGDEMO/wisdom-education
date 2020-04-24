package com.education.service.task;

import com.alibaba.fastjson.JSONObject;
import com.education.common.model.ModelBeanMap;
import com.education.mapper.school.SchoolInfoMapper;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 异步获取学校经纬度
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/14 15:21
 */
@Component
@Slf4j
public class PositionTask implements TaskListener {

    @Autowired
    private SchoolInfoMapper schoolInfoMapper;

    private static final Set<String> codeSet = new HashSet<String>() {
        {
            add("重庆市"); //重庆
            add("北京市"); //北京
            add("上海市"); //上海
            add("天津市");//天津
            add("香港"); //香港
            add("澳门"); //澳门
        }
    };


    public static boolean hasName(String provinceName){
        return codeSet.contains(provinceName);
    }

    private static final String LBS_HTTP_URL = "https://apis.map.qq.com/ws/geocoder/v1/?location=";

    @Override
    public void onMessage(TaskParam taskParam) {
        ModelBeanMap modelBeanMap = taskParam.getData();
        String result = HttpUtils.get(LBS_HTTP_URL + modelBeanMap.getStr("lat") + ","
                + modelBeanMap.get("lng") + "&key=" + modelBeanMap.getStr("key"));
        JSONObject jsonObject = JSONObject.parseObject(result);
        Map params = new HashMap<>();
        try {
            if (jsonObject != null && jsonObject.containsKey("result")) {
                JSONObject location = JSONObject.parseObject(jsonObject.getString("result"));
                if (location != null && location.containsKey("address")) {
                    String address = location.getString("address");
                    params.put("address", address);
                }
                if (location != null && location.containsKey("ad_info")) {
                    Map<String, String> resultMap = (Map<String, String>) JSONObject.parse(location.getString("ad_info"));
                    if (resultMap.containsKey("adcode")) {
                        String districtCode = resultMap.get("adcode"); //区编码
                        String provinceName = resultMap.get("province");
                        if (hasName(provinceName)) { //保存地区code
                            if (StringUtils.isNotBlank(districtCode) && districtCode.length() >= 2) {
                                params.put("province_code", districtCode.substring(0, 2) + "0000");
                                params.put("city_code", districtCode.substring(0, 2) + "0000");
                            }
                        } else {
                            if (StringUtils.isNotEmpty(districtCode) && districtCode.length() >= 2) {
                                String provinceCode = districtCode.substring(0, 2) + "0000";
                                params.put("province_code", provinceCode);
                            }
                            if (StringUtils.isNotEmpty(districtCode) && districtCode.length() >= 4) {
                                String cityCode = districtCode.substring(0, 4) + "00";
                                params.put("city_code", cityCode);
                            }
                        }
                        params.put("town_code", districtCode);
                    }
                }
                modelBeanMap.remove("key");
                schoolInfoMapper.update(params);
            }
        } catch (Exception e) {
            log.error("获取定位异常", e);
        }
    }
}
