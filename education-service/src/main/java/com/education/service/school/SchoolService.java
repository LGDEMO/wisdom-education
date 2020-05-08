package com.education.service.school;

import com.education.common.constants.EnumConstants;
import com.education.common.exception.BusinessException;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.Md5Utils;
import com.education.common.utils.ResultCode;
import com.education.mapper.school.SchoolInfoMapper;
import com.education.mapper.school.StudentInfoMapper;
import com.education.mapper.system.SystemAdminMapper;
import com.education.mapper.system.SystemAdminRoleMapper;
import com.education.mapper.system.SystemRoleMapper;
import com.education.service.BaseService;
import com.education.service.task.PositionListener;
import com.education.service.task.TaskParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 学校管理业务层
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/4/18 22:23
 */
@Service
@Slf4j
public class SchoolService extends BaseService<SchoolInfoMapper> {

    private static final String PRINCIPAL = "校长";
    @Autowired
    private SystemAdminMapper systemAdminMapper;
    @Autowired
    private StudentInfoMapper studentInfoMapper;
    @Autowired
    private SystemRoleMapper systemRoleMapper;
    @Autowired
    private SystemAdminRoleMapper systemAdminRoleMapper;
    @Value("${lbs.key}")
    private String lbsKey;

    @Transactional
    @Override
    public ResultCode saveOrUpdate(boolean updateFlag, ModelBeanMap schoolMap) {
        String message = "";
        try {
            if (updateFlag) {
                checkParams(schoolMap);
                message = "修改";
                Integer result = super.update(schoolMap);
                if (result > 0) {
                    this.updateSchoolAddress(schoolMap.getInt("id"), schoolMap.getStr("lat"), schoolMap.getStr("lng"));
                }
            } else {
                Date now = new Date();
                schoolMap.put("create_date", now);
                schoolMap.put("update_date", now);
                Integer schoolId = super.save(schoolMap);
                this.updateSchoolAddress(schoolMap.getInt("id"), schoolMap.getStr("lat"), schoolMap.getStr("lng"));
                message = "添加";
                schoolMap.put("schoolId", schoolId);
                createPrincipalAccount(schoolMap);
            }
            return new ResultCode(ResultCode.SUCCESS, message + "学校成功");
        } catch (Exception e) {
            log.error(message + "学校失败", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, message + "学校失败"));
        }
    }

    private void updateSchoolAddress(Integer schoolId, String lat, String lng) {
        TaskParam taskParam = new TaskParam(PositionListener.class);
        Map params = new HashMap<>();
        params.put("id", schoolId);
        params.put("lat", lat);
        params.put("key", lbsKey);
        params.put("lng", lng);
        taskManager.pushTask(taskParam);
    }

    @Transactional
    public ResultCode deleteById(ModelBeanMap schoolInfoMap) {
        try {
            super.deleteById(schoolInfoMap); // 删除学校
            systemAdminMapper.deleteBySchoolId(schoolInfoMap.getInt("id"));
            schoolInfoMap.put("schoolId", schoolInfoMap.getInt("id"));
            studentInfoMapper.deleteByIdOrSchoolId(schoolInfoMap);
            return new ResultCode(ResultCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            log.error("删除学校异常", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "删除学校异常"));
        }
    }

    /**
     * 创建校长后台账号
     * @param params
     */
    private void createPrincipalAccount(Map params) {
        String simplicity = (String)params.get("simplicity");
        Integer schoolId = (Integer)params.get("schoolId");
        Date now = new Date();
        String principalName = (String)params.get("principal_name");
        // 创建三个校长账号
        for (int i = 1; i <= 3; i++) {
            params.clear();
            params.put("login_name", simplicity + i);
            params.put("name", principalName);
            String encrypt = Md5Utils.encodeSalt(Md5Utils.generatorKey());
            params.put("encrypt", encrypt);
            String password = Md5Utils.getMd5(simplicity, encrypt);
            params.put("password", password);
            params.put("school_id", schoolId);
            params.put("principal_flag", EnumConstants.Flag.YES.getValue());
            params.put("create_date", now);
            params.put("update_date", now);
            Integer adminId = systemAdminMapper.save(params);
            Map roleMap = systemRoleMapper.findByRoleName(PRINCIPAL);
            params.clear();
            params.put("admin_id", adminId);
            params.put("role_id", roleMap.get("id"));
            systemAdminRoleMapper.save(params);// 关联校长账号角色权限
        }
    }
}
