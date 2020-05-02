package com.education.service.course;

import com.education.common.model.ModelBeanMap;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.mapper.course.QuestionInfoMapper;
import com.education.mapper.course.SubjectInfoMapper;
import com.education.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 19:51
 */
@Service
public class SubjectInfoService extends BaseService<SubjectInfoMapper> {

    @Autowired
    private QuestionInfoMapper questionInfoMapper;

    public ModelBeanMap findByNameAndGradeType(Map params) {
        return mapper.findByNameAndGradeType(params);
    }

    public ResultCode deleteById(ModelBeanMap subjectMap) {
        if (subjectMap.getBoolean("use_flag")) {
            return new ResultCode(ResultCode.FAIL, "该科目已开启,无法删除");
        }
        Integer id = subjectMap.getInt("id");
        Map questionInfo = questionInfoMapper.findBySubjectId(id); //sqlSessionTemplate.selectOne("questions.info.findBySubjectId", id);
        if (ObjectUtils.isNotEmpty(questionInfo)) {
            return new ResultCode(ResultCode.FAIL, "该科目已有试题在使用, 无法删除");
        }
        return super.deleteById(subjectMap);
    }

    public List<ModelBeanMap> findListByGradeType() {
        Map userInfo = getFrontUserInfo();
        Integer gradeType = (Integer) userInfo.get("grade_type");
        Map params = new HashMap<>();
        params.put("gradeType", gradeType);
        return mapper.queryList(params); //sqlSessionTemplate.selectList("system.subject.list", params);
    }

    @Override
    public ResultCode saveOrUpdate(boolean updateFlag, ModelBeanMap subjectMap) {
        if (ObjectUtils.isEmpty(subjectMap.get("id"))) {
            subjectMap.put("gradeType", subjectMap.get("grade_type"));
            Map gradeMap = mapper.findByNameAndGradeType(subjectMap);
            if (ObjectUtils.isNotEmpty(gradeMap)) {
                return new ResultCode(ResultCode.FAIL, "该年级科目已存在,请勿重复创建");
            }
        }
        subjectMap.remove("gradeType");
        return super.saveOrUpdate(updateFlag, subjectMap);
    }
}
