package com.qhjys.springcloud.controller;

import com.qhjys.springcloud.entities.Dept;
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

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/add")
    public Response add(Dept dept) {
        deptService.add(dept);
        return Response.create().success();
    }

    @GetMapping(value = "/get/{id}")
    @ResponseBody
    public Dept get(@PathVariable("id") Long id) {
        Dept dept = deptService.get(id);
        return dept;
    }

    /**
     * Error while extracting response for type [java.util.List<com.qhjys.springcloud.entities.Dept>]
     * and content type [application/json;charset=UTF-8];
     * nested exception is org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error:
     * Cannot deserialize instance of `java.util.ArrayList` out of START_OBJECT token;
     * nested exception is com.fasterxml.jackson.databind.exc.MismatchedInputException: Cannot
     * deserialize instance of `java.util.ArrayList` out of START_OBJECT token at
     * [Source: (PushbackInputStream); line: 1, column: 1]
     */
//    @GetMapping(value = "/list")
//    public Response list(){
//        try {
//            List<Dept> list = deptService.list();
//            return Response.create().body(list);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.create().error(e.getMessage());
//        }
//    }

    @GetMapping(value = "/list")
    public List list() {
        List<Dept> list = deptService.list();
        return list;
    }

}
