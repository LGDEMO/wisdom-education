package com.education.common.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 学员信息实体类
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/4/20 21:22
 */
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
    private String loginName;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    private String password;
    private String encrypt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public Integer getGradeTypeId() {
        return gradeTypeId;
    }

    public void setGradeTypeId(Integer gradeTypeId) {
        this.gradeTypeId = gradeTypeId;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getSexId() {
        return sexId;
    }

    public void setSexId(Integer sexId) {
        this.sexId = sexId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }


}
