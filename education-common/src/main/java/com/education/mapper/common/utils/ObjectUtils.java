package com.education.mapper.common.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 字符串处理工具类
 * @author zengjintao
 * @version 1.0
 * @create_at 2017年6月18日 下午10:02:37
 */
public class ObjectUtils {

	/**
	 * 判断数组是否为空
	 * @param target
	 * @return
	 */
	public static boolean isEmpty(Object[] target) {
		return target == null || target.length == 0 ;
	}

	public static boolean isNotEmpty(Object[] target) {
		return target != null && target.length > 0;
	}

	/**
	 * 判断对象是否为空
	 *
	 * @param target
	 * @return
	 */
	public static boolean isEmpty(Object target) {
		if (target instanceof Collection) {
			Collection collection = (Collection)target;
			return collection == null || collection.size() == 0;
		} else if (target instanceof Map) {
			Map map = (Map)target;
			return map == null || map.size() == 0;
		}
		return target == null || target.toString().trim().length() == 0;
	}

	public static boolean isNotEmpty(Object target) {
		if (target instanceof Collection) {
			Collection collection = (Collection) target;
			return collection != null && collection.size() > 0;
		} else if (target instanceof Map) {
			Map map = (Map) target;
			return map != null || map.size() > 0;
		}
		return target != null && target.toString().trim().length() > 0;
	}



	/**
	 * 将字符串转换成数组
	 * @param str
	 * @return
	 */
	public static String[] spilt(String str) {
		if (str == null)
			throw new NullPointerException("str can not be null");
		return str.split(",");
	}

	public static String[] spilt(String str, String separator) {
		if (str == null)
			throw new NullPointerException("str can not be null");
		return str.split(separator);
	}

	/**
	 * 将首字母变成小写
	 * @param keyName
	 * @return
	 */
	public static String toLowerCaseFirst(String keyName){
		return keyName.substring(0, 1).toLowerCase();
	}

	/**
	 * 将首字母变成大写
	 * @param keyName
	 * @return
	 */
	public static String totoUpperCaseFirst(String keyName) {
		return keyName.substring(0, 1).toUpperCase() + keyName.substring(1, keyName.length());
	}

	/**
	 * 生成年月日字符串
	 * @return
	 */
	public static String generateFileByTime() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date()).replaceAll("-", "/");
	}

	/**
	 * 生成时分秒字符串
	 * @return
	 */
	public static String generateFileBySecond() {
		DateFormat format = new SimpleDateFormat("hh-mm-ss");
		return format.format(new Date()).replaceAll("-", "");
	}

	public static String generateUuId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
