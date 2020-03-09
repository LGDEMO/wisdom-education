package com.education.common.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/3/22 22:32
 */
@Slf4j
public class RequestUtils {

    static final Set<Integer> PORT_SET = new HashSet<Integer>() {
        {
            add(80);
            add(443);
        }
    };

    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder ret;
            br = request.getReader();

            String line = br.readLine();
            if (line != null) {
                ret = new StringBuilder();
                ret.append(line);
            } else {
                return "";
            }

            while ((line = br.readLine()) != null) {
                ret.append('\n').append(line);
            }
            return ret.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRequestUrl(HttpServletRequest request) {
        String contentPath = request.getServletContext().getContextPath();
        int contentPathLength = contentPath.length();
        String target = request.getRequestURI();
        if (contentPathLength != 0){
            target = target.substring(contentPathLength);
        }
        return target;
    }

    /**
     * 获取地址栏域名
     * @return
     */
    public static String getDomain() {
        HttpServletRequest request = getRequest();
        String scheme = request != null ? request.getScheme() : null; //得到协议名 例如：http
        String serverName = request != null ? request.getServerName() : null; //得到域名 localhost
        int port = request.getServerPort();
        if (!PORT_SET.contains(port)) {
            return scheme + "://" + serverName + ":" + port;
        }
        String domain = scheme + "://" + serverName;
        if (domain.contains("www.")) {
            domain = domain.replaceAll("www.", "");
        }
        return domain;
    }

    public static String getUploadDomain() {
        return getDomain() + "/uploads";
    }

    public static HttpServletResponse getResponse() {
        try {
            return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
        } catch (Exception e) {
            return null;
        }
    }

    public static InputStream getInputStreamFromUrl(String url) {
        try {
            URL resource = new URL(url);
            URLConnection connection = resource.openConnection();
            return connection.getInputStream();
        } catch (Exception e) {
            log.error("获取流异常,请检查url是否正确", e);
        }
        return null;
    }

    public static String getCookieValue(String name) {
        Cookie cookie = getCookie(name);
        return ObjectUtils.isNotEmpty(cookie) ? cookie.getValue() : null;
    }

    public static Cookie getCookie(String cookieName) {
        HttpServletRequest request = RequestUtils.getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 清空cookie
     * @param cookieName
     */
    public static void clearCookie(String cookieName) {
        Cookie cookie = getCookie(cookieName);
        HttpServletResponse response = getResponse();
        if (ObjectUtils.isNotEmpty(cookie)) {
            cookie.setMaxAge(0); // 清除cookie
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }

    public static void createCookie(String cookieName, String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        HttpServletResponse response = RequestUtils.getResponse();
        response.addCookie(cookie);
    }
}
