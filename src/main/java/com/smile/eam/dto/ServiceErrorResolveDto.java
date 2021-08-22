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
public class ServiceErrorResolveDto {

    @ApiModelProperty("服务Id")
    @NotNull(message = "服务ID不能为空")
    private Integer id;

    @ApiModelProperty("修复时间")
    @NotNull(message = "恢复时间不能为空")
    private String endAt;
}
