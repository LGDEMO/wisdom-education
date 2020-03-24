package com.education.service.system;

import com.education.mapper.common.model.ModelBeanMap;
import com.education.mapper.common.utils.Result;
import com.education.mapper.system.SystemMessageInfoMapper;
import com.education.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/12 14:06
 */
@Service
public class SystemMessageInfoMessageService extends BaseService<SystemMessageInfoMapper> {

    @Override
    public Result<ModelBeanMap> pagination(Map params) {
        params.put("admin_id", getAdminUser().get("id"));
        return super.pagination(params);
    }
}
