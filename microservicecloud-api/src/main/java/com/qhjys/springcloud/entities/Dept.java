package com.qhjys.springcloud.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "部门表")
public class Dept implements Serializable{

    private static final long serialVersionUID = -3735419132495569189L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "部门名称")
    private String dName;

    @ApiModelProperty(value = "数据源")
    private String dbSource;
}
