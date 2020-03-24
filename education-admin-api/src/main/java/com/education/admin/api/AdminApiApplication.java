package com.education.admin.api;

import com.baidu.ueditor.ConfigManager;
import com.education.mapper.common.utils.FileUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;


/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 11:11
 */
@SpringBootApplication(scanBasePackages =
   {
       "com.education.admin.api",
       "com.education.common",
           "com.education.service"
       /*"com.education.init",
       "com.education.task",
       "com.education.webSocket"*/
   }
)
@MapperScan("com.education.mapper")
public class AdminApiApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(AdminApiApplication.class, args);
        Environment environment = applicationContext.getEnvironment();
        String uploadPath = environment.getProperty("file.uploadPath");
        FileUtils.setUploadPath(uploadPath);
        String configFileName = environment.getProperty("ueditor.configFileName");
        ConfigManager.setConfigFileName(configFileName);
    }

}
