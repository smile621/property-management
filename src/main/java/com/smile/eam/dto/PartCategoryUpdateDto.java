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
public class PartCategoryUpdateDto {

    @ApiModelProperty(value = "分类id")
    private int categoryId;
    @NotNull
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "父分类id")
    private int parentId;
    @ApiModelProperty(value = "折旧id")
    private int depreciationId;
}
