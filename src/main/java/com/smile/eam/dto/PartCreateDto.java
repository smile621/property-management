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
public class PartCreateDto {

    @ApiModelProperty(value = "资产分类id")
    @NotNull(message = "分类id不能为空")
    @Min(value = 1,message = "分类Id需大于0")
    private int assetCategoryId;
    @NotNull(message = "购买路径id不能为空")
    @Min(value = 1,message = "购买路径id需大于0")
    @ApiModelProperty(value = "购买路径id")
    private int buyRouterId;
    @NotNull(message = "厂商id不能为空")
    @Min(value = 1,message = "厂商id需大于0")
    @ApiModelProperty(value = "厂商id")
    private int vendorId;
    @ApiModelProperty(value = "配件名")
    @NotNull(message = "配件名不能为空")
    private String name;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "价格")
    @NotNull(message = "配件价格不能为空")
    private String price;
    @ApiModelProperty(value = "购入时间")
    @NotNull(message = "购入时间不能为空")
    private String buyAt;
    @NotNull(message = "过保时间不能为空")
    @ApiModelProperty(value = "过保时间")
    private String expiredAt;

}
