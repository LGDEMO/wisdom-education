package com.education.service.course;

import com.education.common.exception.BusinessException;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.ResultCode;
import com.education.mapper.course.CourseQuestionInfoMapper;
import com.education.service.BaseService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程试题管理service
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/10 22:09
 */
@Service
public class CourseQuestionService extends BaseService<CourseQuestionInfoMapper> {


    /**
     * 关联试题
     * @param modelBeanMap
     * @return
     */
    public ResultCode relevanceQuestion(ModelBeanMap modelBeanMap) {
        try {
            Integer courseId = (Integer)modelBeanMap.get("courseId");
            List<Integer> questionIds = modelBeanMap.getList("questionIds");
            List<ModelBeanMap> paramList = this.batchSaveCourseQuestionInfo(questionIds, courseId);
            modelBeanMap.clear();
            return new ResultCode(ResultCode.SUCCESS, paramList.size() + "道试题导入成功");
        } catch (Exception e) {
            logger.error("关联试题失败", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "试题导入失败"));
        }
    }

    private List<ModelBeanMap> batchSaveCourseQuestionInfo(List<Integer> questionIds, Integer courseId) {
        List<ModelBeanMap> paramList = new ArrayList<>();
        questionIds.forEach(questionId -> {
            ModelBeanMap data = new ModelBeanMap();
            data.put("question_info_id", questionId);
            data.put("course_id", courseId);
            paramList.add(data);
        });
        Map params = new HashMap<>();
        params.put("list", paramList);
        mapper.batchSave(params);
        return paramList;
    }
}
