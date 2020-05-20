package com.education.service.course;


import com.education.common.model.ModelBeanMap;
import com.education.mapper.course.ExamInfoMapper;
import com.education.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 19:44
 */
@Service
public class ExamInfoService extends BaseService<ExamInfoMapper> {

    public List<ModelBeanMap> countByDateTime(Map params) {
        return mapper.countByDateTime(params);
    }
}
