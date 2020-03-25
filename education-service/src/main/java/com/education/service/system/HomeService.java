package com.education.service.system;

import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.school.StudentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/12 11:29
 */
@Service
@Slf4j
public class HomeService {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StudentInfoService studentInfoService;

    public Result homeData() {
        Result result = new Result();
        try {
            Integer schoolId = (Integer) studentInfoService.getAdminUser().get("school_id");
            Map data = new HashMap<>();
            Integer questionNumber = jdbcTemplate.queryForObject("select count(1) as question_number from question_info", Integer.class);
            Integer studentNumber = 0;
            data.put("has_school_data", true);
            if (ObjectUtils.isEmpty(schoolId)) {
                Integer schoolNumber = jdbcTemplate.queryForObject("select count(1) as school_number from school_info", Integer.class);
                data.put("school_number", schoolNumber);
                studentNumber = jdbcTemplate.queryForObject("select count(1) as student_number from student_info", Integer.class);
            } else {
                data.put("has_school_data", false);
                studentNumber = jdbcTemplate.queryForObject("select count(1) as student_number from student_info where school_id = ?",
                        Integer.class, schoolId);
            }
            data.put("question_number", questionNumber);
            data.put("student_number", studentNumber);
            result.setData(data);
        } catch (Exception e) {
            result.setCode(ResultCode.FAIL);
            result.setMessage("获取数据异常");
            log.error("获取数据异常", e);
        }
        return result;
    }
}
