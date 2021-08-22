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
public class ServiceBindDto {

    @ApiModelProperty("服务ID")
    @NotNull(message = "服务ID不能为空")
    private Integer assetId;

    @ApiModelProperty("要绑定设备ID")
    @NotNull(message = "要绑定设备ID不能为空")
    private Integer affiliateId;
}
