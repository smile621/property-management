package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisposeMalfunctionRequest {
    @NotNull(message = "请传入id[id]")
    private Integer id;

    @ApiModelProperty("处理描述")
    @NotNull(message = "请传入处理描述[repairDescription]")
    private String repairDescription;

    @ApiModelProperty("传数字[status]： 4:故障 6：处理中 7：处理完成 ")
    @NotNull(message = "请选择状态[status]")
    private Integer status;
}
