package com.qhjys.springcloud.controller;

import com.qhjys.springcloud.entities.Dept;
import com.qhjys.springcloud.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequestMapping(value = "/consumer/dept")
@RestController
public class DeptController_Consumer {

    private static final String REST_URL_PREFIS = "http://localhost:8001";

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/add")
    public Response add(Dept dept) {
        try {
            restTemplate.postForObject(REST_URL_PREFIS + "/provider/dept/add", dept, Boolean.class);
            return Response.create().success();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.create().error(e.getMessage());
        }
    }

    @GetMapping(value = "/get/{id}")
    public Response get(@PathVariable("id") Long id) {
        try {
            Dept dept = restTemplate.getForObject(REST_URL_PREFIS + "/provider/dept/get/" + id, Dept.class);
            return Response.create().body(dept);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.create().error(e.getMessage());
        }
    }

    @GetMapping(value = "/list")
    public Response list() {
        try {
            List<Dept> list = restTemplate.getForObject(REST_URL_PREFIS + "/provider/dept/list", List.class);
            return Response.create().body(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.create().error(e.getMessage());
        }
    }

}
