package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCreateDto {

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotNull(message = "描述不能为空")
    private String description;

    @NotNull(message = "状态不能为空")
    private Integer status;

    @NotNull(message = "价格不能为空")
    @Pattern(regexp = "^\\d+(\\.\\d+)?", message = "请输入正确价格")
    private String price;

    @NotEmpty(message = "购买时间不能为空")
    private String buyAt;

    @NotEmpty(message = "结束时间不能为空")
    private String expiredAt;

    @NotNull(message = "购买途径不能为空")
    private Integer buyRouterId;

    @NotNull(message = "厂商不能为空")
    private Integer vendorId;

}
