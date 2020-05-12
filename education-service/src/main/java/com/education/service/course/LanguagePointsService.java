package com.education.service.course;

import com.education.common.model.ModelBeanMap;
import com.education.common.utils.MapTreeUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.mapper.course.LanguagePointsMapper;
import com.education.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 知识点管理service
 */
@Service
public class LanguagePointsService extends BaseService<LanguagePointsMapper> {

    @Autowired
    private SubjectInfoService subjectInfoService;

    @Override
    @Transactional
    public Result saveOrUpdate(ModelBeanMap modelBeanMap) {
        // 将父类的hasChildren 属性设置为true
        Integer parentId = modelBeanMap.getInt("parent_id");
        if (ObjectUtils.isNotEmpty(modelBeanMap.getInt("parent_id"))) {
            Map params = new HashMap<>();
            params.put("hasChildren", true);
            params.put("id", parentId);
            super.update(params);
        }
        return super.saveOrUpdate(modelBeanMap);
    }

    public Map findById(Integer id) {
        Map languagePointsMap = mapper.findById(id);
        Map params = new HashMap<>();
        params.put("gradeType", languagePointsMap.get("grade_type"));
        int parentId = (Integer)languagePointsMap.get("parent_id");
        List<Integer> parentIds = new ArrayList();
        parentIds = getParentIds(parentId, parentIds);
        Collections.reverse(parentIds); // 对集合倒序排序
        // 选中知识点的所有父类id
        languagePointsMap.put("parentIdArray", parentIds);
        Map resultMap = (Map) subjectInfoService.pagination(params).getData();
        List<Map> subjectList = (List<Map>) resultMap.get("dataList");
        languagePointsMap.put("subjectList", subjectList);
        // 获取年级科目知识点列表
        params.put("subjectId", languagePointsMap.get("subject_id"));
        Result<ModelBeanMap> result = super.pagination(params);
        languagePointsMap.put("languagePointsList", MapTreeUtils.buildTreeData((List<ModelBeanMap>) result.getData().get("dataList")));
        return languagePointsMap;
    }

    public List<Integer> getParentId(Integer languagePointsId) {
        Map courseMap = mapper.findById(languagePointsId);
        Integer parentId = (Integer) courseMap.get("parent_id");
        List<Integer> parentIds = new ArrayList<>();
        if (parentId > 0) {
            return getParentIds(parentId, parentIds);
        }
        parentIds.add(parentId);
        return parentIds;
    }

    public List<Integer> getParentIds(int parentId, List<Integer> parentIds) {
        if (parentId != ResultCode.FAIL) {
            Map parentMap = mapper.findById(parentId);
            if (ObjectUtils.isNotEmpty(parentMap)) {
                int newParentId = (Integer)parentMap.get("parent_id");
                parentIds.add((Integer)parentMap.get("id"));
                return getParentIds(newParentId, parentIds);
            }
        }
        return parentIds;
    }
}
