package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteToDoRequest {
    @NotNull(message = "请传入待办id[id]")
    @ApiModelProperty("待办id")
    private Integer id;//请传入待办id

}
