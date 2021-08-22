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
public class BuyRouterCreateDto {

    @ApiModelProperty("购买途径名字")
    @NotNull(message = "购买途径名不能为空")
    private String name;        //创建购买途径的名字

    @ApiModelProperty("购买途径描述")
    @NotNull(message = "描述不能为空")
    private String description; //描述
}
