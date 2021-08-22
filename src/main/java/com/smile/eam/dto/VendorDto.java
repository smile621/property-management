package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorDto {

    @ApiModelProperty(value = "厂商id",example = "1",reference = "false")
    private int id;
    @ApiModelProperty(value = "厂商名称",example = "华为",reference = "false")
    @NotBlank(message = "厂商名不能为空")
    private String name;
    @ApiModelProperty(value = "描述",example = "华为天下第一",reference = "false")
    private String description;
    @ApiModelProperty(value = "厂商所在地",example = "月球",reference = "false")
    private String location;
    private VendorUserDto dto;//厂商联系人列表
}
