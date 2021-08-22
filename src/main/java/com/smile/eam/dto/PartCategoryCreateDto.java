package com.smile.eam.dto;

import com.smile.eam.entity.Asset;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartCategoryCreateDto {

    @NotBlank(message = "分类名字段不能为空")
    @ApiModelProperty(value = "配件名")
    private String name;
    @ApiModelProperty(value = "描述")
    private String description = "";
    private int parentId = Asset.CATEGORY_ID_PART;
    @ApiModelProperty(value = "折旧id")
    private int depreciationId;
}
