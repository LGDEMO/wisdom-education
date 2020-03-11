<p align="center">
  <img src="https://img.shields.io/badge/Avue-2.1.0-green.svg" alt="Build Status">
  <img src="https://img.shields.io/badge/Spring%20Cloud-Greenwich.SR3-blue.svg" alt="Coverage Status">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.1.9.RELEASE-blue.svg" alt="Downloads">
</p>
 
**Pig Microservice Architecture**   
   
- 基于 SpringBoot + Mybatis + Shiro + mysql + redis构建的智慧云智能教育平台 
- 基于数据驱动视图的理念封装 element-ui，即使没有 vue 的使用经验也能快速上手  
- 提供 lambda 、stream api 、webflux 的生产实践   


<a href="http://pig4cloud.com/doc/pig" target="_blank">部署文档</a> | <a target="_blank" href="http://avue.top"> 前端解决方案</a> | <a target="_blank" href="https://gitee.com/log4j/pig/releases/v1.3.2"> 1.0  版本</a> | <a target="_blank" href="http://pigx.pig4cloud.com"> PigX在线体验</a>
    
![](https://images.gitee.com/uploads/images/2019/0330/065147_85756aea_410595.png)

#### 快速构架微服务应用  

<img src="https://images.gitee.com/uploads/images/2019/0823/120112_98bb9619_410595.gif"/>  
   
#### 核心依赖 


依赖 | 版本
---|---
Spring Boot |  2.2.5.RELEASE  
Mybatis | 3.4.6  
Mysql | 5.7
Element-UI | 2.13.0

### 系统特色
支持填空题、综合题、选择题等多种试题类型的录入
支持数学公式的插入同时也支持通过excel 导入试题
支持系统自动评分、教师后台批阅学员试卷
可以将试卷试题导出word或者html,并且支持试题图片导出word
集成了百度地图和百度富文本编辑器
   
#### 模块说明
```lua
pig-ui  -- https://gitee.com/log4j/pig-ui

pig
├── education-admin-api -- 管理后台api模块
└── education-common -- 系统公共模块 
├── education-service -- 业务层模块[8888]
├── education-student-api -- 学生端api模块

	 
```
#### 提交反馈

1. 欢迎提交 issue，请写清楚遇到问题的原因，开发环境，复显步骤。

2. 不接受`功能请求`的 issue，功能请求可能会被直接关闭。  

3. <a href="mailto:pig4cloud@qq.com">pig4cloud@qq.com</a>    

4. <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2270033969&site=qq&menu=yes"> 个人QQ: 2270033969</a>

#### 开源协议


![](https://images.gitee.com/uploads/images/2019/0330/065147_e07bc645_410595.png)


