package com.qhjys.springcloud.controller;

import com.qhjys.springcloud.entities.Dept;
import com.qhjys.springcloud.service.DeptCilentService;
import com.qhjys.springcloud.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/consumer/dept")
@RestController
public class DeptController_Consumer {

    @Autowired
    private DeptCilentService deptCilentService;

    @PostMapping(value = "/add")
    public Response add(Dept dept) {
        boolean flag = this.deptCilentService.add(dept);
        if (flag) {
            return Response.create().success();
        }
        return Response.create().error();
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE) //json
    public Response get(@PathVariable("id") Long id) {
        try {
            Dept dept = this.deptCilentService.get(id);
            return Response.create().body(dept);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.create().error(e.getMessage());
        }
    }

    @GetMapping(value = "/list")
    public Response list() {
        try {
            List<Dept> list = this.deptCilentService.list();
            return Response.create().body(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.create().error(e.getMessage());
        }
    }

}
