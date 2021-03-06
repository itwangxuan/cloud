package com.qhjys.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient  //本服务启动后会自动注册进eureka服务中
//@EnableDiscoveryClient  //服务发现   --->  因为provider是服务提供者，此注解为自己注册自己访问，可注释
@EnableCircuitBreaker
public class ProviderApplication8001_hystrix {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication8001_hystrix.class, args);
    }

}
