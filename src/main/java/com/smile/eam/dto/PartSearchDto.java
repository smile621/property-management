package com.smile.eam.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PartSearchDto {
    @ApiModelProperty(value = "模糊查询内容")
    @NotNull(message = "无模糊查询参数")
    private String title;
}
