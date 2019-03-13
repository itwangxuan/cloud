package com.qhjys.springcloud.service;

import com.qhjys.springcloud.entities.Dept;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component  //必须加
public class DeptCilentServiceFallbackFactory implements FallbackFactory<DeptCilentService>{

    @Override
    public DeptCilentService create(Throwable throwable) {
        return new DeptCilentService() {

            @Override
            public boolean add(Dept dept) {
                return false;
            }

            @Override
            public Dept get(Long id) {
                Dept dept = new Dept();
                dept.setId(id);
                dept.setDName("没有对应信息，cunsumer客户端提供的降级信息，服务provider异常");
                return dept;
            }

            @Override
            public List<Dept> list() {
                return null;
            }
        };
    }
}
