package com.education.event.impl;

import com.education.common.model.GradeTypeInfo;
import com.education.event.BaseTask;
import com.education.common.constants.EnumConstants;
import com.education.common.exception.BusinessException;
import com.education.common.model.QuestionInfo;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.service.SystemDictManager;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 试题 Excel 导入
 * @author zengjintao
 * @create 2019/3/29 14:00
 * @since 1.0
 **/
public class ImportQuestionInfoTask<T> extends BaseTask {

    private static final Map<String, Map> subjectCache = new ConcurrentHashMap<>();
    private static final Map<String, Map> courseCache = new ConcurrentHashMap<>();
    private static final int CALCULATION_QUESTION = 6; // 计算题
    private static final Map<String, Integer> modeTypeMap = new HashMap() {
        {
            put("预习模式", 1);
            put("复习模式", 2);
            put("专题模式", 3);
            put("题库模式", 4);
        }
    };
    private static volatile boolean isRunning = true;

    public ImportQuestionInfoTask(SqlSessionTemplate sqlSessionTemplate, List<T> data, CountDownLatch countDownLatch) {
        super(sqlSessionTemplate, data, countDownLatch);
    }

    public ImportQuestionInfoTask() {

    }

    public static boolean isSuccess() {
        return isRunning;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    @Override
    public void run() {
        try {
            if (isRunning) {
                List<QuestionInfo> dataList = getData();
                logger.info(Thread.currentThread().getName() + "执行数据:" + dataList);
                final List<Map> data = new ArrayList<>();
                final List<String> languagePointsList = new ArrayList<>();
                for (QuestionInfo questionInfo : dataList) {
                    String languagePoints = questionInfo.getLanguagePoints();
                    if (ObjectUtils.isNotEmpty(questionInfo.getLanguagePoints())) {
                        languagePointsList.add(languagePoints);
                    }
                }
                List<Map> pointsList = sqlSessionTemplate.selectList("language.points.findByName", languagePointsList);
                Map<String, Integer> pointsMap = new HashMap();
                Date now = new Date();
                if (ObjectUtils.isNotEmpty(pointsList)) {
                    pointsList.forEach(item -> {
                        String name = (String) item.get("name");
                        Integer id = (Integer) item.get("id");
                        pointsMap.put(name, id);
                    });
                }
                for (QuestionInfo questionInfo : dataList) {
                    if (ObjectUtils.isEmpty(questionInfo.getContent())) {
                        continue;
                    }
                    Integer languagePointsId = pointsMap.get(questionInfo.getLanguagePoints());
                    String type = questionInfo.getQuestionType();
                    Map params = new HashMap<>();
                    params.put("mode_type", modeTypeMap.get(questionInfo.getMode()));
                    params.put("question_type", SystemDictManager.getQuestionTypeValue(type));
                    params.put("content", questionInfo.getContent());
                    int questionType = questionTypes.get(type);
                    String answer = questionInfo.getAnswer();
                    if (questionType == CALCULATION_QUESTION) {
                        params.put("answer", "对".equals(answer) ? Boolean.TRUE : Boolean.FALSE);
                    } else {
                        params.put("answer", answer);
                    }

                    params.put("options", questionInfo.getOptions());
                    params.put("language_points_id", languagePointsId);
                    params.put("analysis", questionInfo.getAnalysis());
                    String gradeName = questionInfo.getGradeInfoName();

                    GradeTypeInfo gradeTypeInfo = SystemDictManager.getGradeTypeValue(gradeName);
                    params.put("grade_type", gradeTypeInfo.getValue());
                    params.put("school_type", gradeTypeInfo.getSchoolType());
                    Integer subjectId = getSubjectId(questionInfo.getSubjectName(), (Integer)params.get("grade_type"));
                    params.put("subject_id", subjectId);
                    Integer courseId = getCourseName(questionInfo.getCourseName(), (Integer) params.get("grade_type"), subjectId);
                    params.put("course_id", courseId);
                    params.put("create_date", now);
                    params.put("update_date", now);
                    data.add(params);
                }
                sqlSessionTemplate.insert("questions.info.batchSave", data);
            }
        } catch (Exception e) {
            logger.error("导入数据失败", e);
            isRunning = false; // 终止其它线程再次执行写入数据库操作
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "导入失败, 请检查数据是否合法或文件格式是否正确"));
        } finally {
            logger.info(Thread.currentThread().getName() + "任务结束");
            countDownLatch.countDown();
        }
    }

    public static void setIsRunning(boolean isRunning) {
        ImportQuestionInfoTask.isRunning = isRunning;
    }

    private Integer getSubjectId(String subjectName, Integer gradeType) {
        if (ObjectUtils.isEmpty(subjectName))
            return null;
        Map subjectMap = subjectCache.get(subjectName);
        if (ObjectUtils.isEmpty(subjectMap)) {
            Map params = new HashMap<>();
            params.put("name", subjectName);
            params.put("gradeType", gradeType);
            synchronized (this) {
                subjectMap = subjectCache.get(subjectName);
                if (ObjectUtils.isEmpty(subjectMap)) {
                    subjectMap = sqlSessionTemplate.selectOne("system.subject.findByName", params);
                    if (ObjectUtils.isEmpty(subjectMap)) {
                        return null;
                    }
                    subjectCache.put(subjectName, subjectMap);
                }
            }
        }
        return (Integer)subjectMap.get("id");
    }

    private Integer getCourseName(String courseName, Integer gradeType, Integer subjectId) {
        if (ObjectUtils.isEmpty(courseName))
            return null;
        String key = courseName + String.valueOf(gradeType) + String.valueOf(subjectId);
        Map courseMap = courseCache.get(key);
        if (ObjectUtils.isEmpty(courseMap)) {
            Map params = new HashMap<>();
            params.put("courseName", courseName);
            params.put("gradeType", gradeType);
            params.put("subjectId", subjectId);
            synchronized (this) {
                courseMap = courseCache.get(courseName);
                if (ObjectUtils.isEmpty(courseMap)) {
                    courseMap = sqlSessionTemplate.selectOne("course.info.findByNameAndGradeAndSubject", params);
                    if (ObjectUtils.isEmpty(courseMap)) {
                        return null;
                    }
                    courseCache.put(key, courseMap);
                }
            }
        }
        return (Integer)courseMap.get("id");
    }
    private Integer getModeId(String modeTypeName) {
        return modeTypeMap.get(modeTypeName);
    }
}
