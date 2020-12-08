package com.mbrickspan.mbpan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = "com.mbrickspan.mbpan.dao")
public class MbpanApplication {

    public static void main(String[] args) {
        SpringApplication.run(MbpanApplication.class, args);
    }

}
