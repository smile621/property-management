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
public class DeleteSoftwareCategoryRequest {
    @ApiModelProperty("软件id")
    @NotNull(message = "请传入软件id[id]")
    private Integer id;
}
