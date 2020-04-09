package com.education.service.system;


import com.education.common.model.ModelBeanMap;
import com.education.common.utils.MapTreeUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.mapper.system.SystemDictMapper;
import com.education.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 21:16
 */
@Service
public class SystemDictService extends BaseService<SystemDictMapper> {

    public Result getDictValueList(Map params) {
        return super.pagination(params, mapper.getClass(), SystemDictMapper.GET_DICT_VALUE_LIST);
    }

    public ResultCode saveOrUpdate(ModelBeanMap params) {
        boolean updateFlag = false;
        if (ObjectUtils.isNotEmpty(params.get("id"))) {
            updateFlag = true;
        }
        return super.saveOrUpdate(updateFlag, params);
    }
}
