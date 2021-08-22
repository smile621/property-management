package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorCreateRequest {
    @ApiModelProperty(value = "厂商名称", example = "华为", reference = "false")
    @NotBlank(message = "厂商名不能为空")
    private String name;

    @ApiModelProperty(value = "描述", example = "华为天下第一", reference = "false")
    @NotNull(message = "描述字段不能为null")
    private String description;

    @ApiModelProperty(value = "厂商所在地", example = "月球", reference = "false")
    private String location;

    @ApiModelProperty(value = "厂商联系人")
    private VendorUserDto vendorUserDto;
}
