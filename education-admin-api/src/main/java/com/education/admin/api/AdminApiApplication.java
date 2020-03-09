package com.education.admin.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



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
   }
)
@MapperScan("com.education.mapper")
public class AdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApiApplication.class, args);
    }

}
