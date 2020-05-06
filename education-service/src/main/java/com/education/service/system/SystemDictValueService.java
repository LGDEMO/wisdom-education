package com.education.service.system;

import com.education.common.model.ModelBeanMap;
import com.education.common.utils.MapTreeUtils;
import com.education.mapper.system.SystemDictValueMapper;
import com.education.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SystemDictValueService extends BaseService<SystemDictValueMapper> {

    public List<ModelBeanMap> getDictValueTreeList() {
        return MapTreeUtils.buildTreeData(mapper.treeList());
    }

    public List<ModelBeanMap> getDictValueByType(Map params) {
        return mapper.getDictValueByType(params);
    }

    public List<ModelBeanMap> getDictValueByParentId(Map params) {
        return mapper.getDictValueByParentId(params);
    }

    public int deleteByDictId(Integer dictId) {
        return mapper.deleteByDictId(dictId);
    }

    public int deleteDictValueById(ModelBeanMap dictValueBeanMap) {
        return mapper.deleteById(dictValueBeanMap.getInt("id"));
    }
}
