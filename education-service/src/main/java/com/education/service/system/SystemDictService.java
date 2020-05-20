package com.education.service.system;


import com.education.common.exception.BusinessException;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.Result;
import com.education.common.utils.ResultCode;
import com.education.mapper.system.SystemDictMapper;
import com.education.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 21:16
 */
@Service
public class SystemDictService extends BaseService<SystemDictMapper> {

    public static final String GRADE_TYPE = "grade_type";
    @Autowired
    private SystemDictValueService systemDictValueService;

    public Result saveOrUpdate(ModelBeanMap params) {
        boolean updateFlag = false;
        if (ObjectUtils.isNotEmpty(params.get("id"))) {
            updateFlag = true;
        }
        return super.saveOrUpdate(updateFlag, params);
    }


    @Transactional
    public Result deleteDictById(ModelBeanMap dictBeanMap) {
        try {
            super.deleteById(dictBeanMap.getInt("id"));
            systemDictValueService.deleteByDictId(dictBeanMap.getInt("id"));
            return Result.success(ResultCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除字典数据失败", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "删除字典数据失败"));
        }
    }
}
