package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorUpdateDto {
    @ApiModelProperty(value = "厂商id",example = "1",reference = "false")
    private int id;
    @ApiModelProperty(value = "厂商名称",example = "华为",reference = "false")
    @NotBlank(message = "厂商名不能为空")
    private String name;
    @ApiModelProperty(value = "描述",example = "华为天下第一",reference = "false")
    private String description;
    @ApiModelProperty(value = "厂商所在地",example = "月球",reference = "false")
    private String location;
}
