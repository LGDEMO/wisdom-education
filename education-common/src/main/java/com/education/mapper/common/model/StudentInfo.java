package com.education.mapper.common.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/4/20 21:22
 */
@Data
public class StudentInfo extends BaseExcelModel implements Serializable {

    @Excel(name = "学生姓名")
    @NotNull(message = "学生姓名不能为空")
    private String name;
    @Excel(name = "手机号")
    private String mobile;
    @Excel(name = "年龄")
    private int age;
    @Excel(name = "性别")
    private String sex;
    @Excel(name = "父亲姓名")
    private String fatherName;
    @Excel(name = "母亲姓名")
    private String motherName;
    @Excel(name = "家庭住址")
    private String address;
    @Excel(name = "就读学校")
    private String school;
    @Excel(name = "就读年级")
    @NotNull(message = "就读年级不能为空")
    private String gradeType;

    private Integer gradeTypeId;
    private Integer schoolId;
    private Integer sexId;
    private Date createDate;
    private Date updateDate;
}
