package com.qhjys.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.qhjys.springcloud")
public class ProviderApplication8001 {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication8001.class, args);
    }

}
