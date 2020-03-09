package com.education.service.system;

import com.education.common.base.BaseService;
import com.education.common.model.ModelBeanMap;
import com.education.mapper.system.SystemAdminRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 14:44
 */
@Service
public class SystemAdminRoleService extends BaseService<SystemAdminRoleMapper> {

    public List<ModelBeanMap> findRoleListByAdminId(Integer adminId) {
        return mapper.findRoleListByAdminId(adminId);
    }
}
