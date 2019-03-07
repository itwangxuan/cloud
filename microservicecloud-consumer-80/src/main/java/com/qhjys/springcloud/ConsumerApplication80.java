package com.qhjys.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient  //服务发现
public class ConsumerApplication80 {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication80.class, args);
    }

}
