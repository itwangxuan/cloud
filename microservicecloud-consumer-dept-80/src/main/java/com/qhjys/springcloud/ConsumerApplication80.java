package com.qhjys.springcloud;

import com.qhjys.myrule.MyselfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
//@EnableDiscoveryClient  //服务发现
@EnableEurekaClient  //服务注册
//在启动改微服务的时候就能去加载我们的自定义Ribbon配置类，从而使配置生效
@RibbonClient(name = "microservicecloud-dept", configuration = MyselfRule.class)  //注释掉以后不加载ribbon
public class ConsumerApplication80 {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication80.class, args);
    }

}
