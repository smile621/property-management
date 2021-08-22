package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class UpdateSoftwareByIdRequest {
    @ApiModelProperty("软件id")
    @NotNull(message = "请传入软件id[id]")
    private Integer id;
    @ApiModelProperty("软件名称")
    @NotNull(message = "请输入软件名称[name]")
    private String name;//软件名称
    @ApiModelProperty("软件描述")
    @NotNull(message = "软件描述不为空[description]")
    @NotBlank(message = "软件描述不为空[description]")
    private String description;//软件描述
    private final static Integer parentId=3;
}
