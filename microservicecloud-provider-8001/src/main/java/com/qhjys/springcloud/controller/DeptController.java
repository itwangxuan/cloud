package com.qhjys.springcloud.controller;

import com.qhjys.springcloud.entities.Dept;
import com.qhjys.springcloud.service.DeptService;
import com.qhjys.springcloud.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping(value = "/add")
    public Response add(Dept dept){
        try {
            deptService.add(dept);
            return Response.create().success();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.create().error(e.getMessage());
        }
    }

    @GetMapping(value = "/get")
    public Response get(Long id){
        try {
            Dept dept = deptService.get(id);
            return Response.create().body(dept);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.create().error(e.getMessage());
        }

    }

    @GetMapping(value = "/list")
    public Response list(){
        try {
            List<Dept> list = deptService.list();
            return Response.create().body(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.create().error(e.getMessage());
        }

    }


}
