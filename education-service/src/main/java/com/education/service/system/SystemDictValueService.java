package com.education.service.system;

import com.education.common.constants.Constants;
import com.education.common.model.ModelBeanMap;
import com.education.common.model.online.OnlineUser;
import com.education.common.utils.MapTreeUtils;
import com.education.common.utils.ObjectUtils;
import com.education.mapper.system.SystemDictValueMapper;
import com.education.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemDictValueService extends BaseService<SystemDictValueMapper> {

    private static final String DICT_VALUE_CACHE_NAME = "system:dict:value:";

    public List<ModelBeanMap> getDictValueTreeList() {
        return MapTreeUtils.buildTreeData(mapper.treeList());
    }

    public List<ModelBeanMap> getDictValueByType(Map params) {
        String key = (String) params.get("type");
        List<ModelBeanMap> dictValueList = cacheBean.get(Constants.ONE_HOUR_CACHE, DICT_VALUE_CACHE_NAME + key);
        if (ObjectUtils.isEmpty(dictValueList)) {
            dictValueList = mapper.getDictValueByType(params);
            cacheBean.put(Constants.ONE_HOUR_CACHE, DICT_VALUE_CACHE_NAME + key, dictValueList);
        }
        return dictValueList;
    }

    public List<ModelBeanMap> getDictValueByParentId(Map params) {
        return mapper.getDictValueByParentId(params);
    }

    public Integer getDictValueByName(String type, String name) {
        Map params = new HashMap<>();
        params.put("type", type);
        List<ModelBeanMap> gradeTypeList = this.getDictValueByType(params);
        if (ObjectUtils.isNotEmpty(gradeTypeList)) {
            for (ModelBeanMap grade : gradeTypeList) {
                if (name.equals(grade.getStr("value"))) {
                    return grade.getInt("code");
                }
            }
        }
        return null;
    }

    public ModelBeanMap getDictValueForMapByName(String type, String name) {
        Map params = new HashMap<>();
        params.put("type", type);
        List<ModelBeanMap> dictValueList = this.getDictValueByType(params);
        if (ObjectUtils.isNotEmpty(dictValueList)) {
            for (ModelBeanMap dictValue : dictValueList) {
                if (name.equals(dictValue.getStr("value"))) {
                    return dictValue;
                }
            }
        }
        return null;
    }

    public String getDictNameByValue(String type, Integer code) {
        Map params = new HashMap<>();
        params.put("type", type);
        List<ModelBeanMap> gradeTypeList = this.getDictValueByType(params);
        if (ObjectUtils.isNotEmpty(gradeTypeList)) {
            for (ModelBeanMap grade : gradeTypeList) {
                if (grade.getInt("code") == code) {
                    return grade.getStr("value");
                }
            }
        }
        return null;
    }
}
