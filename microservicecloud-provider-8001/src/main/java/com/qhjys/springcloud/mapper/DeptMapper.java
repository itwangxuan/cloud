package com.qhjys.springcloud.mapper;

import com.qhjys.springcloud.entities.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptMapper {

    /**
     * 添加部门
     *
     * @param dept
     * @return
     */
    public void addDept(Dept dept);

    /**
     * 根据id查找部门
     *
     * @param id
     * @return
     */
    public Dept findById(@Param("id") Long id);

    /**
     * 查找所有部门
     *
     * @return
     */
    public List<Dept> findAll();
}
