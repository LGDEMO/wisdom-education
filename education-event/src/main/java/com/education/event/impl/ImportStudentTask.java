package com.education.event.impl;

import com.education.event.BaseTask;
import com.education.common.component.SpringBeanManager;
import com.education.common.exception.BusinessException;
import com.education.common.model.StudentInfo;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.service.school.StudentInfoService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/4/21 15:11
 */
public class ImportStudentTask extends BaseTask {


    @Override
    public void run() {
        try {
            List<StudentInfo> studentInfoList = getData();
            StudentInfoService userInfoService = getStudentInfoService();
            Map adminUserMap = userInfoService.getAdminUser();
            Integer schoolId = 0;
            if (ObjectUtils.isNotEmpty(adminUserMap)) {
                schoolId = (Integer) adminUserMap.get("school_id");
            }
            for (StudentInfo studentInfo : studentInfoList) {
                if (ObjectUtils.isEmpty(studentInfo.getName())) {
                    continue;
                }
                studentInfo.setSchoolId(schoolId);
                String gradeName = studentInfo.getGradeType();
                studentInfo.setSexId("男".equals(studentInfo.getSex()) ? ResultCode.SUCCESS : ResultCode.FAIL);
                if (ObjectUtils.isNotEmpty(primarySchool.get(gradeName))) {
                    studentInfo.setGradeTypeId(primarySchool.get(gradeName));
                } else if (ObjectUtils.isNotEmpty(middleSchool.get(gradeName))) {
                    studentInfo.setGradeTypeId( middleSchool.get(gradeName));
                } else {
                    studentInfo.setGradeTypeId( highSchool.get(gradeName));
                }
                Date now = new Date();
                studentInfo.setCreateDate(now);
                studentInfo.setUpdateDate(now);
            }
            sqlSessionTemplate.insert("student.info.batchSave", studentInfoList);
        } catch (Exception e) {
            logger.error("学员数据导入失败", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "导入失败, 请检查数据是否合法或文件格式是否正确"));
        } finally {
            countDownLatch.countDown();
        }
    }

    private StudentInfoService getStudentInfoService() {
        return SpringBeanManager.getBean(StudentInfoService.class);
    }
}
