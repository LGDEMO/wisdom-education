
SET FOREIGN_KEY_CHECKS=0;
-- 课程信息表
DROP TABLE IF EXISTS `course_info`;
CREATE TABLE `course_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `grade_type` int(2) NOT NULL,
  `school_type` int(2) NOT NULL,
  `subject_id` int(11) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL COMMENT '课程编号',
  `sort` varchar(255) DEFAULT NULL COMMENT '排序',
  `parent_id` int(11) DEFAULT '0',
  `head_img` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4;

-- 课程试题关联表
DROP TABLE IF EXISTS `course_question_info`;
CREATE TABLE `course_question_info` (
  `sort` int(11) NOT NULL DEFAULT '0',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question_info_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `mark` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4;

-- 考试信息表
DROP TABLE IF EXISTS `exam_info`;
CREATE TABLE `exam_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `mark` int(11) NOT NULL DEFAULT '0' COMMENT '总得分',
  `test_paper_info_id` int(11) NOT NULL,
  `grade_type` int(11) NOT NULL,
  `subject_id` int(11) NOT NULL,
  `correct_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否批改',
  `type` int(2) NOT NULL DEFAULT '2' COMMENT '考试类型（1 错题组卷考试 2 年级试卷 3.入学测试）',
  `system_mark` int(2) NOT NULL DEFAULT '0' COMMENT '系统判分',
  `teacher_mark` int(2) NOT NULL DEFAULT '0' COMMENT '老师评分',
  `time` varchar(100) NOT NULL DEFAULT '0' COMMENT '考试耗时',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4;

-- 系统日志表
DROP TABLE IF EXISTS `http_request_log`;
CREATE TABLE `http_request_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `request_url` varchar(100) DEFAULT NULL,
  `method` varchar(50) DEFAULT NULL,
  `request_time` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `params` text,
  `ip` varchar(255) DEFAULT NULL,
  `exception` text,
  `platform_type` tinyint(2) DEFAULT NULL,
  `operation_desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20041 DEFAULT CHARSET=utf8mb4;

--  试题信息表
DROP TABLE IF EXISTS `question_info`;
CREATE TABLE `question_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) NOT NULL COMMENT '课程名称',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `video_url` varchar(1000) DEFAULT NULL,
  `answer` text COMMENT '答案',
  `content` text NOT NULL COMMENT '试题内容',
  `school_type` int(2) NOT NULL COMMENT '阶段id',
  `question_type` int(2) NOT NULL COMMENT '试题类型',
  `grade_type` int(2) NOT NULL,
  `options` varchar(2000) DEFAULT NULL COMMENT '试题选项（多个以逗号隔开）',
  `analysis` text COMMENT '试题解析内容',
  `summarize` text COMMENT '总结升华',
  `category` int(2) NOT NULL COMMENT '试题类型（1 课程训练试题 2 考试试题）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1713 DEFAULT CHARSET=utf8mb4;

--  学校信息表
DROP TABLE IF EXISTS `school_info`;
CREATE TABLE `school_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '学校名称',
  `simplicity` varchar(100) DEFAULT NULL COMMENT '学校简称',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `mobile` varchar(100) NOT NULL COMMENT '联系方式',
  `principal_name` varchar(100) NOT NULL,
  `school_type` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `lng` varchar(255) DEFAULT NULL,
  `lat` varchar(255) DEFAULT NULL,
  `province_code` varchar(255) DEFAULT NULL,
  `city_code` varchar(255) DEFAULT NULL,
  `county_code` varchar(255) DEFAULT NULL,
  `town_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4;

--  学员信息表
DROP TABLE IF EXISTS `student_info`;
CREATE TABLE `student_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `age` int(11) NOT NULL,
  `sex` tinyint(2) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `mobile` varchar(100) NOT NULL,
  `school_id` int(11) DEFAULT NULL,
  `mother_name` varchar(100) DEFAULT NULL COMMENT '母亲姓名',
  `father_name` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `head_img` varchar(255) DEFAULT NULL COMMENT '头像',
  `grade_type` int(11) DEFAULT NULL,
  `delete_flag` int(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `is_experience` tinyint(1) NOT NULL DEFAULT '0',
  `password` varchar(255) DEFAULT NULL,
  `encrypt` varchar(100) DEFAULT NULL,
  `disabled_flag` tinyint(1) NOT NULL DEFAULT '0',
  `login_count` int(11) NOT NULL DEFAULT '0',
  `last_login_time` datetime DEFAULT NULL,
  `login_ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for student_question_answer
-- ----------------------------
DROP TABLE IF EXISTS `student_question_answer`;
CREATE TABLE `student_question_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `question_info_id` int(11) NOT NULL,
  `answer` varchar(1000) DEFAULT NULL,
  `is_right` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否正确',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `test_paper_info_id` int(11) DEFAULT NULL,
  `enclosure` varchar(1000) DEFAULT NULL,
  `mark` int(11) NOT NULL DEFAULT '0' COMMENT '答题得分',
  `comment` varchar(255) DEFAULT NULL,
  `question_points` int(11) NOT NULL DEFAULT '0' COMMENT '试题分数',
  `correct_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已纠正',
  `course_id` int(11) NOT NULL DEFAULT '0',
  `correct_status` tinyint(2) DEFAULT '0' COMMENT '批改状态 0 错误 1 正确 2 待批改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8mb4;



-- 科目信息表
DROP TABLE IF EXISTS `subject_info`;
CREATE TABLE `subject_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人',
  `school_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '阶段（1 小学 2. 初中 3. 高中)',
  `use_flag` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `grade_type` int(11) NOT NULL COMMENT '所属年级id',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `cover_img` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4;

-- 系统管理员表
DROP TABLE IF EXISTS `system_admin`;
CREATE TABLE `system_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) NOT NULL COMMENT '登录名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `encrypt` varchar(255) NOT NULL COMMENT '密码加密hash',
  `create_date` datetime DEFAULT NULL,
  `disabled_flag` tinyint(1) DEFAULT '0' COMMENT '是否禁用',
  `login_ip` varchar(255) DEFAULT NULL COMMENT '登录ip',
  `login_count` int(11) DEFAULT '0' COMMENT '登录次数',
  `school_id` int(11) DEFAULT NULL COMMENT '部门id （学校id)',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  `create_type` tinyint(2) DEFAULT '2' COMMENT '创建类型 （1 系统默认 2. 管理员创建)',
  `mobile` varchar(255) DEFAULT NULL COMMENT '手机号',
  `super_flag` tinyint(1) DEFAULT '0' COMMENT '是否超级管理员',
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `system_admin_role`;
CREATE TABLE `system_admin_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=716 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_dict
-- ----------------------------
DROP TABLE IF EXISTS `system_dict`;
CREATE TABLE `system_dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `value` varchar(100) DEFAULT NULL,
  `code` int(11) NOT NULL COMMENT '标识',
  `sort` int(11) DEFAULT NULL,
  `parent_id` int(11) DEFAULT '0',
  `alias` varchar(255) DEFAULT NULL COMMENT '别名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operation_name` varchar(255) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `operation_ip` varchar(255) DEFAULT NULL,
  `operation_desc` varchar(500) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `platform_type` tinyint(2) DEFAULT NULL COMMENT '类型（1 系统后台 2. 学生端）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1436 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `parent_id` int(64) DEFAULT '0',
  `permissions` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `sort` int(11) DEFAULT '0',
  `del_flag` char(1) DEFAULT '0',
  `type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '类型（1菜单 2. 按钮)',
  `create_type` tinyint(2) DEFAULT '1' COMMENT '创建类型（1 系统内置 2. 管理员创建)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_message_info
-- ----------------------------
DROP TABLE IF EXISTS `system_message_info`;
CREATE TABLE `system_message_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `course_id` int(11) NOT NULL COMMENT '0',
  `test_paper_info_id` int(11) NOT NULL DEFAULT '0',
  `read_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for system_region
-- ----------------------------
DROP TABLE IF EXISTS `system_region`;
CREATE TABLE `system_region` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_code` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5851 DEFAULT CHARSET=utf8mb4;


-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `create_type` tinyint(2) DEFAULT '2' COMMENT '创建类型 （1 系统创建 2.管理员创建） ',
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu` (
  `menu_id` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for test_paper_info
-- ----------------------------
DROP TABLE IF EXISTS `test_paper_info`;
CREATE TABLE `test_paper_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `mark` int(11) NOT NULL DEFAULT '0' COMMENT '试卷总分',
  `school_type` int(11) NOT NULL,
  `grade_type` int(11) NOT NULL,
  `publish_flag` tinyint(1) DEFAULT '0' COMMENT '是否发布（此字段暂时无效）',
  `subject_id` int(11) NOT NULL,
  `sort` int(11) NOT NULL DEFAULT '0',
  `exam_time` int(11) NOT NULL COMMENT '考试时间',
  `exam_number` int(11) NOT NULL DEFAULT '0' COMMENT '考试人数',
  `type` int(11) DEFAULT NULL,
  `correct_number` int(11) DEFAULT '0' COMMENT '已批改试卷数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for test_paper_question_info
-- ----------------------------
DROP TABLE IF EXISTS `test_paper_question_info`;
CREATE TABLE `test_paper_question_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question_info_id` int(11) NOT NULL,
  `test_paper_info_id` int(11) NOT NULL,
  `mark` int(11) NOT NULL DEFAULT '0',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `sort` int(4) NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=utf8mb4;
