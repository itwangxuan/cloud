package com.qhjys.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient  //服务注册
@EnableFeignClients(basePackages = {"com.qhjys.springcloud"})
@ComponentScan("com.qhjys.springcloud")
public class ConsumerApplicationFeign {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplicationFeign.class, args);
    }

}
