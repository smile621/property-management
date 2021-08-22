package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
@Data
public class SoftwareStatisticRequest {
    @ApiModelProperty("当前年份year")
    @NotNull(message = "请传入当前年份[year]")
    private Integer year;//年份
}
