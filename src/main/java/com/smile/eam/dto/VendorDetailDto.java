package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorDetailDto {

    @ApiModelProperty(value = "厂商id")
    private int id;
    @ApiModelProperty(value = "厂商名称",example = "华为",reference = "false")
    @NotBlank(message = "厂商名不能为空")
    private String name;
    @ApiModelProperty(value = "描述",example = "华为天下第一",reference = "false")
    private String description;
    @ApiModelProperty(value = "厂商所在地",example = "月球",reference = "false")
    private String location;
    @ApiModelProperty(value = "厂商联系人信息列表",example = "",reference = "false")
    private List<VendorUserDetailDto> vendorUser;
    @ApiModelProperty(value = "创建时间",example = "2055-5-5",reference = "false")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty(value = "更新时间",example = "2055-9-9",reference = "false")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
