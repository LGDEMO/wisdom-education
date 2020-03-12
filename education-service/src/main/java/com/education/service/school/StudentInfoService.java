package com.education.service.school;

import com.education.common.constants.Constants;
import com.education.common.constants.EnumConstants;
import com.education.common.exception.BusinessException;
import com.education.common.model.*;
import com.education.common.model.online.OnlineUser;
import com.education.common.model.online.OnlineUserManager;
import com.education.common.utils.*;
import com.education.mapper.course.ExamInfoMapper;
import com.education.mapper.course.StudentQuestionAnswerMapper;
import com.education.mapper.course.TestPaperInfoMapper;
import com.education.mapper.school.StudentInfoMapper;
import com.education.service.BaseService;
import com.education.service.WebSocketMessageService;
import com.education.service.course.QuestionInfoService;
import com.education.task.BaseTask;
import com.education.task.StudentMessageTask;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.*;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/9 15:36
 */
@Service
public class StudentInfoService extends BaseService<StudentInfoMapper> {

    @Autowired
    private QuestionInfoService questionService;
    @Autowired
    private StudentQuestionAnswerMapper studentQuestionAnswerMapper;
    @Autowired
    private ExamInfoMapper examInfoMapper;
    @Autowired
    private TestPaperInfoMapper testPaperInfoMapper;
    @Autowired
    private JwtToken frontJwtToken;
    @Autowired
    private WebSocketMessageService webSocketMessageService;
    @Autowired
    private OnlineUserManager onlineUserManager;


    private static final String STUDENT_EXCEL_TITLE[] = new String[]{
            "学生姓名", "头像", "就读学校", "性别", "年龄", "年级", "家庭住址", "联系电话", "父亲姓名", "母亲姓名"
    };

    @Override
    public ResultCode deleteById(ModelBeanMap studentInfoMap) {
        try {

        } catch (Exception e) {

        }
        return super.deleteById(studentInfoMap.getInt("id"));
    }

    public ResultCode exportExcel(Map params)  {
        List<String> column = new ArrayList<>();
        column.add("name");
        column.add("head_img");
        column.add("school_name");
        column.add("sex");
        column.add("age");
        column.add("grade_type");
        column.add("address");
        column.add("mobile");
        column.add("father_name");
        column.add("mother_name");
        int width[] = {10000, 10000, 9000, 5000, 7000, 10000, 5000, 15000, 5000, 5000};
        String title = "学员信息表";
        AdminUserSession adminUserSession = getAdminUserSession();
        if (adminUserSession.isPrincipalAccount()) {
            params.put("schoolId", adminUserSession.getUserMap().get("school_id"));
        }
        List<Map> dataList = mapper.queryList(params); //sqlSessionTemplate.selectList("student.info.list", params);
        dataList.forEach(student -> {
            Integer gradeType = (Integer) student.get("grade_type");
            String gradeName = null; // BaseTask.getGradeName(gradeType);
            student.put("grade_type", gradeName);
            Integer sex = (Integer) student.get("sex");
            student.put("sex", sex == EnumConstants.Sex.MAN.getValue() ? "男" : "女");
        });
        HSSFWorkbook hssfWorkbook = ExcelKit.export(dataList, column, width, STUDENT_EXCEL_TITLE, title);
        return ExcelKit.exportByAjax("学员信息表.xls", RequestUtils.getResponse(), hssfWorkbook);
    }

    public ResultCode importStudentFromExcel(List<StudentInfo> dataList) throws Exception {

        return new ResultCode(ResultCode.SUCCESS, "数据导入失败");
    }

    @Transactional
    public ResultCode saveOrUpdate(boolean updateFlag, ModelBeanMap studentInfoMap) {
        try {
            int result = 0;
            String message = "";
            if (updateFlag) {
                message = "修改学员成功";
                result = super.update(studentInfoMap);
            } else {
                message = "添加学员成功";
                result = super.save(studentInfoMap);
            }
            if (result > 0) {
                return new ResultCode(ResultCode.FAIL, message);
            }
        } catch (Exception e) {
            logger.error("操作异常", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "操作异常"));
        }
        return new ResultCode(ResultCode.FAIL, "操作异常");
    }

    public List<ModelBeanMap> getStudentCourseOrPaperQuestionInfoList(Map params) {
        try {
            List<ModelBeanMap> userAnswerQuestionList = studentQuestionAnswerMapper.getStudentCourseOrPaperQuestionInfoList(params); //sqlSessionTemplate.selectList("user.question.answer.modeQuestion.list", params);
            int number = 1;
            for (ModelBeanMap userAnswer : userAnswerQuestionList) {
                questionService.parseQuestionOptions(userAnswer);
                int questionType = (Integer) userAnswer.get("question_type");
                if (questionType == EnumConstants.QuestionType.MULTIPLE_QUESTION.getValue()) {
                    String userAnswerStr = (String) userAnswer.get("user_answer");
                    if (ObjectUtils.isNotEmpty(userAnswerStr)) {
                        userAnswer.put("user_answer", ObjectUtils.spilt(userAnswerStr));
                    }
                }
                String questionTypeName = EnumConstants.QuestionType.getName(questionType);
                userAnswer.put("questionTypeName", questionTypeName);

                String enclosure = (String) userAnswer.get("enclosure");
                List<String> enclosureList = new ArrayList<>();
                if (ObjectUtils.isNotEmpty(enclosure)) {
                    enclosureList = Arrays.asList(ObjectUtils.spilt(enclosure));
                }
                userAnswer.put("enclosureList", enclosureList);
                userAnswer.put("content", String.valueOf(number) + "、 " + userAnswer.get("content"));
                number++;
            }
            return userAnswerQuestionList;
        } catch (Exception e) {
            logger.error("获取试卷试题异常", e);
        }
        return null;
    }

    @Transactional
    public Map correctStudentQuestionAnswer(ModelBeanMap params) {
        try {
            Integer studentId = (Integer) params.get("studentId");
            Integer modeId = (Integer) params.get("modeId");
            Integer testPaperId = (Integer) params.get("testPaperId");
            List<ModelBeanMap> studentQuestionList = params.getModelBeanMapList("studentQuestionList");
            Map data = new HashMap<>();
            int mark = 0;
            String examName = null;
            for (ModelBeanMap question : studentQuestionList) {
                Integer questionType = (Integer) question.get("question_type");
                if (questionType == EnumConstants.QuestionType.FILL_QUESTION .getValue()
                        || questionType == EnumConstants.QuestionType.CALCULATION_QUESTION.getValue()
                        || questionType == EnumConstants.QuestionType.SYNTHESIS_QUESTION.getValue()
                        || questionType == EnumConstants.QuestionType.INDEFINITE_ITEM_QUESTION.getValue()) {
                    data.put("studentId", studentId);
                    data.put("modeId", modeId);
                    data.put("questionInfoId", question.get("id"));
                    data.put("mark", question.get("answer_mark"));
                    data.put("question_points", question.get("mark"));
                    data.put("is_right", question.get("is_right"));
                    mark += question.getInt("answer_mark");
                    data.put("comment", question.get("comment"));
                    data.put("test_paper_info_id", question.get("testPaperId"));
                    studentQuestionAnswerMapper.updateStudentQuestionMark(data);
                } else {
                    mark += question.getInt("answer_mark");
                }
            }

            // 更新考试记录分数
            if (testPaperId > 0) {
                data.clear();
                data.put("testPaperId", testPaperId);
                data.put("studentId", studentId);
                Map examInfo = examInfoMapper.findByPaperIdAndStudentId(data); //sqlSessionTemplate.selectOne("exam.info.findByPaperId", data);
                if (ObjectUtils.isNotEmpty(examInfo) && (boolean)examInfo.get("correct_flag")) {
                    params.put("code", ResultCode.FAIL);
                    params.put("message", "该试卷已批改,请勿重复提交");
                    return params;
                }
                Integer examMark = (Integer) examInfo.get("mark");
                mark += examMark;
                data.clear();
                data.put("id", examInfo.get("id"));
                data.put("mark", mark);
                data.put("correct_flag", true);
                examInfoMapper.update(data);
            }
            // 发送答题成绩分数模板消息
            BaseTask studyTemplateMsg = new StudentMessageTask();
            studyTemplateMsg.put("student_id", studentId);
            studyTemplateMsg.put("mark", mark);
            studyTemplateMsg.put("admin_id", getAdminUser().get("id"));
            ModelBeanMap paperInfo = testPaperInfoMapper.findById(testPaperId);
            studyTemplateMsg.put("content", "您参加的考试【" + paperInfo.getStr("name") + "】已被管理员" + getAdminUser().get("login_name") + "批改，快去查看吧");
            studyTemplateMsg.put("create_date", new Date());
            studyTemplateMsg.put("test_paper_info_id", testPaperId);
            taskManager.execute(studyTemplateMsg);
            params.put("code", ResultCode.SUCCESS);
            params.put("message", "操作成功, 该学员本次共得到" + mark + "分");
            return params;
        } catch (Exception e) {
            params.clear();
            params.put("code", ResultCode.FAIL);
            params.put("message", "批改试题异常");
            logger.error("批改试题异常", e);
            throw new BusinessException(new ResultCode(ResultCode.FAIL, "批改试题异常"));
        }
    }

    /**
     * 修改学员密码
     * @param params
     * @return
     */
    public ResultCode resettingFrontUserPassword(Map params) {
        try {
            String newPassword = (String)params.get("newPassword");
            String password = (String)params.get("password");
            String confirmPassword = (String)params.get("confirmPassword");
            if (!newPassword.equals(confirmPassword)) {
                return new ResultCode(ResultCode.FAIL, "密码与确认密码不一致");
            }
            Map userInfo = getFrontUserInfo();
            String encrypt = (String)userInfo.get("encrypt");
            password = Md5Utils.getMd5(password, encrypt);
            String userPassword = (String)userInfo.get("password");
            if (!password.equals(userPassword)) {
                return new ResultCode(ResultCode.FAIL, "密码输入错误");
            }
            password = Md5Utils.getMd5(newPassword, encrypt);
            Integer userInfoId = (Integer) params.get("id");
            params.clear();
            params.put("password", password);
            params.put("id", userInfoId);
            mapper.update(params);
            return new ResultCode(ResultCode.SUCCESS, "密码修改成功, 退出后请用新密码登录");
        } catch (Exception e) {
            logger.error("密码修改失败", e);
        }
        return new ResultCode(ResultCode.SUCCESS, "密码修改失败");
    }

    public Result<ModelBeanMap> getPaperHistory(Map params) {
        try {
            Integer studentId = (Integer) getFrontUserInfo().get("student_id");
            params.put("studentId", studentId);
            return super.pagination(params, StudentQuestionAnswerMapper.class,
                    StudentQuestionAnswerMapper.GET_STUDENT_ANSWER_PAPER_LIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Result getStudentErrorQuestionList(Map params) {
        params.put("studentId", getFrontUserInfo().get("student_id"));
        return super.pagination(params, StudentQuestionAnswerMapper.class,
                StudentQuestionAnswerMapper.GET_STUDENT_ERROR_QUESTION_LIST);
    }

    public Result doLogin(Map params) {
        String loginName = (String) params.get("userName");
        ModelBeanMap studentInfoMap = mapper.findByLoginName(loginName);
        Map resultMap = new HashMap<>();
        Result result = new Result(ResultCode.SUCCESS, "登录成功");
        if (ObjectUtils.isEmpty(studentInfoMap)) {
            result.setCode(ResultCode.FAIL);
            result.setMessage("用户不存在");
            return result;
        } else {
            if (studentInfoMap.getBoolean("disabled_flag")) {
                result.setCode(ResultCode.FAIL);
                result.setMessage("账号已被禁用");
                return result;
            }
            String password = (String) params.get("password");
            boolean rememberMe = (boolean) params.get("checked"); // 是否记住密码
            String dataBasePassword = (String) studentInfoMap.get("password");
            String encrypt = (String) studentInfoMap.get("encrypt");
            if (dataBasePassword.equals(Md5Utils.getMd5(password, encrypt))) {
                this.setFrontUserInfoSession(studentInfoMap, rememberMe, resultMap);
                result.setData(resultMap);
            } else {
                result.setCode(ResultCode.FAIL);
                result.setMessage("用户名或密码错误");
            }
            return result;
        }
    }

    private String getOrCreateSessionId(boolean rememberMe) {
        return getOrCreateSessionId(Constants.SESSION_NAME, rememberMe);
    }

    public String getOrCreateSessionId(String cookieName, boolean rememberMe) {
        Cookie cookie = RequestUtils.getCookie(cookieName);
        if (ObjectUtils.isNotEmpty(cookie) && rememberMe) {
            if (rememberMe) {
                int maxAge = cookie.getMaxAge();
                if (maxAge < Constants.SESSION_TIME_OUT) {
                    RequestUtils.createCookie(cookieName, cookie.getValue(), 0);  // 先删除这个cookie
                    return this.createNewCookie(cookieName, true); // 然后创建新的同名cookie
                }
            }
            return cookie.getValue();
        }
        String sessionId = NumberUtils.getUUID();
        int timeOut = Constants.DEFAULT_SESSION_TIME_OUT; // 默认会话cookie关闭浏览器时过期
        if (rememberMe) {
            timeOut = Constants.SESSION_TIME_OUT;
        }
        RequestUtils.createCookie(cookieName, sessionId, timeOut);
        return sessionId;
    }

    private String createNewCookie(String cookieName, boolean rememberMe) {
        String sessionId = NumberUtils.getUUID();
        int timeOut = Constants.DEFAULT_SESSION_TIME_OUT; // 默认会话cookie关闭浏览器时过期
        if (rememberMe) {
            timeOut = Constants.SESSION_TIME_OUT;
        }
        RequestUtils.createCookie(cookieName, sessionId, timeOut);
        return sessionId;
    }

    public void logout() {
        FrontUserInfoSession userInfoSession = getFrontUserInfoSession();
        if (ObjectUtils.isEmpty(userInfoSession)) {
            return;
        }
     //   Integer studentId = (Integer) userInfoSession.getUserInfoMap().get("student_id");
    //    String studentName = (String) userInfoSession.getUserInfoMap().get("student_name");
       // onlineUserManager.removeOnlineUser(userInfoSession.getUserId());
        // 推送模板消息
     //   BaseTask<Map> studyTemplateMsg = new StudyTemplateMsg(sqlSessionTemplate);
      /*  studyTemplateMsg.put("studentId", studentId);
        studyTemplateMsg.put("student_name", studentName);
        studyTemplateMsg.put("login_status", EnumConstants.LoginStatus.LOGIN_OUT.getValue());
        studyTemplateMsg.put("templateId", Constants.STUDY_STATUS_TEMPLATE_MESSAGE_ID);
        taskManager.execute(studyTemplateMsg);*/
        RequestUtils.clearCookie(Constants.SESSION_NAME);
        ehcacheBean.remove(Constants.USER_INFO_CACHE, userInfoSession.getSessionId()); // 删除用户缓存
    }


    private void setFrontUserInfoSession(Map userInfoMap, boolean rememberMe, Map resultMap) {
        FrontUserInfoSession userInfoSession = new FrontUserInfoSession();
        userInfoSession.setUserInfoMap(userInfoMap);
        String sessionId = getOrCreateSessionId(rememberMe);
        userInfoSession.setSessionId(sessionId);
        ehcacheBean.put(Constants.USER_INFO_CACHE, sessionId, userInfoSession);
        String token = frontJwtToken.createToken(userInfoMap.get("student_id"), Constants.SESSION_TIME_OUT * 60 * 1000); // 默认缓存5天
        Map userCacheMap = new HashMap();
        Integer gradeType = (Integer) userInfoMap.get("grade_type");
        String gradeName = BaseTask.getGradeName(gradeType); //BaseTask.getGradeName(gradeType);
        userCacheMap.put("gradeName", gradeName);
        userCacheMap.put("gradeType", gradeType);
        userCacheMap.put("school_name", userInfoMap.get("school_name"));
        userCacheMap.put("sex", userInfoMap.get("sex"));
        userCacheMap.put("name", userInfoMap.get("student_name"));
        userCacheMap.put("head_img", userInfoMap.get("head_img"));
        userCacheMap.put("studentId", userInfoMap.get("student_id"));
        resultMap.put("code", ResultCode.SUCCESS);
        resultMap.put("token", token);
        resultMap.put("sessionId", sessionId);
        resultMap.put("message", "登录成功");
        resultMap.put("userInfo", userCacheMap);
        webSocketMessageService.checkOnlineUser(userInfoSession.getUserId(), EnumConstants.PlatformType.WEB_FRONT);
        OnlineUser nowOnlineUser = new OnlineUser(userInfoSession.getUserId(), sessionId, EnumConstants.PlatformType.WEB_FRONT);
        nowOnlineUser.setFrontUserInfoSession(userInfoSession);
        onlineUserManager.addOnlineUser(userInfoSession.getUserId(), nowOnlineUser);
        // 更新用户登录信息
        Map loginLog = new HashMap<>();
        Date now = new Date();
        loginLog.put("last_login_time", now);
        loginLog.put("login_ip", IpUtils.getAddressIp(RequestUtils.getRequest()));
        int loginCount = (int) userInfoMap.get("login_count");
        loginLog.put("login_count", ++loginCount);
        loginLog.put("update_date", now);
        loginLog.put("id", userInfoMap.get("student_id"));
        mapper.update(loginLog);
    }
}
