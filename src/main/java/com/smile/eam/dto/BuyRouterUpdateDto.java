package com.smile.eam.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyRouterUpdateDto {


    @NotNull(message = "购入途径名不能为空")
    private String name;

    @NotNull(message = "id不能为空")
    private Integer id;

    @NotNull(message = "描述不能为空")
    private String description;
}
