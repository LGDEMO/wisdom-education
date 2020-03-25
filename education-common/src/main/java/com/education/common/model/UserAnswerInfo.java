package com.education.common.model;

import lombok.Data;

@Data
public class UserAnswerInfo {

    private Integer id;
    private int mark; // 答题得分
    private String comment; //评语
    private Object userAnswer;

    private int correctStatus;

    // 答案附件
    private String[] enclosure = new String []{};
}
