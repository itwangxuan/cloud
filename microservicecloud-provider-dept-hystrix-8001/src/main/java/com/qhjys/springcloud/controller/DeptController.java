package com.qhjys.springcloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.qhjys.springcloud.entities.Dept;
import com.qhjys.springcloud.exception.ValidationException;
import com.qhjys.springcloud.service.DeptService;
import com.qhjys.springcloud.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/provider/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping(value = "/get/{id}")
    //一旦调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中的制定方法
    @HystrixCommand(fallbackMethod = "get_hytrix")
    public Dept get(@PathVariable("id") Long id){
        Dept dept = deptService.get(id);
        if(null == dept){
            throw new ValidationException("没有对应信息");
        }
        return dept;
    }

    public Dept get_hytrix(@PathVariable("id") Long id){
        Dept dept = new Dept();
        dept.setId(id);
        dept.setDName("没有对应信息");
        return dept;
    }

}
