package com.education.student.api;

import com.education.common.utils.FileUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages =
    {
        "com.education.student.api",
        "com.education.common",
        "com.education.service",
        "com.education.event"
    }
)
@MapperScan("com.education.mapper")
public class EducationStudentApiApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(EducationStudentApiApplication.class, args);
        Environment environment = applicationContext.getEnvironment();
        String uploadPath = environment.getProperty("file.uploadPath");
        FileUtils.setUploadPath(uploadPath);
    }
}
