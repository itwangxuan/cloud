package com.qhjys.myrule;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyselfRule {

    @Bean
    public IRule myRule(){
//        return new RandomRule();//达到的目的：用我们重新选择的随机算法替代默认的轮循
        return new RandomRule_WX();
    }

}
