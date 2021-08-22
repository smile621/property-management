package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ConsumableCreateDto {

    @ApiModelProperty(value = "描述信息",example = "很酸的盐或者加盐的酸",reference = "false")
    private String description;
    @ApiModelProperty(value = "耗材名称",example = "盐酸",reference = "true")
    @NotBlank
    private String name;
    @ApiModelProperty(value = "单价",example = "111.22",reference = "true")
    @Digits(integer = 10,fraction = 2)
    private BigDecimal unitPrice;       //必须为小数，整数部位的位数不能超过10，小数位数不能超过2
    @ApiModelProperty(value = "规格",example = "100斤",reference = "false")
    private String specification;
    @ApiModelProperty(value = "耗材分类ID",example = "1",reference = "false")
    @NotNull
    private int categoryId;
    @ApiModelProperty(value = "厂商编号",example = "1234",reference = "false")
    private int vendorId;

}
