package com.smile.eam.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceErrorDto {

    @ApiModelProperty("服务ID")
    @NotNull(message = "服务ID不能为空")
    private Integer assetId;

    @ApiModelProperty("异常描述")
    @NotNull(message = "异常描述不能为空")
    private String description;

    @ApiModelProperty("异常开始时间")
    @NotNull(message = "异常开始时间不能为空")
    private String startAt;
}
