package com.smile.eam.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceUpdateDto {

    @ApiModelProperty(value = "资产编号",example = "1234",reference = "true")
    @NotNull(message = "资产编号不能为空")
    private int id;
    @ApiModelProperty(value = "设备资产分类编号",example = "1234",reference = "true")
    @NotNull(message = "设备资产分类编号不能为空")
    private int assetCategoryId;
    @ApiModelProperty(value = "厂商名称",example = "1",reference = "true")
    private int vendorId;
    @ApiModelProperty(value = "购入途径名称",example = "1",reference = "false")
    private int buyRouterId;
    @ApiModelProperty(value = "折旧名称",example = "1",reference = "false")
    private int depreciateId;
    @ApiModelProperty(value = "资产名称",example = "数控",reference = "true")
    @NotNull(message = "资产名不能为空")
    private String name;
    @ApiModelProperty(value = "价格",example = "20",reference = "false")
    private String price;
    @ApiModelProperty(value = "描述",example = "啧啧啧",reference = "false")
    private String description;
    @ApiModelProperty(value = "ip地址",example = "11.22.33.44",reference = "false")
    private String ip;
    @ApiModelProperty(value = "物理地址",example = "x",reference = "false")
    private String mac;
    @ApiModelProperty(value = "购入日期",example = "2021-5-20",reference = "false")
    private String buyAt;
    @ApiModelProperty(value = "过保时间",example = "2021-5-20",reference = "false")
    private String expiredAt;

}
