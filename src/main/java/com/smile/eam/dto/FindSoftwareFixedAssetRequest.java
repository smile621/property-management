package com.smile.eam.dto;

import io.swagger.annotations.Api;
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
public class FindSoftwareFixedAssetRequest {
    @ApiModelProperty("分类id")
    @NotNull(message = "请输入分类id[assetCategoryId]")
    private Integer assetCategoryId=3;//分类id
    @ApiModelProperty("查找内容")
    @NotNull(message = "请输入要查找的内容[search]")
    private String search;

}
