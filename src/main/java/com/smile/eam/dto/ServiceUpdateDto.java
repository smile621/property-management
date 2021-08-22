package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceUpdateDto {

    @NotNull(message = "Id不能为空")
    private Integer id;

    @NotEmpty(message = "名字不能为空")
    private String name;

    @NotNull(message = "描述不能为空")
    private String description;

    @NotNull(message = "状态不能为空")
    private Integer status;

    @NotNull(message = "价格不能为空")
    @Pattern(regexp = "^\\d+(\\.\\d+)?", message = "请输入正确价格")
    private String price;

    @NotNull(message = "购入时间不能为空")
    private String buyAt;

    @NotNull(message = "厂商不能为空")
    private Integer vendorId;

    @NotNull(message = "过期时间不能为空")
    private String expiredAt;

    @NotNull(message = "购入途径不能为空")
    private Integer buyRouterId;
}
