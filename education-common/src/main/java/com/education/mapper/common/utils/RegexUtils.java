package com.education.mapper.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/16 10:48
 */
public class RegexUtils {

    public static boolean compile(String regex, Object value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(value));
        return matcher.matches();
    }
}
