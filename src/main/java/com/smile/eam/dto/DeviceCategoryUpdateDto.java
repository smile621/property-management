package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceCategoryUpdateDto {

    @ApiModelProperty(value = "设备id",example = "22",reference = "true")
    @NotNull(message = "设备id不能为空")
    private int id;
    @ApiModelProperty(value = "设备名称",example = "小米",reference = "true")
    @NotNull(message = "设备名称不能为空")
    private String name;
    @ApiModelProperty(value = "折旧id",example = "最省钱",reference = "false")
    private int depreciateId;
    @ApiModelProperty(value = "设备父级id",example = "2",reference = "false")
    private int parentId;
    @ApiModelProperty(value = "描述",example = "啧啧啧",reference = "false")
    private String description;

}
