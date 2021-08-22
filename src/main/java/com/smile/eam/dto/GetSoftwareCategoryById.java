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
public class GetSoftwareCategoryById {
    @ApiModelProperty("父级id,请输入3")
    @NotNull(message = "请输入父级id[parentId]")
    private Integer parentId;//父级id
}
