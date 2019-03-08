package com.qhjys.springcloud.service;

import com.qhjys.springcloud.entities.Dept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "microservicecloud-dept")  //开启feign
@RequestMapping(value = "/provider/dept")
public interface DeptCilentService {

    @PostMapping(value = "/add")
    public void add(Dept dept);

    @GetMapping(value = "/get/{id}")
    public Dept get(@PathVariable("id") Long id);

    @GetMapping(value = "/list")
    public List<Dept> list();
}
