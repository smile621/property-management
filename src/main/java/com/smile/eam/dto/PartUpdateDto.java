package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartUpdateDto {

    @ApiModelProperty(value = "资产编号id")
    @NotNull(message = "资产编号不能为空")
    private int assetId;
    @ApiModelProperty(value = "资产分类id")
    @NotNull(message = "分类id不能为空")
    @Min(value = 1,message = "分类Id需大于0")
    private int assetCategoryId;
    @ApiModelProperty(value = "购入路径id")
    private int buyRouterId;
    @ApiModelProperty(value = "厂商id")
    private int vendorId;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "照片")
    private String avatar;
    @ApiModelProperty(value = "价格")
    private String price;
    @ApiModelProperty(value = "购入时间")
    private String buyAt;
    @ApiModelProperty(value = "过保时间")
    private String expiredAt;

}
