package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAssetDto {

    @ApiModelProperty(value = "资产编号",example = "1234",reference = "true")
    private int id;
    @ApiModelProperty(value = "资产分类id",example = "1234",reference = "true")
    @NotNull(message = "资产不能为空")
    private String assetCategoryName;
    @ApiModelProperty(value = "厂商名称",example = "小米",reference = "true")
    @NotNull(message = "厂商名不能为空")
    private String vendorName;
    @ApiModelProperty(value = "购入途径名称",example = "线下",reference = "false")
    private String buyRouterName;
    @ApiModelProperty(value = "折旧名称",example = "最省钱",reference = "false")
    private String depreciateName;
    @ApiModelProperty(value = "资产名称",example = "数控",reference = "true")
    @NotNull(message = "设备名不能为空")
    private String name;
    @ApiModelProperty(value = "领用人借用人名称",example = "小王",reference = "false")
    private String userName;
    @ApiModelProperty(value = "设备id",example = "2",reference = "false")
    private int deviceId;
    @ApiModelProperty(value = "价格",example = "20",reference = "false")
    private BigDecimal price;
    @ApiModelProperty(value = "描述",example = "啧啧啧",reference = "false")
    private String description;
    @ApiModelProperty(value = "ip地址",example = "11.22.33.44",reference = "false")
    private String ip;
    @ApiModelProperty(value = "物理地址",example = "x",reference = "false")
    private String mac;
    @ApiModelProperty(value = "购入日期",example = "2021-5-20",reference = "false")
    private Date buyAt;
    @ApiModelProperty(value = "过保时间",example = "2021-5-20",reference = "false")
    private Date expiredAt;
    @ApiModelProperty(value = "创建时间",example = "2021-5-20",reference = "false")
    private Date createdAt;
    @ApiModelProperty(value = "设备状态",example = "闲置",reference = "false")
    private int status;
}
