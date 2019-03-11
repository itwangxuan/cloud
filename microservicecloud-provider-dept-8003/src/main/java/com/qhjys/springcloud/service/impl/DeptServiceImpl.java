package com.qhjys.springcloud.service.impl;

import com.qhjys.springcloud.entities.Dept;
import com.qhjys.springcloud.mapper.DeptMapper;
import com.qhjys.springcloud.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 添加部门
     *
     * @param dept
     * @return
     */
    public void add(Dept dept) {
        deptMapper.addDept(dept);
    }

    /**
     * 根据id查找部门
     *
     * @param id
     * @return
     */
    public Dept get(Long id) {
        Dept dept = deptMapper.findById(id);
        return dept;
    }

    /**
     * 查找所有部门
     *
     * @return
     */
    public List<Dept> list() {
        List<Dept> list = deptMapper.findAll();
        return list;
    }
}
