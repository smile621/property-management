package com.smile.eam.dto;

import io.swagger.annotations.*;
import lombok.*;
import org.apache.ibatis.annotations.Options;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareCreateRequest {
    @ApiModelProperty("软件名称")
    @NotNull(message = "请输入软件名称[name]")
    private String name;//软件名称

    @ApiModelProperty("软件描述")
//    @NotNull(message = "软件描述不为空[description]")
    private String description;//软件描述
    @ApiModelProperty("软件名称")
    @NotNull(message = "请输入软件父类id[parentId]")
    private  Integer parentId;
}
