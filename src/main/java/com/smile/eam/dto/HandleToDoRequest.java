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
public class HandleToDoRequest {
    @NotNull(message = "请传入待办id[id]")
    @ApiModelProperty("待办id")
    private Integer id;//待办id

    @NotNull(message = "请传入处理描述[handleDescription]")
    @ApiModelProperty("处理描述")
    private String handleDescription;//处理描述

    @NotNull(message = "请选择待办状态[status]")
    @ApiModelProperty("待办状态 5:待处理 6: 处理中 7: 处理完成 ")
    private Integer status;//待办状态

}
