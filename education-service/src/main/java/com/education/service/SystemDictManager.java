package com.education.service;

import com.education.common.component.SpringBeanManager;
import com.education.common.model.GradeTypeInfo;
import com.education.common.utils.ObjectUtils;
import com.education.mapper.system.SystemDictMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @descript:
 * @Auther: zengjintao
 * @Date: 2020/3/25 14:41
 * @Version:2.1.0
 */
public class SystemDictManager {

    private static final Map<String, Integer> questionTypeMap = new ConcurrentHashMap();
    private static final Map<String, GradeTypeInfo> gradeTypeMap = new ConcurrentHashMap();
    private static final Map<Integer, Integer> schoolTypeMap = new ConcurrentHashMap();
    private static SystemDictMapper systemDictMapper = null;


    public static Integer getSchoolTypeValue(Integer key) {
        if (schoolTypeMap.size() == 0) {
            synchronized (SystemDictManager.class) {
                Map params = new HashMap();
                params.put("type", "school_type");
                List<Map> list = getSystemDictMapper().queryList(params);
                list.stream().forEach(item -> {
                    schoolTypeMap.put((Integer) item.get("id"), (Integer) item.get("code"));
                });
            }
        }
        return schoolTypeMap.get(key);
    }

    public static GradeTypeInfo getGradeTypeValue(String key) {
        if (gradeTypeMap.size() == 0) {
            synchronized (SystemDictManager.class) {
                Map params = new HashMap();
                params.put("type", "grade_type");
                List<Map> list = getSystemDictMapper().queryList(params);
                list.stream().forEach(item -> {
                    GradeTypeInfo gradeTypeInfo = new GradeTypeInfo();
                    gradeTypeInfo.setName((String) item.get("value"));
                    gradeTypeInfo.setValue((Integer) item.get("code"));
                    gradeTypeInfo.setSchoolType((Integer) item.get("parent_id"));
                    gradeTypeMap.put((String) item.get("value"), gradeTypeInfo);
                });
            }
        }
        return gradeTypeMap.get(key);
    }

    public static Integer getQuestionTypeValue(String key) {
        if (questionTypeMap.size() == 0) {
            synchronized (SystemDictManager.class) {
                Map params = new HashMap();
                params.put("type", "question_type");
                List<Map> list = getSystemDictMapper().queryList(params);
                list.stream().forEach(item -> {
                    questionTypeMap.put((String) item.get("value"), (Integer) item.get("code"));
                });
            }
        }
        return questionTypeMap.get(key);
    }

    public static SystemDictMapper getSystemDictMapper() {
        if (ObjectUtils.isEmpty(systemDictMapper)) {
            systemDictMapper = SpringBeanManager.getBean(SystemDictMapper.class);
        }
        return systemDictMapper;
    }

    public static void clear() {
        questionTypeMap.clear();
        gradeTypeMap.clear();
        questionTypeMap.clear();
        schoolTypeMap.clear();
    }
}
