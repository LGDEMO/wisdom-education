package com.education.common.model;

import com.education.common.constants.EnumConstants;
import com.education.common.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 试题类型分数实体类
 * @Auther: zengjintao
 * @Date: 2020/1/7 15:34
 * @Version:2.1.0
 */
public class QuestionTypeMarkInfo {

    private static final Map<Integer, Integer> QUESTION_TYPE_MARK_INFO = new HashMap() {
        {
           put(EnumConstants.QuestionType.SINGLE_QUESTION.getValue(), 3);
           put(EnumConstants.QuestionType.MULTIPLE_QUESTION.getValue(), 3);
           put(EnumConstants.QuestionType.FILL_QUESTION.getValue(), 5);
           put(EnumConstants.QuestionType.JUDGMENT_QUESTION.getValue(), 3);
           put(EnumConstants.QuestionType.CALCULATION_QUESTION.getValue(), 10);
           put(EnumConstants.QuestionType.INDEFINITE_ITEM_QUESTION.getValue(), 10);
        }
    };

    public static int getQuestionTypeMark(int questionType) {
        Integer mark = QUESTION_TYPE_MARK_INFO.get(questionType);
        if (ObjectUtils.isEmpty(mark)) {
            return 0;
        }
        return mark;
    }
}
