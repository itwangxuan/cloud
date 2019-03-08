package com.qhjys.springcloud.service;

import com.qhjys.springcloud.entities.Dept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeptService {

    /**
     * 添加部门
     *
     * @param dept
     * @return
     */
    public void add(Dept dept);

    /**
     * 根据id查找部门
     *
     * @param id
     * @return
     */
    public Dept get(Long id);

    /**
     * 查找所有部门
     *
     * @return
     */
    public List<Dept> list();
}
