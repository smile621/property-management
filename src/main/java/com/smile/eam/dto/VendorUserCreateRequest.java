package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorUserCreateRequest {

    @ApiModelProperty(value = "联系人姓名",example = "华为",reference = "false")
    @NotBlank(message = "联系人姓名不能为空")
    private String name;//联系人姓名

    @ApiModelProperty(value = "联系人职位",example = "华为",reference = "false")
    @NotBlank(message = "联系人职位不能为空")
    private String job;//联系人职位

    @ApiModelProperty(value = "厂商名称",example = "华为",reference = "false")
    @NotBlank(message = "厂商名不能为空")
    private String phone;//联系人电话

    @ApiModelProperty(value = "厂商名称",example = "华为",reference = "false")
    @NotBlank(message = "厂商名不能为空")
    @Email(message = "邮箱格式不正确，请重输")
    private String email;//联系人邮箱

}
