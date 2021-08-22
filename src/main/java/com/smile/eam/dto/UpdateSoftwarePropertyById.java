package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateSoftwarePropertyById {
    @ApiModelProperty("资产编号")
    @NotNull(message = "请传入资产编号[id]")
    private Integer id;
}
