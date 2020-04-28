package com.education.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


public class IpUtils {

	private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);

	/**
	 * 获取客户端ip地址
	 * @return
	 */
   public static String getAddressIp(HttpServletRequest request) {
	   String ip = request.getHeader("x-forwarded-for");
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	      ip = request.getRemoteAddr();
	    }
	    return ip;
    }

    private static final String IP_SERVICE_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=";

    /**
     * 获取ip地址
     * @param ip
     * @return
     */
    public static String getIpAddress(String ip){
		String address = null;
		try {
	//		{"code":0,"data":{"country":"中国","country_id":"CN","area":"华北","area_id":"100000","region":"北京市","region_id":"110000","city":"北京市","city_id":"110100","county":"","county_id":"-1","isp":"阿里巴巴","isp_id":"100098","ip":"47.94.12.108"}}
			String data = HttpUtils.get(IP_SERVICE_URL + ip);
			JSONObject jsonObject = JSONObject.parseObject(data);
			if (ObjectUtils.isNotEmpty(jsonObject)) {
				if (jsonObject.getInteger("code") == 0) {
					JSONObject result = (JSONObject)jsonObject.get("data");
					address = (String) result.get("country") + result.get("region") + result.get("city");
				}
			}
			return address;
		} catch (Exception e) {
			logger.error("获取ip地址异常", e);
		}
		return "";
	}
}
