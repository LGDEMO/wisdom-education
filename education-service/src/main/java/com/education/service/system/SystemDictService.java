package com.education.service.system;


import com.education.common.model.ModelBeanMap;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.mapper.system.SystemDictMapper;
import com.education.service.BaseService;
import org.springframework.stereotype.Service;


/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 21:16
 */
@Service
public class SystemDictService extends BaseService<SystemDictMapper> {

    public ResultCode saveOrUpdate(ModelBeanMap params) {
        boolean updateFlag = false;
        if (ObjectUtils.isNotEmpty(params.get("id"))) {
            updateFlag = true;
        }
        return super.saveOrUpdate(updateFlag, params);
    }
}
