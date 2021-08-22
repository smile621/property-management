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
public class BindSoftwareRequest {
    @ApiModelProperty("软件资产id")
    @NotNull(message = "请传入软件资产id[assetId]")
    private Integer assetId;
    @ApiModelProperty("绑定设备id")
    @NotNull(message = "请传入要绑定的设备id[assetAffiliate]")
    private Integer assetAffiliate;
}
