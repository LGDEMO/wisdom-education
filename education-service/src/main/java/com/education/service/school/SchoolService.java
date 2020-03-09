package com.education.service.school;
import com.education.common.base.BaseService;
import com.education.common.exception.BusinessException;
import com.education.common.utils.DateUtils;
import com.education.common.utils.Md5Utils;
import com.education.common.utils.ResultCode;
import com.education.mapper.school.SchoolInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Transactional
    public ResultCode saveOrUpdate(boolean updateFlag, String operationDesc, Map params) {
        String message = "";
        try {
            checkParams(params);
            params.remove("sqlId");
            if (updateFlag) {
                checkParams(params);
                if (params.containsKey("add_account_number")) {
                    int addAccountNumber = (int) params.get("add_account_number");
                    int beforeUpdateAccountNumber = (int) params.get("old_account_number");
                    Integer schoolId = (Integer) params.get("id");
                    String simplicity = (String) params.get("simplicity");
                    this.createUserAccount(addAccountNumber, simplicity, beforeUpdateAccountNumber, schoolId, true);
                    params.remove("add_account_number");
                    params.remove("old_account_number");
                }
                message = "修改";
                super.update(params);
            } else {
                Date now = new Date();
                params.put("create_date", now);
                params.put("update_date", now);
                Integer schoolId = super.save(params);
                message = "添加";
                params.put("schoolId", schoolId);
                createPrincipalAndStudentAccount(params);
            }
            this.saveSystemLog(message + "学校" + params.get("name"));
            return new ResultCode(ResultCode.SUCCESS, message + "学校成功");
        } catch (Exception e) {
            log.error(message + "学校失败", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, message + "学校失败"));
        }
    }

    @Transactional
    public ResultCode deleteById(Map params) {
        try {
            params.put("operation", "删除学校" + params.get("name"));
            /*super.deleteById(params); // 删除学校
            sqlSessionTemplate.delete("system.admin.deleteBySchoolId", params.get("id"));
            super.saveSystemLog("删除学校" + params.get("name") + "校长账号");
            sqlSessionTemplate.delete("user.info.deleteBySchoolId", params.get("id"));
            super.saveSystemLog("删除学校" + params.get("name") + "学生账号");
            sqlSessionTemplate.delete("student.info.deleteBySchoolId", params.get("id"));
            super.saveSystemLog("删除学校" + params.get("name") + "所有学员");*/
            return new ResultCode(ResultCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            log.error("删除学校异常", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "删除学校异常"));
        }
    }

    /**
     * 创建校长学生账号
     * @param params
     */
    private void createPrincipalAndStudentAccount(Map params) {
        String simplicity = (String)params.get("simplicity");
        Integer schoolId = (Integer)params.get("schoolId");
        Date now = new Date();
      //  String schoolName = (String) params.get("name");
        Integer accountNumber = (Integer) params.get("account_number");
        // 创建三个校长账号
        for (int i = 1; i <= 3; i++) {
            String principalName = (String)params.get("principal_name");
            params.clear();
            params.put("login_name", simplicity + i);
            params.put("name", principalName);
            String encrypt = Md5Utils.encodeSalt(Md5Utils.generatorKey());
            params.put("encrypt", encrypt);
            String password = Md5Utils.getMd5(simplicity, encrypt);
            params.put("password", password);
            params.put("school_id", schoolId);
            params.put("create_date", now);
            params.put("update_date", now);
         //   int adminId = super.save("system.admin.save", params);
        //    Map roleMap = sqlSessionTemplate.selectOne("system.role.findByName", PRINCIPAL);
            params.clear();
         //   params.put("admin_id", adminId);
         //   params.put("role_id", roleMap.get("id"));
          //  super.save("system.admin.role.save", params); // 关联校长账号角色权限
        }
        this.createUserAccount(accountNumber, simplicity, accountNumber, schoolId, false);
    }

    /**
     * @param accountNumber 账号数量
     * @param simplicity 学校简称
     * @param beforeUpdateAccountNumber 修改之前的学员账号数量
     * @param schoolId 学校id
     * @param addFlag 是否在之前的账号基础上添加账号
     */
    private void createUserAccount(int accountNumber, String simplicity,
                                   int beforeUpdateAccountNumber,
                                   Integer schoolId, boolean addFlag) {
        // 由于学员账号是由学校简称后面加数字依次递增
        List<String> accountList = new ArrayList<>();
        int offset = 0;
        if (addFlag) {
            offset = beforeUpdateAccountNumber;
        }
        for (int i = 0; i < accountNumber; i++) {
            accountList.add(simplicity + (offset + 1)); // 以学校简称开头随机生成100个账户名
            offset++;
        }

        List<Map> userInfoList = new ArrayList<>();
        Iterator<String> iterator = accountList.iterator();
        while (iterator.hasNext()) {
            Map userInfo = new HashMap<>();
            String account = iterator.next();
            userInfo.put("school_id", schoolId);
            userInfo.put("login_name", account);
            String salt = Md5Utils.encodeSalt(Md5Utils.generatorKey());
            userInfo.put("encrypt", salt);
            userInfo.put("password", Md5Utils.getMd5(account, salt));
            userInfo.put("create_date", new Date());
            userInfo.put("invalid_date", DateUtils.getAfterYearDate(1));
         //   userInfo.put("activate_code", super.getActivateCode("user.info.findByActivateCode", "activate_code"));
            userInfoList.add(userInfo);
        }
       // sqlSessionTemplate.insert("user.info.batchInsert", userInfoList);
    }

    public List<Map> getSchoolPosition() {
        return null;
       // return sqlSessionTemplate.selectList("school.info.list.position");
    }
}
