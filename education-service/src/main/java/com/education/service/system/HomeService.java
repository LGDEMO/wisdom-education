package com.education.service.system;

import com.education.common.model.ModelBeanMap;
import com.education.common.utils.DateUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.service.course.ExamInfoService;
import com.education.service.school.SchoolService;
import com.education.service.school.StudentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 首页数据统计
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
    @Autowired
    private ExamInfoService examInfoService;
    @Autowired
    private SchoolService schoolService;

    public Result homeData() {
        Result result = new Result();
        try {
            Integer schoolId = (Integer) studentInfoService.getAdminUser().get("school_id");
            Map data = new HashMap<>();
            Integer questionNumber = jdbcTemplate.queryForObject("select count(1) as question_number from question_info", Integer.class);
            Integer studentNumber = 0;
            data.put("has_school_data", true);
            data.put("school_number", 0);
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
            this.setExamInfoData(data);
            result.setData(data);
        } catch (Exception e) {
            result.setCode(ResultCode.FAIL);
            result.setMessage("获取数据异常");
            log.error("获取数据异常", e);
        }
        return result;
    }

    /**
     * 获取考试记录统计
     * @param resultMap
     */
    public void setExamInfoData(Map resultMap) {
        Date now = new Date();
        String startTime = DateUtils.getDayBefore(DateUtils.getSecondDate(now), 7);
        String endTime = DateUtils.getDayBefore(DateUtils.getSecondDate(now), 1);
        Map params = new HashMap<>();
        // 获取近七天的开始时间和结束时间
        params.put("startTime", startTime + " 00:00:00");
        params.put("endTime", endTime + " 23:59:59");

        List<ModelBeanMap> dataList = examInfoService.countByDateTime(params);
        Map dataTimeMap = new HashMap<>();
        dataList.forEach(data -> {
            String day = data.getStr("day_group");
            Integer examNumber = data.getInt("exam_number");
            dataTimeMap.put(day, examNumber);
        });

        List<String> weekDateList = DateUtils.getSectionByOneDay(8);
        // 近七天日期集合
        weekDateList.remove(weekDateList.size() - 1); // 移除最后一天，也就是当天的日期
        List<ModelBeanMap> resultDataList = examInfoService.countByDateTime(params);
        weekDateList.forEach(day -> {
            ModelBeanMap item = new ModelBeanMap();
            item.put("day_group", day);
            item.put("exam_number", ObjectUtils.isNotEmpty(dataTimeMap.get(day)) ? dataTimeMap.get(day) : 0);
            resultDataList.add(item);
        });
        resultMap.put("examInfoData", resultDataList);
    }


    public Result getRegionInfoData() {
        List<ModelBeanMap> data = schoolService.getSchoolRegionInfo();
        return Result.success(data);
    }
}
